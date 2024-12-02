package com.thekillerbunny.goofyplugin.ducks;

import org.luaj.vm2.LuaFunction;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import org.figuramc.figura.lua.FiguraLuaRuntime;

@Mixin(value = FiguraLuaRuntime.class, remap = false)
public interface FiguraLuaRuntimeAccess {
    @Accessor("setHookFunction")
    LuaFunction getSetHookFunction();
}
