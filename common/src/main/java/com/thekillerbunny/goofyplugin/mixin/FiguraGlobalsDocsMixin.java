package com.thekillerbunny.goofyplugin.mixin;

import com.thekillerbunny.goofyplugin.lua.GoofyAPI;
import org.figuramc.figura.lua.docs.FiguraGlobalsDocs;
import org.figuramc.figura.lua.docs.LuaFieldDoc;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = FiguraGlobalsDocs.class)
public class FiguraGlobalsDocsMixin {
    @LuaFieldDoc("globals.goofy")
    public GoofyAPI goofy;
}
