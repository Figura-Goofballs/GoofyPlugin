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

import com.thekillerbunny.goofyplugin.GoofyPlugin;

@Mixin(value = UserData.class, remap = false)
public class UserDataMixin {
    @Shadow
    @Final
    public UUID id;

    @Inject(method = "loadAvatar", at = @At(value = "HEAD"))
    private void loadAvatarHook(CallbackInfo ci) {
        if (FiguraMod.isLocal(id)) {
            GoofyPlugin.disabledElements.put("HOTBAR", false);
            GoofyPlugin.disabledElements.put("JUMP_METER", false);
            GoofyPlugin.disabledElements.put("EXPERIENCE_BAR", false);
            GoofyPlugin.disabledElements.put("SELECTED_ITEM_NAME", false);
            GoofyPlugin.disabledElements.put("SCOREBOARD_SIDEBAR", false);
            GoofyPlugin.disabledElements.put("PLAYER_HEALTH", false);
            GoofyPlugin.disabledElements.put("VEHICLE_HEALTH", false);
            GoofyPlugin.disabledElements.put("TEXTURE_OVERLAY", false);
            GoofyPlugin.disabledElements.put("SPYGLASS_OVERLAY", false);
            GoofyPlugin.disabledElements.put("VIGNETTE", false);
            GoofyPlugin.disabledElements.put("PORTAL_OVERLAY", false);
        }
    }
}
