package org.figuramc.exampleplugin.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.figuramc.figura.gui.screens.AbstractPanelScreen;
import org.figuramc.figura.gui.widgets.Button;
import org.figuramc.figura.gui.widgets.EntityPreview;
import org.figuramc.figura.lua.LuaWhitelist;
import org.figuramc.figura.utils.FiguraIdentifier;

@LuaWhitelist
public class ExampleScreen extends AbstractPanelScreen {
    public ExampleScreen(Screen parentScreen) {
        super(parentScreen, Component.translatable("examplefiguraplugin.gui.examplescreen"));
    }
    private Button exampleButton;
    @Override
    protected void init() {
        super.init();
        Minecraft minecraft = Minecraft.getInstance();
        int panels = Math.min(width / 3, 256) - 8;
        int middle = width / 2;
        int modelBgSize = Math.min(width - panels * 2 - 16, height - 96);
        int entitySize = 11 * modelBgSize / 29;
        int entityX = middle - modelBgSize / 2;
        int entityY = this.height / 2 - modelBgSize / 2;

        EntityPreview entity = new EntityPreview(entityX, entityY, modelBgSize, modelBgSize, entitySize, -15f, 30f, minecraft.player, this);
        addRenderableWidget(entity);

        int buttX = entity.getX() + entity.getWidth() / 2;
        int buttY = entity.getY() + entity.getHeight() + 4;

        addRenderableWidget(exampleButton = new Button(buttX - 48, buttY, 20, 20, 0, 0, 20, new FiguraIdentifier("textures/gui/play.png"), 60, 20, Component.translatable("examplefiguraplugin.gui.examplescreen.ok.tooltip"), button -> {
            onClose();
        }));
    }

    @Override
    public void onClose() {
        super.onClose();
    }
}
