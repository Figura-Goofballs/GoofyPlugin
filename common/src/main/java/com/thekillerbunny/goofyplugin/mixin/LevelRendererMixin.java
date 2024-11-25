package com.thekillerbunny.goofyplugin.mixin;

import org.figuramc.figura.FiguraMod;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.avatar.AvatarManager;
import org.figuramc.figura.lua.api.entity.EntityAPI;
import org.figuramc.figura.lua.api.event.LuaEvent;
import org.luaj.vm2.Varargs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.vertex.PoseStack;
import com.thekillerbunny.goofyplugin.nomixducks.EventsAPIAccess;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;

@Mixin(value = LevelRenderer.class)
public class LevelRendererMixin {
    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    // I know I don't use these, mixins decided they didn't want to work how they normally do
    private void render(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, CallbackInfo ci) {
        Avatar localPlayer = AvatarManager.getAvatarForPlayer(FiguraMod.getLocalPlayerUUID());

        if (localPlayer == null || localPlayer.luaRuntime == null) {
            return;
        }

        LuaEvent event = ((EventsAPIAccess) localPlayer.luaRuntime.events).GoofyPlugin$getEntityRenderEvent();

        if (event.__len() > 0) {
            Varargs shouldNotRender = localPlayer.run(event, localPlayer.render, tickDelta, EntityAPI.wrap(entity));
            
            try {
                boolean stopRender = shouldNotRender.checkboolean(1);

                if (stopRender) {
                    ci.cancel();
                }
            }catch (Exception e) {}
        }
    }
}
