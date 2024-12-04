package com.thekillerbunny.goofyplugin.mixin;

import net.minecraft.world.entity.Entity;
import org.figuramc.figura.lua.LuaWhitelist;
import org.figuramc.figura.lua.api.entity.EntityAPI;
import org.figuramc.figura.lua.docs.LuaMethodDoc;
import org.jetbrains.annotations.NotNull;
import org.luaj.vm2.LuaError;
import org.spongepowered.asm.mixin.*;

import java.util.UUID;

@Mixin(EntityAPI.class)
public class EntityAPIMixin {
    @Shadow @NotNull protected Entity entity;
    @Shadow @Mutable @Final @NotNull protected UUID entityUUID;

    @Unique
    @LuaWhitelist
    @LuaMethodDoc("goofy.entity_api.set_uuid")
    public void goofy_setUUID(String uuid) {
        try {
            UUID uuid1 = UUID.fromString(uuid);
            this.entity.setUUID(uuid1);
            this.entityUUID = uuid1;
        } catch (IllegalArgumentException err) {
            throw new LuaError("Invalid UUID");
        }
    }
}
