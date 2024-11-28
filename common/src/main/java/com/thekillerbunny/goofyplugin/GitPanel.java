package com.thekillerbunny.goofyplugin;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.figuramc.figura.gui.screens.AbstractPanelScreen;

public class GitPanel extends AbstractPanelScreen {
    public GitPanel(Screen parent) {
        super(parent, Component.translatable("goofyplugin.gui.panels.title.history"));
    }
}
