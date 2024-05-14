package com.thekillerbunny.goofyplugin;

import net.minecraft.client.gui.screens.Screen;
import com.thekillerbunny.goofyplugin.screens.ExampleScreen;
import org.figuramc.figura.entries.FiguraScreen;
import org.figuramc.figura.entries.annotations.FiguraScreenPlugin;
import org.figuramc.figura.gui.widgets.PanelSelectorWidget;

/**
 * Example Screen Plugin
 *  Annotation required for Forge to Locate and Load the Plugin
 *  Entrypoint in fabric.mod.json: figura_screen
 */
@FiguraScreenPlugin
public class ExampleScreensPlugin implements FiguraScreen {
    @Override
    public Screen getScreen(Screen parentScreen) {
        return new ExampleScreen(parentScreen);
    }

    @Override
    public PanelSelectorWidget.PanelIcon getPanelIcon() {
        return FiguraScreen.super.getPanelIcon();
    }
}
