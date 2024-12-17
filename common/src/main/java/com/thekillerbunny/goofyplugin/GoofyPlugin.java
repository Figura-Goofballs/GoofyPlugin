package com.thekillerbunny.goofyplugin;

import com.thekillerbunny.goofyplugin.lua.BackendAPI;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.entries.FiguraAPI;
import org.figuramc.figura.entries.annotations.FiguraAPIPlugin;
import org.figuramc.figura.lua.LuaWhitelist;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thekillerbunny.goofyplugin.Enums.GuiElement;

import club.bottomservices.discordrpc.lib.DiscordRPCClient;
import club.bottomservices.discordrpc.lib.ErrorEvent;
import club.bottomservices.discordrpc.lib.EventListener;
import club.bottomservices.discordrpc.lib.RichPresence;
import club.bottomservices.discordrpc.lib.User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.HashMap;

import java.io.IOException;

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
    private static String discord_appId = "1243628722599362631";
    public static final Long start_timestamp = System.currentTimeMillis() / 1000;
    public static DiscordRPCClient discordClient;
    private Avatar avatar;

    public static HashMap<GuiElement, Boolean> disabledElements = new HashMap<GuiElement, Boolean>();

    public GoofyPlugin() {
    }
    public GoofyPlugin(Avatar avatar) {
        this.avatar = avatar;
    }

    /**
     * You can do common things on init here
     */
    
    public static void init() {
        if (discord_appId == null) {
            discord_appId = "1243628722599362631";
        }

        for (GuiElement elem: GuiElement.values()) {
            GoofyPlugin.disabledElements.put(elem, false);
        }

        discordClient = new DiscordRPCClient(new EventListener() {
            @SuppressWarnings("null")
            @Override
            public void onReady(@NotNull DiscordRPCClient client, @NotNull User user) {
                LOGGER.info("DiscordRPC Ready");
                long timestamp = System.currentTimeMillis() / 1000;
                
                RichPresence.Builder builder = new RichPresence.Builder()
                    .setTimestamps(start_timestamp, null)
                    .setAssets("LARGE_IMAGE", "test", "SMALL_IMAGE", "test_small")
                    .setText("test_details", "PLAYING");

                client.sendPresence(builder.build());
            }

            @SuppressWarnings("null")
            @Override
            public void onError(@NotNull DiscordRPCClient client, @Nullable IOException exception, @Nullable ErrorEvent event) {
                if (exception != null) {
                    LOGGER.error("DiscordRPC error with IOException", exception);
                } else if (event != null) {
                    LOGGER.error("DiscordRPC error with ErrorEvent code {} and message {}", event.code, event.message);
                }
            }
        }, GoofyPlugin.discord_appId);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                if (discordClient.isConnected) {
                    discordClient.disconnect();
                }
            }
        });

        discordClient.connect();

        if (discordClient.isConnected) {
            RichPresence.Builder builder = new RichPresence.Builder()
                .setTimestamps(start_timestamp, null)
                .setAssets("LARGE_IMAGE", "test", "SMALL_IMAGE", "test_small")
                .setText("test_details", "PLAYING");

            discordClient.sendPresence(builder.build());
        }
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
            BackendAPI.class,
    };

}
