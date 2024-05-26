package com.thekillerbunny.goofyplugin.lua;

import org.apache.logging.log4j.Level;
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
    public Object sum(@LuaNotNil LuaTable tbl) {
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
            return sum;
        } else if (first.isuserdata(FiguraVec2.class)) {
            FiguraVec2 sum = FiguraVec2.of(0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                sum.add((FiguraVec2) tbl.get(i).checkuserdata(FiguraVec2.class));
            }
            return sum;
        } else if (first.isuserdata(FiguraVec3.class)) {
            FiguraVec3 sum = FiguraVec3.of(0, 0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                sum.add((FiguraVec3) tbl.get(i).checkuserdata(FiguraVec3.class));
            }
            return sum;
        } else if (first.isuserdata(FiguraVec4.class)) {
            FiguraVec4 sum = FiguraVec4.of(0, 0, 0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                sum.add((FiguraVec4) tbl.get(i).checkuserdata(FiguraVec4.class));
            }
            return sum;
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
    public Object difference(@LuaNotNil LuaTable tbl) {
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
            return difference;
        } else if (first.isuserdata(FiguraVec2.class)) {
            FiguraVec2 difference = FiguraVec2.of(0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                difference.subtract((FiguraVec2) tbl.get(i).checkuserdata(FiguraVec2.class));
            }
            return difference;
        } else if (first.isuserdata(FiguraVec3.class)) {
            FiguraVec3 difference = FiguraVec3.of(0, 0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                difference.subtract((FiguraVec3) tbl.get(i).checkuserdata(FiguraVec3.class));
            }
            return difference;
        } else if (first.isuserdata(FiguraVec4.class)) {
            FiguraVec4 difference = FiguraVec4.of(0, 0, 0, 0);
            for (int i = 1; i <= tbl.length(); i++) {
                difference.subtract((FiguraVec4) tbl.get(i).checkuserdata(FiguraVec4.class));
            }
            return difference;
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
    public Object product(@LuaNotNil LuaTable tbl) {
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
            return product;
        } else if (first.isuserdata(FiguraVec2.class)) {
            FiguraVec2 product = FiguraVec2.of(1, 1);
            for (int i = 1; i <= tbl.length(); i++) {
                product.multiply((FiguraVec2) tbl.get(i).checkuserdata(FiguraVec2.class));
            }
            return product;
        } else if (first.isuserdata(FiguraVec3.class)) {
            FiguraVec3 product = FiguraVec3.of(1, 1, 1);
            for (int i = 1; i <= tbl.length(); i++) {
                product.multiply((FiguraVec3) tbl.get(i).checkuserdata(FiguraVec3.class));
            }
            return product;
        } else if (first.isuserdata(FiguraVec4.class)) {
            FiguraVec4 product = FiguraVec4.of(1, 1, 1, 1);
            for (int i = 1; i <= tbl.length(); i++) {
                product.multiply((FiguraVec4) tbl.get(i).checkuserdata(FiguraVec4.class));
            }
            return product;
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
    public Object quotient(@LuaNotNil LuaTable tbl) {
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
            return quotient;
        } else if (first.isuserdata(FiguraVec2.class)) {
            FiguraVec2 quotient = FiguraVec2.of(1, 1);
            for (int i = 1; i <= tbl.length(); i++) {
                quotient.divide((FiguraVec2) tbl.get(i).checkuserdata(FiguraVec2.class));
            }
            return quotient;
        } else if (first.isuserdata(FiguraVec3.class)) {
            FiguraVec3 quotient = FiguraVec3.of(1, 1, 1);
            for (int i = 1; i <= tbl.length(); i++) {
                quotient.divide((FiguraVec3) tbl.get(i).checkuserdata(FiguraVec3.class));
            }
            return quotient;
        } else if (first.isuserdata(FiguraVec4.class)) {
            FiguraVec4 quotient = FiguraVec4.of(1, 1, 1, 1);
            for (int i = 1; i <= tbl.length(); i++) {
                quotient.divide((FiguraVec4) tbl.get(i).checkuserdata(FiguraVec4.class));
            }
            return quotient;
        } else {
            throw new LuaError("Unsupported array type");
        }
    }

    @LuaWhitelist
    @LuaMethodDoc(
            overloads = {
                    @LuaMethodOverload(
                            argumentTypes = {LuaTable.class, LuaFunction.class},
                            argumentNames = {"tbl", "func"}
                    ),

            },
            value = "collection.map"
    )
    public LuaTable map(@LuaNotNil LuaTable tbl, LuaFunction func) {
        for (LuaValue key : tbl.keys()) {
            tbl.set(key, func.call(tbl.get(key)));
        }
        return tbl;
    }

    @LuaWhitelist
    @LuaMethodDoc(
            overloads = {
                    @LuaMethodOverload(
                            argumentTypes = {LuaTable.class, LuaFunction.class},
                            argumentNames = {"tbl", "func"}
                    ),

            },
            value = "collection.each"
    )
    public LuaTable each(@LuaNotNil LuaTable tbl, LuaFunction func) {
        for (LuaValue key : tbl.keys()) {
            func.call(tbl.get(key));
        }
        return tbl;
    }

    @LuaWhitelist
    @LuaMethodDoc(
            overloads = {
                    @LuaMethodOverload(
                            argumentTypes = {LuaTable.class, LuaFunction.class},
                            argumentNames = {"tbl", "func"}
                    ),

            },
            value = "collection.flatMap"
    )
    public LuaTable flatMap(@LuaNotNil LuaTable tbl, LuaFunction func) {
        LuaTable result = new LuaTable();
        for (LuaValue key : tbl.keys()) {
            LuaTable subTable = (LuaTable) func.call(tbl.get(key));
            for (LuaValue subKey : subTable.keys()) {
                result.set(result.length() + 1, subTable.get(subKey));
            }
        }
        return result;
    }

    @LuaWhitelist
    @LuaMethodDoc(
            overloads = {
                    @LuaMethodOverload(
                            argumentTypes = {LuaTable.class, LuaFunction.class},
                            argumentNames = {"tbl", "func"}
                    ),

            },
            value = "collection.filter"
    )
    public LuaTable filter(@LuaNotNil LuaTable tbl, LuaFunction func) {
        LuaTable result = new LuaTable();
        for (LuaValue key : tbl.keys()) {
            LuaValue value = tbl.get(key);
            if (func.call(value).toboolean()) {
                result.set(result.length() + 1, value);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        return "CollectionAPI";
    }
}
