package io.github.moosyu.helpers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class TextShadowHelper {
    public static void drawShadowText(GuiGraphics graphics, String text, float x, float y, int color) {
        final Font font = Minecraft.getInstance().font;
        int textOffset = (font.width(text) / 2) - 1;
        x -= textOffset;
        // shadow
        graphics.drawString(font, text, x - 1, y - 4, 0, false);
        graphics.drawString(font, text, x + 1, y - 4, 0, false);
        graphics.drawString(font, text, x, y - 5, 0, false);
        graphics.drawString(font, text, x, y - 3, 0, false);

        // unrelated but it took an actual real life hour to figure out there was a drop shadow property on by default
        graphics.drawString(font, text, x, y - 4, color, false);
    }
}
