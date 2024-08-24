package com.thekillerbunny.goofyplugin.mixin;

import java.util.UUID;

import org.figuramc.figura.FiguraMod;
import org.figuramc.figura.avatar.AvatarManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.thekillerbunny.goofyplugin.GoofyPlugin;
import com.thekillerbunny.goofyplugin.Enums.GuiElement;

@Mixin(value = AvatarManager.class, remap = false)
public class AvatarManagerMixin {
    @Inject(method = "reloadAvatar", at = @At(value = "HEAD"))
    private static void reloadAvatarHook(UUID id, CallbackInfo ci) {
        if (FiguraMod.isLocal(id)) {
            for (GuiElement elem: GuiElement.values()) {
                GoofyPlugin.disabledElements.put(elem, false);
            }
        }
    }

    @Inject(method = "togglePanic", at = @At(value = "HEAD"))
    private static void togglePanicHook(CallbackInfo ci) {
      for (GuiElement elem: GuiElement.values()) {
        GoofyPlugin.disabledElements.put(elem, false);
      }
    }
}
