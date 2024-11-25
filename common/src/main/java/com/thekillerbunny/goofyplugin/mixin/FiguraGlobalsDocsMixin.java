package com.thekillerbunny.goofyplugin.mixin;

import com.thekillerbunny.goofyplugin.lua.*;
import org.figuramc.figura.lua.docs.FiguraGlobalsDocs;
import org.figuramc.figura.lua.docs.LuaFieldDoc;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = FiguraGlobalsDocs.class, remap = false)
public class FiguraGlobalsDocsMixin {
    @LuaFieldDoc("globals.goofy")
    public GoofyAPI goofy;
    
    @LuaFieldDoc("globals.collection")
    public CollectionAPI collection;
    
    @LuaFieldDoc("globals.backend")
    public BackendAPI backend;
}
