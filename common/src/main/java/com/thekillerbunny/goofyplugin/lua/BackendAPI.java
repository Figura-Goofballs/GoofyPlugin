package com.thekillerbunny.goofyplugin.lua;

import com.google.gson.Gson;
import com.mojang.serialization.JsonOps;
import com.neovisionaries.ws.client.WebSocket;
import net.minecraft.util.ExtraCodecs;
import org.figuramc.figura.backend2.HttpAPI;
import org.figuramc.figura.backend2.NetworkStuff;
import org.figuramc.figura.lua.LuaWhitelist;
import org.figuramc.figura.lua.api.data.FiguraBuffer;
import org.figuramc.figura.lua.docs.LuaMethodDoc;

import java.lang.reflect.Field;

public class BackendAPI {
    @LuaMethodDoc("goofy.backend.connected")
    @LuaWhitelist
    public boolean connected() {
        return NetworkStuff.isConnected();
    }
    @LuaMethodDoc("goofy.backend.can_upload")
    @LuaWhitelist
    public boolean canUpload() {
        return NetworkStuff.canUpload();
    }
    @LuaMethodDoc("goofy.backend.get_avatar_max_size")
    @LuaWhitelist
    public int getAvatarMaxSize() {
        return NetworkStuff.getSizeLimit();
    }
    @LuaMethodDoc("goofy.backend.send_ping")
    @LuaWhitelist
    public void sendPing(int id, boolean sync, byte[] data) {
        NetworkStuff.sendPing(id, sync, data);
    }
    @LuaMethodDoc("goofy.backend.disconnect")
    @LuaWhitelist
    public void disconnect(String message) {
        NetworkStuff.disconnect(message);
    }
    @LuaMethodDoc("goofy.backend.get_token")
    @LuaWhitelist
    public String getToken() {
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
    public void connectWithToken(String token) {
        NetworkStuff.connect(token);
    }
    @LuaMethodDoc("goofy.backend.motd")
    @LuaWhitelist
    public String motd() {
        try {
            Field gsonField = NetworkStuff.class.getDeclaredField("GSON");
            gsonField.setAccessible(true);
            return ((Gson) gsonField.get(null)).toJson(ExtraCodecs.COMPONENT.encodeStart(JsonOps.INSTANCE, NetworkStuff.motd).getOrThrow(true, str -> {}));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
    @LuaMethodDoc("goofy.backend.write")
    @LuaWhitelist
    public void write(FiguraBuffer data) {
        try {
            Field socketField = NetworkStuff.class.getDeclaredField("ws");
            socketField.setAccessible(true);
            Field bufField = FiguraBuffer.class.getDeclaredField("buf");
            bufField.setAccessible(true);
            ((WebSocket) socketField.get(null)).sendBinary((byte[]) bufField.get(data));
        } catch (NoSuchFieldException | IllegalAccessException ignored) {}
    }
}
