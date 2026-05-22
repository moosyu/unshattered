package io.github.moosyu.layers;

import io.github.moosyu.registers.AttributesRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import static io.github.moosyu.helpers.TextShadowHelper.drawShadowText;
import static io.github.moosyu.registers.RenderRegister.SMALL_BAR;

public class ManaBarLayer implements LayeredDraw.Layer {
    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        final int SPRITE_WIDTH = 73;
        final int SPRITE_HEIGHT = 15;
        final int BAR_HEIGHT = 8;
        final int POS_X_BAR = (graphics.guiWidth() / 2) - (SPRITE_WIDTH / 2) + 54;
        final int POS_Y_BAR = graphics.guiHeight() - SPRITE_HEIGHT - 24;
        final AttributeInstance manaAttribute = Minecraft.getInstance().player.getAttribute(AttributesRegistry.MANA);

        graphics.setColor(0.0f, 0.653f, 1f, 1.0f);
        graphics.blit(SMALL_BAR, POS_X_BAR, POS_Y_BAR, 0, BAR_HEIGHT - 1, SPRITE_WIDTH, BAR_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
        graphics.blit(SMALL_BAR, POS_X_BAR, POS_Y_BAR, 0, 0, SPRITE_WIDTH, BAR_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        drawShadowText(graphics, manaAttribute != null ? String.valueOf((int) manaAttribute.getValue()) : "ERROR", POS_X_BAR + (SPRITE_WIDTH / 2), POS_Y_BAR, 0x4E5BC6);
    }
}
