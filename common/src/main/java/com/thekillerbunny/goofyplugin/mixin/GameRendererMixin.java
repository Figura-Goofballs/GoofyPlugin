package com.thekillerbunny.goofyplugin.mixin;

import com.thekillerbunny.goofyplugin.nomixducks.EventsAPIAccess;
import net.minecraft.client.renderer.GameRenderer;
import org.figuramc.figura.avatar.AvatarManager;
import org.luaj.vm2.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    void render(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        for (var avatar: AvatarManager.getLoadedAvatars()) {
            //noinspection DataFlowIssue: the `return null` is never reachable because of the error() call, but Java can't encode never types
            if (avatar.luaRuntime != null && avatar.run(((EventsAPIAccess) avatar.luaRuntime.events).GoofyPlugin$getPreRenderEvent(), avatar.worldRender, tickDelta, startTime, tick).toboolean(1) && avatar.isHost) ci.cancel();
        }
    }
}
