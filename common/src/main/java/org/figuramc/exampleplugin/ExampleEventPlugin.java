package org.figuramc.exampleplugin;

import com.mojang.datafixers.util.Pair;
import org.figuramc.figura.entries.FiguraEvent;
import org.figuramc.figura.entries.annotations.FiguraEventPlugin;
import org.figuramc.figura.lua.LuaWhitelist;
import org.figuramc.figura.lua.api.event.LuaEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

/**
 * Example Event Plugin
 *  Annotation required for Forge to Locate and Load the Plugin
 *  Entrypoint in fabric.mod.json: figura_event
 */
@FiguraEventPlugin
public class ExampleEventPlugin implements FiguraEvent {

    @LuaWhitelist
    public static LuaEvent EXAMPLE = new LuaEvent();
    @Override
    public String getID() {
        return ExamplePlugin.PLUGIN_ID;
    }

    /**
     *  Available so that other mods can add in Events to figura's event API.
     *  You'd call your events from a mixin, for a more concrete example refer to Figura itself
     */
    @Override
    public Collection<Pair<String, LuaEvent>> getEvents() {
        return Collections.singleton(new Pair<>("EXAMPLE", EXAMPLE));
    }
}
