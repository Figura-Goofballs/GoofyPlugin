package com.thekillerbunny.goofyplugin;

import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.entries.FiguraAPI;
import org.figuramc.figura.entries.annotations.FiguraAPIPlugin;
import org.figuramc.figura.lua.LuaWhitelist;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import static java.util.Map.entry;

/**
 * Example API Plugin
 *  Annotation required for Forge to Locate and Load the Plugin
 *  Entrypoint in fabric.mod.json: figura_api
 *  Fabric requires entrypoints have an empty constructor, Figura will not use it
 */
@FiguraAPIPlugin
@LuaWhitelist
public class GoofyPlugin implements FiguraAPI {
    public static final String PLUGIN_ID = "goofyplugin";
    public static final Logger LOGGER = LoggerFactory.getLogger("GoofyPlugin");
    private Avatar avatar;

    public static HashMap<String, Boolean> disabledElements = new HashMap<String, Boolean>();

    public GoofyPlugin() {
    }
    public GoofyPlugin(Avatar avatar) {
        this.avatar = avatar;
    }

    /**
     * You can do common things on init here
     */
    
    public static void init() {
        disabledElements.put("HOTBAR", false);
        disabledElements.put("JUMP_METER", false);
        disabledElements.put("EXPERIENCE_BAR", false);
        disabledElements.put("SELECTED_ITEM_NAME", false);
        disabledElements.put("SCOREBOARD_SIDEBAR", false);
        disabledElements.put("PLAYER_HEALTH", false);
        disabledElements.put("VEHICLE_HEALTH", false);
        disabledElements.put("TEXTURE_OVERLAY", false);
        disabledElements.put("SPYGLASS_OVERLAY", false);
        disabledElements.put("VIGNETTE", false);
        disabledElements.put("PORTAL_OVERLAY", false);
    
        LOGGER.info("Hello multi-loader world!");
    }

    @Override
    public FiguraAPI build(Avatar avatar) {
        return new GoofyPlugin(avatar);
    }

    @Override
    public String getName() {
        return PLUGIN_ID;
    }

    /**
     * You must whitelist your classes for your Plugin to work correctly! This cannot be null
     */
    @Override
    public Collection<Class<?>> getWhitelistedClasses() {
        List<Class<?>> classesToRegister = new ArrayList<>();
        for (Class<?> aClass : GOOFY_PLUGIN_CLASSES) {
            if (aClass.isAnnotationPresent(LuaWhitelist.class)) {
                classesToRegister.add(aClass);
            }
        }
        return classesToRegister;
    }

    /**
     * This can be empty, but not null
     */
    @Override
    public Collection<Class<?>> getDocsClasses() {
        return List.of();
    }

    public static final Class<?>[] GOOFY_PLUGIN_CLASSES = new Class[] {
            GoofyPlugin.class,
    };

}
