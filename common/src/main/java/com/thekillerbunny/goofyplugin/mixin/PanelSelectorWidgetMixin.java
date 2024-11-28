package com.thekillerbunny.goofyplugin.mixin;

import com.mojang.datafixers.util.Pair;
import com.thekillerbunny.goofyplugin.GitPanel;
import net.minecraft.client.gui.screens.Screen;
import org.figuramc.figura.gui.screens.WardrobeScreen;
import org.figuramc.figura.gui.widgets.PanelSelectorWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Function;

@Mixin(value = PanelSelectorWidget.class, remap = false)
public abstract class PanelSelectorWidgetMixin {
    @Shadow @Final private static List<Function<Screen, Pair<Screen, PanelSelectorWidget.PanelIcon>>> PANELS;

    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void clinit(CallbackInfo ci) {
        // TODO: don't hardcode
        PANELS.add(3, s -> Pair.of(new GitPanel(s), PanelSelectorWidget.PanelIcon.OTHER));
    }
}
