package com.thekillerbunny.goofyplugin.lua;

import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.lua.FiguraLuaRuntime;
import org.figuramc.figura.lua.LuaNotNil;
import org.figuramc.figura.lua.LuaWhitelist;
import org.figuramc.figura.lua.docs.LuaMethodDoc;
import org.figuramc.figura.lua.docs.LuaMethodOverload;
import org.figuramc.figura.lua.docs.LuaTypeDoc;
import org.figuramc.figura.math.vector.FiguraVec2;
import org.figuramc.figura.math.vector.FiguraVec3;
import org.figuramc.figura.math.vector.FiguraVec4;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.jse.CoerceLuaToJava;

@LuaWhitelist
@LuaTypeDoc(name = "CollectionAPI", value = "collection")
public class CollectionAPI {
    private final FiguraLuaRuntime runtime;
    private final Avatar owner;

    public CollectionAPI(FiguraLuaRuntime runtime) {
        this.runtime = runtime;
        this.owner = runtime.owner;
    }

    @LuaWhitelist
    @LuaMethodDoc(
        overloads = {
            @LuaMethodOverload(
                argumentTypes = {LuaTable.class},
                argumentNames = {"tbl"}
            ),

        },
        value = "collection.sum"
    )
    public LuaValue sum(@LuaNotNil LuaTable tbl) {
        if (tbl.length() == 0) {
            return LuaValue.NIL;
        }

        LuaValue first = tbl.get(1);
        if (first.isnumber()) {
            double[] arr = (double[]) CoerceLuaToJava.coerce(tbl, double[].class);
            double sum = 0.0;
            for (double v : arr) {
                sum += v;
            }
            return LuaValue.valueOf(sum);
        } else if (first.isuserdata(FiguraVec2.class)) {
            FiguraVec2 sum = FiguraVec2.of(0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                sum.add((FiguraVec2) tbl.get(i).checkuserdata(FiguraVec2.class));
            }
            return LuaValue.userdataOf(sum);
        } else if (first.isuserdata(FiguraVec3.class)) {
            FiguraVec3 sum = FiguraVec3.of(0, 0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                sum.add((FiguraVec3) tbl.get(i).checkuserdata(FiguraVec3.class));
            }
            return LuaValue.userdataOf(sum);
        } else if (first.isuserdata(FiguraVec4.class)) {
            FiguraVec4 sum = FiguraVec4.of(0, 0, 0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                sum.add((FiguraVec4) tbl.get(i).checkuserdata(FiguraVec4.class));
            }
            return LuaValue.userdataOf(sum);
        } else {
            throw new LuaError("Unsupported array type");
        }
    }

    @LuaWhitelist
    @LuaMethodDoc(
            overloads = {
                    @LuaMethodOverload(
                            argumentTypes = {LuaTable.class},
                            argumentNames = {"tbl"}
                    ),

            },
            value = "collection.sum"
    )
    public LuaValue difference(@LuaNotNil LuaTable tbl) {
        if (tbl.length() == 0) {
            return LuaValue.NIL;
        }

        LuaValue first = tbl.get(1);
        if (first.isnumber()) {
            double[] arr = (double[]) CoerceLuaToJava.coerce(tbl, double[].class);
            double difference = 0.0;
            for (double v : arr) {
                difference -= v;
            }
            return LuaValue.valueOf(difference);
        } else if (first.isuserdata(FiguraVec2.class)) {
            FiguraVec2 sum = FiguraVec2.of(0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                sum.subtract((FiguraVec2) tbl.get(i).checkuserdata(FiguraVec2.class));
            }
            return LuaValue.userdataOf(sum);
        } else if (first.isuserdata(FiguraVec3.class)) {
            FiguraVec3 sum = FiguraVec3.of(0, 0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                sum.subtract((FiguraVec3) tbl.get(i).checkuserdata(FiguraVec3.class));
            }
            return LuaValue.userdataOf(sum);
        } else if (first.isuserdata(FiguraVec4.class)) {
            FiguraVec4 sum = FiguraVec4.of(0, 0, 0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                sum.subtract((FiguraVec4) tbl.get(i).checkuserdata(FiguraVec4.class));
            }
            return LuaValue.userdataOf(sum);
        } else {
            throw new LuaError("Unsupported array type");
        }
    }

    @LuaWhitelist
    @LuaMethodDoc(
            overloads = {
                    @LuaMethodOverload(
                            argumentTypes = {LuaTable.class},
                            argumentNames = {"tbl"}
                    ),

            },
            value = "collection.sum"
    )
    public LuaValue product(@LuaNotNil LuaTable tbl) {
        if (tbl.length() == 0) {
            return LuaValue.NIL;
        }

        LuaValue first = tbl.get(1);
        if (first.isnumber()) {
            double[] arr = (double[]) CoerceLuaToJava.coerce(tbl, double[].class);
            double product = 1.0;
            for (double v : arr) {
                product *= v;
            }
            return LuaValue.valueOf(product);
        } else if (first.isuserdata(FiguraVec2.class)) {
            FiguraVec2 sum = FiguraVec2.of(0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                sum.multiply((FiguraVec2) tbl.get(i).checkuserdata(FiguraVec2.class));
            }
            return LuaValue.userdataOf(sum);
        } else if (first.isuserdata(FiguraVec3.class)) {
            FiguraVec3 sum = FiguraVec3.of(0, 0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                sum.multiply((FiguraVec3) tbl.get(i).checkuserdata(FiguraVec3.class));
            }
            return LuaValue.userdataOf(sum);
        } else if (first.isuserdata(FiguraVec4.class)) {
            FiguraVec4 sum = FiguraVec4.of(0, 0, 0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                sum.multiply((FiguraVec4) tbl.get(i).checkuserdata(FiguraVec4.class));
            }
            return LuaValue.userdataOf(sum);
        } else {
            throw new LuaError("Unsupported array type");
        }
    }

    @LuaWhitelist
    @LuaMethodDoc(
            overloads = {
                    @LuaMethodOverload(
                            argumentTypes = {LuaTable.class},
                            argumentNames = {"tbl"}
                    ),

            },
            value = "collection.sum"
    )
    public LuaValue quotient(@LuaNotNil LuaTable tbl) {
        if (tbl.length() == 0) {
            return LuaValue.NIL;
        }

        LuaValue first = tbl.get(1);
        if (first.isnumber()) {
            double[] arr = (double[]) CoerceLuaToJava.coerce(tbl, double[].class);
            double quotient = 1.0;
            for (double v : arr) {
                quotient /= v;
            }
            return LuaValue.valueOf(quotient);
        } else if (first.isuserdata(FiguraVec2.class)) {
            FiguraVec2 sum = FiguraVec2.of(0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                sum.divide((FiguraVec2) tbl.get(i).checkuserdata(FiguraVec2.class));
            }
            return LuaValue.userdataOf(sum);
        } else if (first.isuserdata(FiguraVec3.class)) {
            FiguraVec3 sum = FiguraVec3.of(0, 0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                sum.divide((FiguraVec3) tbl.get(i).checkuserdata(FiguraVec3.class));
            }
            return LuaValue.userdataOf(sum);
        } else if (first.isuserdata(FiguraVec4.class)) {
            FiguraVec4 sum = FiguraVec4.of(0, 0, 0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                sum.divide((FiguraVec4) tbl.get(i).checkuserdata(FiguraVec4.class));
            }
            return LuaValue.userdataOf(sum);
        } else {
            throw new LuaError("Unsupported array type");
        }
    }

    @Override
    public String toString() {
        return "CollectionAPI";
    }
}
