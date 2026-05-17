package io.github.moosyu.layers;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.DeltaTracker;

import static io.github.moosyu.registers.RenderRegister.SMALL_BAR;

public class HealthBarLayer implements LayeredDraw.Layer {
    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        final int SPRITE_WIDTH = 73;
        final int SPRITE_HEIGHT = 13;
        final int POS_X = (graphics.guiWidth() / 2) - (SPRITE_WIDTH / 2) - 54;
        final int POS_Y = graphics.guiHeight() - SPRITE_HEIGHT - 24;

        graphics.enableScissor(POS_X, POS_Y, POS_X + 73, POS_Y + 7);
        graphics.blit(SMALL_BAR, POS_X, POS_Y, 0, 0, SPRITE_WIDTH, SPRITE_HEIGHT, 73, 13);
        graphics.disableScissor();
    }
}
