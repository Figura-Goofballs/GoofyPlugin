package com.thekillerbunny.goofyplugin;

import io.netty.util.concurrent.CompleteFuture;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.figuramc.figura.gui.screens.AbstractPanelScreen;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public class GitPanel extends AbstractPanelScreen {
    public GitPanel(Screen parent) {
        super(parent, Component.translatable("goofyplugin.gui.panels.title.history"));
    }

    @Override
    protected void init() {
        super.init();
    }
}
