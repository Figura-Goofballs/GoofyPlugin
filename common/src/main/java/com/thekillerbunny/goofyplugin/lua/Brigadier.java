package com.thekillerbunny.goofyplugin.lua;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.lua.LuaWhitelist;
import org.figuramc.figura.lua.api.entity.EntityAPI;
import org.figuramc.figura.lua.docs.LuaMethodDoc;
import org.figuramc.figura.lua.docs.LuaTypeDoc;
import org.figuramc.figura.utils.FiguraClientCommandSource;
import org.figuramc.figura.utils.LuaUtils;
import org.figuramc.figura.utils.TextUtils;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.function.BiFunction;

@LuaWhitelist
public sealed interface Brigadier<T extends Brigadier<T>> {
    @LuaWhitelist
    @LuaMethodDoc("goofy.brigadier")
    T addChild(Brigadier<?> node);

    @LuaTypeDoc(name = "BrigadierNode", value = "goofy")
    record Node (CommandNode<FiguraClientCommandSource> node) implements Brigadier<Node> {
        @Override
        public Node addChild(Brigadier<?> child) {
            node.addChild(switch (child) {
                case Node(var n) -> n;
                case Builder(_, var b) -> b.build();
            });
            return this;
        }
    }

    @LuaTypeDoc(name = "BrigadierBuilder", value = "goofy")
    @LuaWhitelist
    record Builder<T extends ArgumentBuilder<FiguraClientCommandSource, T>> (Avatar avatar, T builder) implements Brigadier<Builder<T>> {
        @LuaWhitelist
        @Override
        public Builder<T> addChild(Brigadier<?> node) {
            switch (node) {
                case Node(var n) -> builder.then(n);
                case Builder(_, var b) -> builder.then(b);
            }
            return this;
        }

        public void executesJava(BiFunction<LuaValue, FiguraClientCommandSource, Integer> callback) {
            builder.executes(c -> callback.apply(new LuaUserdata(c, new LuaTable() {{
                set("__index", new TwoArgFunction() {
                    @Override
                    public LuaValue call(LuaValue arg1, LuaValue arg2) {
                        try {
                            // TODO: consider changing `c` to `((CommandContext<?>) arg1.checkuserdata(CommandContext.class))` to reuse metatable and the associated performance changes
                            return avatar.luaRuntime.typeManager.javaToLua(c.getArgument(arg2.checkjstring(), Object.class)).arg1();
                        } catch (IllegalArgumentException a) {
                            if (a.getMessage().startsWith("No such argument")) {
                                return LuaValue.NIL;
                            } else {
                                throw new RuntimeException("Failed to retrieve argument from command", a);
                            }
                        }
                    }
                });
            }}), c.getSource()));
        }

        @LuaWhitelist
        public Builder<T> executes(LuaFunction contents) {
            executesJava((a, s) -> {
                try {
                    return switch (avatar.luaRuntime.typeManager.luaToJava(contents.call(a))) {
                        case String m -> {
                            s.figura$sendFeedback(TextUtils.tryParseJson(m));
                            yield 0;
                        }
                        case Integer i -> i;
                        case Boolean b -> b ? 1 : 0;
                        case LuaTable t -> {
                            s.figura$sendFeedback(ExtraCodecs.COMPONENT.decode(JsonOps.INSTANCE, LuaUtils.asJsonValue(t)).getOrThrow(true, e -> { throw new LuaError("Invalid component in command handler: " + t); }).getFirst());
                            yield 0;
                        }
                        case null -> 0;
                        case Object r -> r.hashCode();
                    };
                } catch (Exception err) {
                    throw new CommandRuntimeException(err instanceof LuaError luaError ? switch (avatar.luaRuntime.typeManager.luaToJava(luaError.getMessageObject())) {
                        case String m -> Component.literal(m);
                        case LuaTable t -> ExtraCodecs.COMPONENT.decode(JsonOps.INSTANCE, LuaUtils.asJsonValue(t)).get().map(Pair::getFirst, e -> Component.translatable("goofyplugin.error.while_parsing_error", Component.literal(e.message())));
                        // idk if this is needed
                        case EntityAPI<?> e -> e.getEntity().getName();
                        // other types
                        case LuaNil _ -> Component.translatable("goofyplugin.error.unknown");
                        case Object o -> Component.literal(o.toString());
                    } : Component.nullToEmpty(err.getMessage()));
                }
            });
            return this;
        }
    }
}
