package com.thekillerbunny.goofyplugin.lua;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.figuramc.figura.FiguraMod;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.config.Configs;
import org.figuramc.figura.lua.FiguraLuaRuntime;
import org.figuramc.figura.lua.LuaNotNil;
import org.figuramc.figura.lua.LuaWhitelist;
import org.figuramc.figura.lua.docs.LuaMethodDoc;
import org.figuramc.figura.lua.docs.LuaMethodOverload;
import org.figuramc.figura.lua.docs.LuaTypeDoc;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import com.thekillerbunny.goofyplugin.GoofyPermissionsPlugin;
import com.thekillerbunny.goofyplugin.GoofyPlugin;

@LuaWhitelist
@LuaTypeDoc(name = "GoofyAPI", value = "goofy")
public class GoofyAPI {
    private final FiguraLuaRuntime runtime;
    private final Avatar owner;

    public GoofyAPI(FiguraLuaRuntime runtime) {
        this.runtime = runtime;
        this.owner = runtime.owner;
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
        if ((Configs.LOG_OTHERS.value || FiguraMod.isLocal(owner.owner) && (owner.permissions.get(GoofyPermissionsPlugin.CAN_LOG) == 1))) {
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
        if ((Configs.LOG_OTHERS.value || FiguraMod.isLocal(owner.owner) && (owner.permissions.get(GoofyPermissionsPlugin.CAN_LOG) == 1))) {
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
        if ((Configs.LOG_OTHERS.value || FiguraMod.isLocal(owner.owner) && (owner.permissions.get(GoofyPermissionsPlugin.CAN_LOG) == 1))) {
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
        if ((Configs.LOG_OTHERS.value || FiguraMod.isLocal(owner.owner) && (owner.permissions.get(GoofyPermissionsPlugin.CAN_LOG) == 1))) {
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
        if ((Configs.LOG_OTHERS.value || FiguraMod.isLocal(owner.owner) && (owner.permissions.get(GoofyPermissionsPlugin.CAN_LOG) == 1))) {
            GoofyPlugin.LOGGER.trace("[" + owner.entityName + "] - " + data);
        }
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.is_debug_enabled"
    )
    public boolean isDebugEnabled() {
        return !(!(owner.permissions.get(GoofyPermissionsPlugin.CAN_LOG) == 1) || (!Configs.LOG_OTHERS.value && !FiguraMod.isLocal(owner.owner)) || !GoofyPlugin.LOGGER.isDebugEnabled());
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.is_info_enabled"
    )
    public boolean isInfoEnabled() {
        return !(!(owner.permissions.get(GoofyPermissionsPlugin.CAN_LOG) == 1) || (!Configs.LOG_OTHERS.value && !FiguraMod.isLocal(owner.owner)) || !GoofyPlugin.LOGGER.isInfoEnabled());
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.is_warn_enabled"
    )
    public boolean isWarnEnabled() {
        return !(!(owner.permissions.get(GoofyPermissionsPlugin.CAN_LOG) == 1) || (!Configs.LOG_OTHERS.value && !FiguraMod.isLocal(owner.owner)) || !GoofyPlugin.LOGGER.isWarnEnabled());
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.is_error_enabled"
    )
    public boolean isErrorEnabled() {
        return !(!(owner.permissions.get(GoofyPermissionsPlugin.CAN_LOG) == 1) || (!Configs.LOG_OTHERS.value && !FiguraMod.isLocal(owner.owner)) || !GoofyPlugin.LOGGER.isErrorEnabled());
    }

    @LuaWhitelist
    @LuaMethodDoc(
        value = "goofy.is_trace_enabled"
    )
    public boolean isTraceEnabled() {
        return !(!(owner.permissions.get(GoofyPermissionsPlugin.CAN_LOG) == 1) || (!Configs.LOG_OTHERS.value && !FiguraMod.isLocal(owner.owner)) || !GoofyPlugin.LOGGER.isTraceEnabled());
    }

    @Override
    public String toString() {
        return "GoofyAPI";
    }
}
