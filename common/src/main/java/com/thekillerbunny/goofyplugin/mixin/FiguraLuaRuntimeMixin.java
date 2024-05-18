package com.thekillerbunny.goofyplugin.mixin;

import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.lua.FiguraLuaRuntime;
import org.figuramc.figura.lua.api.event.LuaEvent;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.Varargs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.thekillerbunny.goofyplugin.GoofyPlugin;
import com.thekillerbunny.goofyplugin.ducks.EventsAPIAccess;

@Mixin(value = FiguraLuaRuntime.class, remap = false)
public class FiguraLuaRuntimeMixin {
    @Shadow
    Avatar owner;
    @Shadow
    public static LuaError parseError(Throwable e) {return new LuaError("");}
    
    @Inject(method = "error", at = @At(value = "HEAD"), cancellable = true)
    public void stopError(Throwable err, CallbackInfo ci) {
        LuaEvent event = ((EventsAPIAccess) owner.luaRuntime.events).GoofyPlugin$getErrorEvent();
        // GoofyPlugin.LOGGER.warn(event.toString());
        if ((event.__len() > 0) && (owner.luaRuntime != null)) {
            LuaError parsedErr = parseError(err);
            Varargs shouldStopError = owner.run(event, owner.tick, parsedErr.getMessage());

            try {
                boolean stopError = shouldStopError.checkboolean(1);
                
                if (stopError == false) {
                    ci.cancel();
                }
            }catch (Exception e) {
                if (shouldStopError.isnoneornil(1)) {
                    ci.cancel();
                }
            }
        }
    }
}
