package com.thekillerbunny.goofyplugin.mixin;

import net.minecraft.network.chat.MutableComponent;
import org.figuramc.figura.avatar.Avatar;
import org.figuramc.figura.gui.widgets.permissions.PlayerStatusWidget;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;

@Mixin(PlayerStatusWidget.class)
public class PlayerStatusWidgetMixin {
    @Shadow @Mutable @Final private static List<Function<Avatar, MutableComponent>> HOVER_TEXT;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void init(CallbackInfo ci) {
        HOVER_TEXT = new ArrayList<>(HOVER_TEXT);
        var old = HOVER_TEXT.get(0);
        HOVER_TEXT.set(0, avatar -> old.apply(avatar).append("Avatar Hash\n• %8x".formatted(avatar.nbt.hashCode() & 0xFFFFFFFFL)));
    }
}
