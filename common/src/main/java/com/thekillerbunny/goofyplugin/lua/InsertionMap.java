package com.thekillerbunny.goofyplugin.lua;

import org.figuramc.figura.lua.LuaWhitelist;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.ZeroArgFunction;

import java.util.LinkedHashMap;

@LuaWhitelist
public class InsertionMap {
    private final LinkedHashMap<LuaValue, LuaValue> map = new LinkedHashMap<>();
    public InsertionMap() {}
    public InsertionMap(LuaTable tbl) {
        LuaValue k = LuaValue.NIL;
        for(;;) {
            Varargs n = tbl.next(k);
            if ((k = n.arg1()).isnil()) return;
            map.put(k, n.arg(2));
        }
    }

    @LuaWhitelist
    public LuaValue __index(LuaValue k) {
        return map.get(k);
    }

    @LuaWhitelist
    public void __newindex(LuaValue k, LuaValue v) {
        map.put(k, v);
    }

    @LuaWhitelist
    public LuaValue __pairs() {
        var iter = map.entrySet().iterator();
        return new ZeroArgFunction() {
            @Override
            public LuaValue call() {
                return iter.next().getKey();
            }

            @Override
            public Varargs invoke(Varargs varargs) {
                var entry = iter.next();
                return LuaValue.varargsOf(new LuaValue[] { entry.getKey(), entry.getValue() });
            }
        };
    }
}
