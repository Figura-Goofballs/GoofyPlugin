package com.thekillerbunny.goofyplugin.mixin;

import org.figuramc.figura.lua.LuaWhitelist;
import org.figuramc.figura.lua.api.event.EventsAPI;
import org.figuramc.figura.lua.api.event.LuaEvent;
import org.figuramc.figura.lua.docs.LuaFieldDoc;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.thekillerbunny.goofyplugin.ducks.EventsAPIAccess;

import java.util.Map;

@Mixin(value = EventsAPI.class, remap = false)
public class EventsAPIMixin implements EventsAPIAccess {
    @Shadow
    @Final
    private Map<String, LuaEvent> events;
    
    @Unique
    @LuaWhitelist
    @LuaFieldDoc("events.error")
    public LuaEvent ERROR;

    @Inject(method = "<init>", at = @At("RETURN"))
    void a(CallbackInfo ci) {
        ERROR = new LuaEvent();
        events.put("ERROR", ERROR);
    }

    @Override
    public LuaEvent GoofyPlugin$getErrorEvent() {
        return ERROR;
    }
}
