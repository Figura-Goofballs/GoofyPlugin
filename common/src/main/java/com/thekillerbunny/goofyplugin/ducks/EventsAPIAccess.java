package com.thekillerbunny.goofyplugin.ducks;

import org.figuramc.figura.lua.api.event.LuaEvent;

public interface EventsAPIAccess {
    public LuaEvent GoofyPlugin$getErrorEvent();
    public LuaEvent GoofyPlugin$getEntityRenderEvent();
}
