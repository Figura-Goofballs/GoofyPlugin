package com.thekillerbunny.goofyplugin.ducks;

import org.figuramc.figura.avatar.Avatar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Queue;

@Mixin(value = Avatar.class, remap = false)
public interface AvatarAccessor {
    @Accessor
    Queue<Runnable> getEvents();
}
