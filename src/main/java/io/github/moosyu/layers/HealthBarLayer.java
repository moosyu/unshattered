package io.github.moosyu.layers;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.DeltaTracker;

import static io.github.moosyu.helpers.ShadowTextHelper.drawShadowText;
import static io.github.moosyu.registers.RenderRegister.SMALL_BAR;

public class HealthBarLayer implements LayeredDraw.Layer {
    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        final int SPRITE_WIDTH = 73;
        final int SPRITE_HEIGHT = 15;
        final int POS_X_BAR = (graphics.guiWidth() / 2) - (SPRITE_WIDTH / 2) - 54;
        final int POS_Y_BAR = graphics.guiHeight() - SPRITE_HEIGHT - 24;

        graphics.enableScissor(POS_X_BAR, POS_Y_BAR, POS_X_BAR + 73, POS_Y_BAR + 7);
        graphics.blit(SMALL_BAR, POS_X_BAR, POS_Y_BAR, 0, 0, SPRITE_WIDTH, SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
        graphics.disableScissor();

        drawShadowText(graphics, "0", POS_X_BAR, POS_Y_BAR, 0xFC5454);
    }
}
