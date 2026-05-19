package io.github.moosyu.layers;

import io.github.moosyu.attachments.PlayerStatsAttachment;
import io.github.moosyu.registers.AttributesRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.DeltaTracker;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;

import static io.github.moosyu.helpers.ShadowTextHelper.drawShadowText;
import static io.github.moosyu.registers.AttachmentRegistry.PLAYER_STATS;
import static io.github.moosyu.registers.RenderRegister.SMALL_BAR;

public class HealthBarLayer implements LayeredDraw.Layer {
    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        final int SPRITE_WIDTH = 73;
        final int SPRITE_HEIGHT = 15;
        final int POS_X_BAR = (graphics.guiWidth() / 2) - (SPRITE_WIDTH / 2) - 54;
        final int POS_Y_BAR = graphics.guiHeight() - SPRITE_HEIGHT - 24;
        final double currentHealth = Minecraft.getInstance().player.getData(PLAYER_STATS.get()).getCurrentStat(PlayerStatsAttachment.Stat.HEALTH);

        graphics.enableScissor(POS_X_BAR, POS_Y_BAR, POS_X_BAR + 73, POS_Y_BAR + 8);
        graphics.setColor(1.0f, 0.01f, 0.043f, 1.0f);
        graphics.blit(SMALL_BAR, POS_X_BAR, POS_Y_BAR, 0, 0, SPRITE_WIDTH, SPRITE_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        graphics.disableScissor();

        drawShadowText(graphics, String.valueOf((int) currentHealth), POS_X_BAR + (SPRITE_WIDTH / 2), POS_Y_BAR, 0xFC5454);
    }
}
