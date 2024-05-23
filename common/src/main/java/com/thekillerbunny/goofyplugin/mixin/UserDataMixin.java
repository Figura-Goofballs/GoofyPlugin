package com.thekillerbunny.goofyplugin.mixin;

import java.util.UUID;

import org.figuramc.figura.FiguraMod;
import org.figuramc.figura.avatar.UserData;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.thekillerbunny.goofyplugin.Enums.GuiElement;
import com.thekillerbunny.goofyplugin.GoofyPlugin;

@Mixin(value = UserData.class, remap = false)
public class UserDataMixin {
    @Shadow
    @Final
    public UUID id;

    @Inject(method = "loadAvatar", at = @At(value = "HEAD"))
    private void loadAvatarHook(CallbackInfo ci) {
        if (FiguraMod.isLocal(id)) {
            for (GuiElement elem: GuiElement.values()) {
                GoofyPlugin.disabledElements.put(elem, false);
            }
        }
    }
}
