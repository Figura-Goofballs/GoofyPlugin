package com.thekillerbunny.goofyplugin.mixin;

import com.thekillerbunny.goofyplugin.Enums;
import com.thekillerbunny.goofyplugin.lua.*;
import org.figuramc.figura.lua.docs.FiguraDocsManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Mixin(value = FiguraDocsManager.class, remap = false)
public class FiguraDocsManagerMixin {
    @Shadow
    @Final
    public static Map<String, Collection<Class<?>>> GLOBAL_CHILDREN;

    @Shadow
    @Final
    private static Map<Class<?>, String> NAME_MAP;

    static {
        GLOBAL_CHILDREN.put("goofy", List.of(
            GoofyAPI.class
        ));
        GLOBAL_CHILDREN.put("collection", List.of(
            CollectionAPI.class
        ));
        GLOBAL_CHILDREN.put("backend", List.of(
            BackendAPI.class
        ));

        NAME_MAP.put(Enums.GuiElement.class, "GuiElement");
    }
}
