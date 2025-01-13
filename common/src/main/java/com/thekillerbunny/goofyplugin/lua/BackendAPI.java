package com.thekillerbunny.goofyplugin.lua;

import com.thekillerbunny.goofyplugin.GoofyPlugin;

import com.google.gson.Gson;
import com.mojang.serialization.JsonOps;
import com.neovisionaries.ws.client.WebSocket;
import net.minecraft.util.ExtraCodecs;

import org.figuramc.figura.FiguraMod;
import org.figuramc.figura.backend2.HttpAPI;
import org.figuramc.figura.backend2.NetworkStuff;
import org.figuramc.figura.lua.LuaWhitelist;
import org.figuramc.figura.lua.api.data.FiguraBuffer;
import org.figuramc.figura.lua.docs.LuaMethodDoc;
import org.figuramc.figura.lua.FiguraLuaRuntime;
import org.figuramc.figura.avatar.Avatar;

import java.lang.reflect.Field;
import java.util.Objects;

import net.minecraft.network.chat.Component;

@LuaWhitelist
public class BackendAPI {
    private final Avatar owner;
    public BackendAPI(FiguraLuaRuntime runtime) {
        this.owner = runtime.owner;
    }

    @LuaMethodDoc("goofy.backend.connected")
    @LuaWhitelist
    public boolean connected() {
        return NetworkStuff.isConnected();
    }
    @LuaMethodDoc("goofy.backend.can_upload")
    @LuaWhitelist
    public boolean canUpload() {
        if (!FiguraMod.isLocal(this.owner.owner)) {
            return false;
        }

        return NetworkStuff.canUpload();
    }
    @LuaMethodDoc("goofy.backend.get_avatar_max_size")
    @LuaWhitelist
    public int getAvatarMaxSize() {
        if (!FiguraMod.isLocal(this.owner.owner)) {
            return 0;
        }

        return NetworkStuff.getSizeLimit();
    }
    @LuaMethodDoc("goofy.backend.send_ping")
    @LuaWhitelist
    public void sendPing(int id, boolean sync, byte[] data) {
        if (!FiguraMod.isLocal(this.owner.owner)) {
            return;
        }

        NetworkStuff.sendPing(id, sync, data);
    }
    @LuaMethodDoc("goofy.backend.disconnect")
    @LuaWhitelist
    public void disconnect(String message) {
        if (!FiguraMod.isLocal(this.owner.owner)) {
            return;
        }

        NetworkStuff.disconnect(message);
    }

    private String getToken() {
        if (!FiguraMod.isLocal(this.owner.owner)) {
            return "";
        }

        try {
            Field apiField = NetworkStuff.class.getDeclaredField("api");
            apiField.setAccessible(true);
            HttpAPI api = (HttpAPI) apiField.get(null);
            Field tokenField = HttpAPI.class.getDeclaredField("token");
            tokenField.setAccessible(true);
            return (String) tokenField.get(api);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new AssertionError("Not possible", e);
        }
    }

    @LuaMethodDoc("goofy.backend.connect_with_token")
    @LuaWhitelist
    public void connect() {
        if (!FiguraMod.isLocal(this.owner.owner)) {
            return;
        }

        NetworkStuff.connect(getToken());
    }
    @LuaMethodDoc("goofy.backend.motd")
    @LuaWhitelist
    public Component motd() {
        return NetworkStuff.motd;
    }
    @LuaMethodDoc("goofy.backend.write")
    @LuaWhitelist
    public void write(FiguraBuffer data) {
        if (!FiguraMod.isLocal(this.owner.owner)) {
            return;
        }

        try {
            Field socketField = NetworkStuff.class.getDeclaredField("ws");
            socketField.setAccessible(true);
            Field bufField = FiguraBuffer.class.getDeclaredField("buf");
            bufField.setAccessible(true);
            ((WebSocket) socketField.get(null)).sendBinary((byte[]) bufField.get(data));
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
    }

    @Override
    public String toString() {
        return "BackendAPI";
    }
}
