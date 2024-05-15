package com.thekillerbunny.goofyplugin.mixin;

import com.thekillerbunny.goofyplugin.lua.GoofyAPI;
import org.figuramc.figura.lua.FiguraAPIManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Set;

@Mixin(value = FiguraAPIManager.class, remap = false)
public class FiguraAPIManagerMixin {
    @Shadow
    @Final
    public static Set<Class<?>> WHITELISTED_CLASSES;

    static {
        WHITELISTED_CLASSES.add(GoofyAPI.class);
    }
}