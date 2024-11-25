package com.thekillerbunny.goofyplugin.mixin;

import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.lua.FiguraLuaPrinter;
import org.figuramc.figura.lua.FiguraLuaRuntime;
import org.figuramc.figura.lua.api.event.LuaEvent;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.Varargs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.thekillerbunny.goofyplugin.nomixducks.EventsAPIAccess;

@Mixin(value = FiguraLuaRuntime.class, remap = false)
public class FiguraLuaRuntimeMixin {
    @Shadow
    Avatar owner;
    @Shadow
    public static LuaError parseError(Throwable e) {return new LuaError("");}

    private static LuaError prevError;
    private static Boolean errorDuringHandling = false;
    
    @Inject(method = "error", at = @At(value = "HEAD"), cancellable = true)
    public void stopError(Throwable err, CallbackInfo ci) {
        if (errorDuringHandling == false) {
            System.out.println("test3");
            LuaError parsedErr = parseError(err);
            prevError = parsedErr;

            LuaEvent event = ((EventsAPIAccess) owner.luaRuntime.events).GoofyPlugin$getErrorEvent();
            // GoofyPlugin.LOGGER.warn(event.toString());
            if ((event.__len() > 0) && (owner.luaRuntime != null)) {
                errorDuringHandling = true;
                Varargs shouldStopError = owner.run(event, owner.tick, parsedErr.getMessage());

                try {
                    boolean stopError = shouldStopError.checkboolean(1);

                    if (stopError == true) {
                        ci.cancel();
                    }
                }catch (Exception e) {}
            }
        }else {
            if (err != null) {
                FiguraLuaPrinter.sendLuaError(parseError(err), owner);
                FiguraLuaPrinter.sendLuaError(new LuaError("The above error occured while handling the error below (events.ERROR)"), owner);
            }

            owner.scriptError = true;
            owner.luaRuntime = null;
            owner.clearParticles();
            owner.clearSounds();
            owner.closeBuffers();

            ci.cancel();
        }

        errorDuringHandling = false;
    }
}
