package com.thekillerbunny.goofyplugin.mixin;

import com.thekillerbunny.goofyplugin.GoofyPlugin;
import com.thekillerbunny.goofyplugin.Enums.GuiElement;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.gui.Gui;

@Mixin(Gui.class)
public class GuiMixin {
    @Inject(method = "renderItemHotbar", at = @At("HEAD"), cancellable = true)
    public void renderHotbar(CallbackInfo ci) {
        if (GoofyPlugin.disabledElements.get(GuiElement.HOTBAR)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderJumpMeter", at = @At("HEAD"), cancellable = true)
    public void renderJumpMeter(CallbackInfo ci) {
        if (GoofyPlugin.disabledElements.get(GuiElement.JUMP_METER)) {
            ci.cancel();
        }
    }
    
    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    public void renderExperienceBar(CallbackInfo ci) {
        if (GoofyPlugin.disabledElements.get(GuiElement.EXPERIENCE_BAR)) {
            ci.cancel();
        }
    }
    
    @Inject(method = "renderSelectedItemName", at = @At("HEAD"), cancellable = true)
    public void renderSelectedItemName(CallbackInfo ci) {
        if (GoofyPlugin.disabledElements.get(GuiElement.SELECTED_ITEM_NAME)) {
            ci.cancel();
        }
    }
    
    @Inject(method = "displayScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    public void displayScoreboardSidebar(CallbackInfo ci) {
        if (GoofyPlugin.disabledElements.get(GuiElement.SCOREBOARD_SIDEBAR)) {
            ci.cancel();
        }
    }
        
    @Inject(method = "renderPlayerHealth", at = @At("HEAD"), cancellable = true)
    public void renderPlayerHealth(CallbackInfo ci) {
        if (GoofyPlugin.disabledElements.get(GuiElement.PLAYER_HEALTH)) {
            ci.cancel();
        }
    }
            
    @Inject(method = "renderVehicleHealth", at = @At("HEAD"), cancellable = true)
    public void renderVehicleHealth(CallbackInfo ci) {
        if (GoofyPlugin.disabledElements.get(GuiElement.VEHICLE_HEALTH)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderTextureOverlay", at = @At("HEAD"), cancellable = true)
    public void renderTextureOverlay(CallbackInfo ci) {
        if (GoofyPlugin.disabledElements.get(GuiElement.TEXTURE_OVERLAY)) {
            ci.cancel();
        }
    }
    
    @Inject(method = "renderSpyglassOverlay", at = @At("HEAD"), cancellable = true)
    public void renderSpyglassOverlay(CallbackInfo ci) {
        if (GoofyPlugin.disabledElements.get(GuiElement.SPYGLASS_OVERLAY)) {
            ci.cancel();
        }
    }
    
    @Inject(method = "renderVignette", at = @At("HEAD"), cancellable = true)
    public void renderVignette(CallbackInfo ci) {
        if (GoofyPlugin.disabledElements.get(GuiElement.VIGNETTE)) {
            ci.cancel();
        }
    }
    
    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
    public void renderPortalOverlay(CallbackInfo ci) {
        if (GoofyPlugin.disabledElements.get(GuiElement.PORTAL_OVERLAY)) {
            ci.cancel();
        }
    }
}
