package com.thekillerbunny.goofyplugin.lua;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.lua.FiguraLuaRuntime;
import org.figuramc.figura.lua.LuaNotNil;
import org.figuramc.figura.lua.LuaWhitelist;
import org.figuramc.figura.lua.docs.LuaMethodDoc;
import org.figuramc.figura.lua.docs.LuaMethodOverload;
import org.figuramc.figura.lua.docs.LuaTypeDoc;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

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

    @Override
    public String toString() {
        return "GoofyAPI";
    }
}
