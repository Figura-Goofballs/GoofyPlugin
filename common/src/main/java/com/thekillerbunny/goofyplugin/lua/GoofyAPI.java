package com.thekillerbunny.goofyplugin.lua;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.lua.FiguraLuaRuntime;
import org.figuramc.figura.lua.LuaNotNil;
import org.figuramc.figura.lua.LuaWhitelist;
import org.figuramc.figura.lua.docs.LuaMethodDoc;

@LuaWhitelist
public class GoofyAPI {
    private final FiguraLuaRuntime runtime;
    private final Avatar owner;

    public GoofyAPI(Avatar avatar, FiguraLuaRuntime runtime) {
        this.runtime = runtime;
        this.owner = runtime.owner;
    }

    @LuaWhitelist
    @LuaMethodDoc("goofy.regex_match")
    public String[] regexMatch(@LuaNotNil String str, @LuaNotNil String pattern) {
        Pattern goofyPattern = Pattern.compile(pattern);
        Matcher matcher = goofyPattern.matcher(str);
        
        ArrayList<String> matches = new ArrayList<String>();

        while (matcher.find()) {
            matches.add(matcher.group());
        }

        return matches.toArray(new String[0]);
    }

    @LuaWhitelist
    @LuaMethodDoc("goofy.regex_sub")
    public String regexSub(@LuaNotNil String str, @LuaNotNil String pattern, @LuaNotNil String replacement) {
        return str.replaceAll(pattern, replacement);
    }
}
