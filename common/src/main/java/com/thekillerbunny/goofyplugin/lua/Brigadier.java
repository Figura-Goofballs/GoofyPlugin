package com.thekillerbunny.goofyplugin.lua;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ExtraCodecs;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.lua.LuaNotNil;
import org.figuramc.figura.lua.LuaWhitelist;
import org.figuramc.figura.lua.api.entity.EntityAPI;
import org.figuramc.figura.lua.docs.LuaMethodDoc;
import org.figuramc.figura.lua.docs.LuaTypeDoc;
import org.figuramc.figura.utils.FiguraClientCommandSource;
import org.figuramc.figura.utils.LuaUtils;
import org.figuramc.figura.utils.TextUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.TwoArgFunction;

import java.util.function.BiFunction;

@LuaWhitelist
public sealed interface Brigadier<T extends Brigadier<T>> {
    @LuaWhitelist
    @LuaMethodDoc("goofy.brigadier")
    T addChild(Brigadier<?> node);

    CommandNode<FiguraClientCommandSource> toBuilt();

    @LuaTypeDoc(name = "BrigadierNode", value = "goofy")
    record Node (CommandNode<FiguraClientCommandSource> node) implements Brigadier<Node> {
        @Override
        public Node addChild(Brigadier<?> child) {
            node.addChild(child.toBuilt());
            return this;
        }

        @Override
        public CommandNode<FiguraClientCommandSource> toBuilt() {
            return node;
        }
    }

    @LuaTypeDoc(name = "BrigadierBuilder", value = "goofy")
    @LuaWhitelist
    record Builder<T extends ArgumentBuilder<FiguraClientCommandSource, T>> (Avatar avatar, T builder) implements Brigadier<Builder<T>> {
        @LuaWhitelist
        @Override
        public Builder<T> addChild(Brigadier<?> node) {
            builder.then(node.toBuilt());
            return this;
        }

        @Override
        public CommandNode<FiguraClientCommandSource> toBuilt() {
            return builder.build();
        }

        @Contract(mutates = "this")
        public void executesJava(BiFunction<LuaValue, FiguraClientCommandSource, Integer> callback) {
            builder.executes(c -> callback.apply(wrapArgs(c), c.getSource()));
        }

        @Contract(value = "!null -> new", pure = true)
        @NotNull LuaUserdata wrapArgs(CommandContext<FiguraClientCommandSource> c) {
            return new LuaUserdata(c, new LuaTable() {{
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
            }});
        }

        @LuaWhitelist
        @Contract(value = "_ -> this", mutates = "this, param")
        public Builder<T> executes(LuaFunction contents) {
            executesJava((a, s) -> {
                try {
                    var value = avatar.luaRuntime.typeManager.luaToJava(contents.call(a));
                    if (value instanceof String m) {
                        s.figura$sendFeedback(TextUtils.tryParseJson(m));
                        return 0;
                    } else if (value instanceof Integer i) {
                        return i;
                    } else if (value == Boolean.TRUE) {
                        return 1;
                    } else if (value == null || value == Boolean.FALSE) {
                        return 0;
                    } else if (value instanceof LuaTable t) {
                        s.figura$sendFeedback(ExtraCodecs.COMPONENT.decode(JsonOps.INSTANCE, LuaUtils.asJsonValue(t)).getOrThrow(true, e -> { throw new LuaError("Invalid component in command handler: " + t); }).getFirst());
                        return 0;
                    } else {
                        return 0;
                    }
                } catch (Exception err) {
                    throw new CommandRuntimeException(Component.nullToEmpty(err.getMessage()));
                }
            });
            return this;
        }
    }

    @LuaWhitelist
    @LuaTypeDoc(name = "Suggestions", value = "goofy")
    record Suggestions (SuggestionsBuilder builder) {
        @LuaWhitelist
        @LuaMethodDoc("goofy.brigadier.suggestions.add")
        @Contract(value = "!null, _ -> this; null, _ -> fail", mutates = "this")
        public Suggestions add(@LuaNotNil String value, String tooltip) {
            if (tooltip != null) {
                builder.suggest(value, () -> tooltip);
            } else {
                builder.suggest(value);
            }
            return this;
        }

        @LuaWhitelist
        @LuaMethodDoc("goofy.brigadier.suggestions.offset")
        @Contract(pure = true)
        public Object offset(Integer offset) {
            if (offset == null) {
                return builder.getStart();
            } else {
                return new Suggestions(builder.createOffset(offset));
            }
        }

        @LuaWhitelist
        @LuaMethodDoc("goofy.brigadier.suggestions.text")
        @Contract(pure = true)
        public String text(@Nullable Boolean all) {
            return all == Boolean.TRUE ? builder.getRemaining() : builder.getInput();
        }
    }
}
