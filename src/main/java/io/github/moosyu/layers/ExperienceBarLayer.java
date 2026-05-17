package io.github.moosyu.layers;

import io.github.moosyu.helpers.ShadowTextHelper;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.util.ColorRGBA;

import static io.github.moosyu.helpers.ShadowTextHelper.drawShadowText;
import static io.github.moosyu.registers.RenderRegister.LARGE_BAR;

public class ExperienceBarLayer implements LayeredDraw.Layer {
    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        final int SPRITE_WIDTH = 181;
        final int SPRITE_HEIGHT = 15;
        final int POS_X_BAR = (graphics.guiWidth() / 2) - (SPRITE_WIDTH / 2);
        final int POS_Y_BAR = graphics.guiHeight() - SPRITE_HEIGHT - 17;

        graphics.enableScissor(POS_X_BAR, POS_Y_BAR, POS_X_BAR + SPRITE_WIDTH, POS_Y_BAR + 7);
        graphics.blit(LARGE_BAR, POS_X_BAR, POS_Y_BAR, 0, 0, SPRITE_WIDTH, SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
        graphics.disableScissor();

        drawShadowText(graphics, "0", (graphics.guiWidth() / 2), POS_Y_BAR, 0x7EFC20);
    }
}
