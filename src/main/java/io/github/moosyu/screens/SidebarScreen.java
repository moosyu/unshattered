package io.github.moosyu.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

public class SidebarScreen extends Screen {
    public SidebarScreen() {
        super(Component.literal("Sidebar"));
    }

    private final int SCREEN_WIDTH = 200;
    private final int SCREEN_HEIGHT = 300;

    @Override
    protected void init() {
        super.init();
    }
    
    // so it wont try to save and what not
    @Override
    public boolean isPauseScreen() {return false;}

    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        final int CORNER_POS_X = (this.width - SCREEN_WIDTH) / 2;
        final int CORNER_POS_Y = (this.height - SCREEN_HEIGHT) / 2;
        graphics.fill(CORNER_POS_X, CORNER_POS_Y - SCREEN_HEIGHT, CORNER_POS_X + SCREEN_WIDTH, CORNER_POS_Y, 0x66383838);
    }
}
