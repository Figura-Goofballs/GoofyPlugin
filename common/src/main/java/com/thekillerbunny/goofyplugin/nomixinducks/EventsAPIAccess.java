package com.thekillerbunny.goofyplugin.nomixducks;

import org.figuramc.figura.lua.api.event.LuaEvent;

public interface EventsAPIAccess {
    public LuaEvent GoofyPlugin$getErrorEvent();
    public LuaEvent GoofyPlugin$getEntityRenderEvent();

    LuaEvent GoofyPlugin$getPreRenderEvent();
}

