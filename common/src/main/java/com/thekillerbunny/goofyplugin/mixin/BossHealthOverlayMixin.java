package com.thekillerbunny.goofyplugin.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.thekillerbunny.goofyplugin.Enums.GuiElement;
import com.thekillerbunny.goofyplugin.GoofyPlugin;

import net.minecraft.client.gui.components.BossHealthOverlay;

@Mixin(value = BossHealthOverlay.class)
public class BossHealthOverlayMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(CallbackInfo ci) {
        if (GoofyPlugin.disabledElements.get(GuiElement.BOSSBAR)) {
            ci.cancel();
        }
    }
}
