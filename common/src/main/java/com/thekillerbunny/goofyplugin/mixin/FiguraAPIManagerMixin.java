package com.thekillerbunny.goofyplugin.mixin;

import com.thekillerbunny.goofyplugin.lua.*;
import org.figuramc.figura.lua.FiguraAPIManager;
import org.figuramc.figura.lua.FiguraLuaRuntime;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;

@Mixin(value = FiguraAPIManager.class, remap = false)
public class FiguraAPIManagerMixin {
    @Shadow
    @Final
    public static Set<Class<?>> WHITELISTED_CLASSES;

    @Shadow
    @Final
    public static Map<String, Function<FiguraLuaRuntime, Object>> API_GETTERS;

    static {
        WHITELISTED_CLASSES.add(GoofyAPI.class);
        WHITELISTED_CLASSES.add(CollectionAPI.class);
        WHITELISTED_CLASSES.add(BackendAPI.class);

        API_GETTERS.put("goofy", r -> new GoofyAPI(r));
        API_GETTERS.put("collection", r -> new CollectionAPI(r));
        API_GETTERS.put("backend", r -> new BackendAPI(r));
    }
}
