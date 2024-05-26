package com.thekillerbunny.goofyplugin.lua;

import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.lua.LuaNotNil;
import org.figuramc.figura.lua.LuaWhitelist;
import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import net.minecraft.nbt.*;

@LuaWhitelist
public class NbtAPI {
	public final Avatar owner;
	@LuaWhitelist
	public Tag tag;
	public NbtAPI(Avatar owner, Tag tag) {
		this.owner = owner;
		this.tag = tag;
	}
	public LuaValue mkLua(Object o) {
		return owner.luaRuntime.typeManager.javaToLua(o).arg1();
	}
	public LuaValue read() {
		return switch (tag) {
			case CompoundTag      t -> {
				final var tbl = new LuaTable(0, t.size());
				for (String key: t.getAllKeys())
					tbl.set(key, mkLua(new NbtAPI(owner, t.get(key))));
				yield tbl;
			}
			case CollectionTag<?> t ->
				new LuaTable(
					new LuaValue[0],
					(LuaValue[]) t.stream()
					              .map(this::mkLua)
					              .toArray(),
					LuaValue.NONE
				);
			case NumericTag       t -> mkLua(t.getAsNumber());
			case StringTag        t -> mkLua(t.getAsString());
			default                 -> throw new IllegalStateException("Impossible tag type");
		};
	}
}
