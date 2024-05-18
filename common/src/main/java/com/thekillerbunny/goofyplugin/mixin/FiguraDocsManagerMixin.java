package com.thekillerbunny.goofyplugin.mixin;

import com.thekillerbunny.goofyplugin.lua.GoofyAPI;
import org.figuramc.figura.lua.docs.FiguraDocsManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(value = FiguraDocsManager.class)
public class FiguraDocsManagerMixin {
    @Shadow
    @Final
    public static Map<String, Collection<Class<?>>> GLOBAL_CHILDREN;


    static {
        GLOBAL_CHILDREN.put("goofy", List.of(
            GoofyAPI.class
        ));
    }
}
