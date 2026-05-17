package io.github.moosyu.layers;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.util.ColorRGBA;

import static io.github.moosyu.registers.RenderRegister.LARGE_BAR;

public class ExperienceBarLayer implements LayeredDraw.Layer {
    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        final int SPRITE_WIDTH = 181;
        final int SPRITE_HEIGHT = 13;
        final int POS_X = (graphics.guiWidth() / 2) - (SPRITE_WIDTH / 2);
        final int POS_Y = graphics.guiHeight() - SPRITE_HEIGHT - 17;
        final Font font = Minecraft.getInstance().font;

        graphics.enableScissor(POS_X, POS_Y, POS_X + SPRITE_WIDTH, POS_Y + 7);
        graphics.blit(LARGE_BAR, POS_X, POS_Y, 0, 0, SPRITE_WIDTH, SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
        graphics.disableScissor();
        graphics.drawString(font, "21", (graphics.guiWidth() / 2), POS_Y, 127, false);
    }
}
