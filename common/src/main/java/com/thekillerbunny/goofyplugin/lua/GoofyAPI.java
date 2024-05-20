package com.thekillerbunny.goofyplugin.lua;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.figuramc.figura.FiguraMod;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.avatar.AvatarManager;
import org.figuramc.figura.avatar.UserData;
import org.figuramc.figura.backend2.NetworkStuff;
import org.figuramc.figura.config.Configs;
import org.figuramc.figura.lua.FiguraLuaRuntime;
import org.figuramc.figura.lua.LuaNotNil;
import org.figuramc.figura.lua.LuaWhitelist;
import org.figuramc.figura.lua.docs.LuaMethodDoc;
import org.figuramc.figura.lua.docs.LuaMethodOverload;
import org.figuramc.figura.lua.docs.LuaTypeDoc;
import org.figuramc.figura.utils.ColorUtils;
import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

import com.thekillerbunny.goofyplugin.GoofyPermissionsPlugin;
import com.thekillerbunny.goofyplugin.GoofyPlugin;

import net.minecraft.network.chat.Component;

@LuaWhitelist
@LuaTypeDoc(name = "GoofyAPI", value = "goofy")
public class GoofyAPI {
    private final FiguraLuaRuntime runtime;
    private final Avatar owner;

    public GoofyAPI(FiguraLuaRuntime runtime) {
        this.runtime = runtime;
        this.owner = runtime.owner;
    }

    public boolean canLog() {
        return ((Configs.LOG_OTHERS.value || FiguraMod.isLocal(owner.owner)) && owner.permissions.get(GoofyPermissionsPlugin.CAN_LOG) == 1);
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {Double[].class},
                argumentNames = {"tbl"}
            )
        },
        value = "goofy.sum"
    )
    public Double sum(@LuaNotNil Double[] args) {
        Double finalNumber = 0.0;

        for (Double arg : args) {
            finalNumber += arg;
        }

        return finalNumber;
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {Double[].class},
                argumentNames = {"tbl"}
            )
        },
        value = "goofy.difference"
    )
    public Double difference(@LuaNotNil Double[] args) {
        Double finalNumber = 0.0;

        for (Double arg : args) {
            finalNumber -= arg;
        }

        return finalNumber;
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {Double[].class},
                argumentNames = {"tbl"}
            )
        },
        value = "goofy.product"
    )
    public Double product(@LuaNotNil Double[] args) {
        Double finalNumber = 1.0;

        for (Double arg : args) {
            finalNumber *= arg;
        }

        return finalNumber;
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {Double[].class},
                argumentNames = {"tbl"}
            )
        },
        value = "goofy.quotient"
    )
    public Double quotient(@LuaNotNil Double[] args) {
        Double finalNumber = 1.0;

        for (Double arg : args) {
            finalNumber /= arg;
        }

        return finalNumber;
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.stop_avatar"
    )
    public void stopAvatar() {
        owner.errorText = Component.literal("Execution forcefully aborted by script").withStyle(ColorUtils.Colors.LUA_ERROR.style);
        owner.scriptError = true;
        owner.luaRuntime = null;
        owner.clearParticles();
        owner.clearSounds();
        owner.closeBuffers();
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class, String.class},
                argumentNames = {"str", "pattern"}
            )
        },
        value = "goofy.regex_match"
    )
    public LuaTable regexMatch(@LuaNotNil String str, @LuaNotNil String pattern) {
        Pattern goofyPattern = Pattern.compile(pattern);
        Matcher matcher = goofyPattern.matcher(str);
        
        ArrayList<String> matches = new ArrayList<String>();

        while (matcher.find()) {
            matches.add(matcher.group());
        }

        LuaTable tbl = new LuaTable();
        
        for (int i = 0; i < matches.size(); i++) {
            tbl.set(i + 1, LuaValue.valueOf(matches.get(i)));
        }

        return tbl;
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class, String.class, String.class},
                argumentNames = {"str", "pattern", "replacement"}
            )
        },
        value = "goofy.regex_sub"
    )
    public String regexSub(@LuaNotNil String str, @LuaNotNil String pattern, @LuaNotNil String replacement) {
        return str.replaceAll(pattern, replacement);
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class},
                argumentNames = {"data"}
            )
        },
        value = "goofy.debug_to_log"
    )
    public void debugToLog(@LuaNotNil String data) {
        if (canLog()) {
            GoofyPlugin.LOGGER.debug("[" + owner.entityName + "] - " + data);
        }
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class},
                argumentNames = {"data"}
            )
        },
        value = "goofy.info_to_log"
    )
    public void infoToLog(@LuaNotNil String data) {
        if (canLog()) {
            GoofyPlugin.LOGGER.info("[" + owner.entityName + "] - " + data);
        }
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class},
                argumentNames = {"data"}
            )
        },
        value = "goofy.warn_to_log"
    )
    public void warnToLog(@LuaNotNil String data) {
        if (canLog()) {
            GoofyPlugin.LOGGER.warn("[" + owner.entityName + "] - " + data);
        }
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class},
                argumentNames = {"data"}
            )
        },
        value = "goofy.error_to_log"
    )
    public void errorToLog(@LuaNotNil String data) {
        if (canLog()) {
            GoofyPlugin.LOGGER.error("[" + owner.entityName + "] - " + data);
        }
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class},
                argumentNames = {"data"}
            )
        },
        value = "goofy.trace_to_log"
    )
    public void traceToLog(@LuaNotNil String data) {
        if (canLog()) {
            GoofyPlugin.LOGGER.trace("[" + owner.entityName + "] - " + data);
        }
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.is_debug_enabled"
    )
    public boolean isDebugEnabled() {
        return canLog() && GoofyPlugin.LOGGER.isDebugEnabled();
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.is_info_enabled"
    )
    public boolean isInfoEnabled() {
        return !(!canLog() || GoofyPlugin.LOGGER.isInfoEnabled());
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.is_warn_enabled"
    )
    public boolean isWarnEnabled() {
        return canLog() && GoofyPlugin.LOGGER.isWarnEnabled();
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.is_error_enabled"
    )
    public boolean isErrorEnabled() {
        return canLog() && GoofyPlugin.LOGGER.isErrorEnabled();
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.is_trace_enabled"
    )
    public boolean isTraceEnabled() {
        return canLog() && GoofyPlugin.LOGGER.isTraceEnabled();
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.what_does_bumpscocity_do"
    )
    public String whatDoesBumpscocityDo() {
        throw new LuaError("");
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.get_bumpscocity"
    )
    public int getBumpscocity() {
        if (owner.permissions.get(GoofyPermissionsPlugin.BUMPSCOCITY) > 1000) {
            throw new LuaError("Dear god, this is way too much bumpscocity! (max 1000)");
        }
        return owner.permissions.get(GoofyPermissionsPlugin.BUMPSCOCITY);
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class},
                argumentNames = {"playerUUID"}
            )
        },
        value = "goofy.load_avatar"
    )
    public void loadAvatar(String playerUUID) {
        UUID uuid = UUID.fromString(playerUUID);
        if (AvatarManager.getAvatarForPlayer(uuid) == null) {
            return;
        }
        NetworkStuff.getUser(new UserData(uuid));
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {String.class},
                argumentNames = {"playerUUID"}
            )
        },
        value = "goofy.reload_avatar"
    )
    public void reloadAvatar(String playerUUID) {
        UUID uuid = UUID.fromString(playerUUID);
        if (AvatarManager.getAvatarForPlayer(uuid) == null) {
            return;
        }
        AvatarManager.reloadAvatar(uuid);
    }

    @Override
    public String toString() {
        return "GoofyAPI";
    }
}
