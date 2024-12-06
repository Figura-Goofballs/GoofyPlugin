package com.thekillerbunny.goofyplugin.mixin;

import com.thekillerbunny.goofyplugin.Enums;
import com.thekillerbunny.goofyplugin.GoofyPlugin;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Inject(at = @At("HEAD"), method = "renderWithTooltip", cancellable = true)
    void renderWithTooltip(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (GoofyPlugin.disabledElements.get(Enums.GuiElement.SCREEN)) {
            ci.cancel();
        }
    }
}
