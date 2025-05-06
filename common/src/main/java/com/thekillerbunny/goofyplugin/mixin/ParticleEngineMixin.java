package com.thekillerbunny.goofyplugin.mixin;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import org.figuramc.figura.avatar.AvatarManager;
import org.figuramc.figura.lua.api.particle.LuaParticle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public class ParticleEngineMixin {
    @Inject(at = @At("HEAD"), method = "add", cancellable = true)
    private void figura$checkParticle(Particle particle, CallbackInfo ci) {
        LuaParticle luaParticle = new LuaParticle("event", particle, null);
        AvatarManager.executeAll("checkParticle", avatar -> {
            var results = avatar.run("PARTICLE_CREATED", avatar.worldRender, luaParticle);
            if (results == null) return;
            for (int i = 0; i < results.narg(); i++) {
                var value = results.arg(1);
                if (value.isboolean() && value.toboolean()) ci.cancel();
            }
        });
    }
}
