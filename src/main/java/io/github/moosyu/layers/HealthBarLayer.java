package io.github.moosyu.layers;

import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.registers.AttributesRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.DeltaTracker;
import net.minecraft.world.entity.player.Player;

import static io.github.moosyu.helpers.TextShadowHelper.drawShadowText;
import static io.github.moosyu.registers.AttachmentRegistry.PLAYER_STATE;
import static io.github.moosyu.registers.RenderRegister.SMALL_BAR;

public class HealthBarLayer implements LayeredDraw.Layer {
    @Override
    public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
        final int SPRITE_WIDTH = 73;
        final int SPRITE_HEIGHT = 15;
        final int BAR_HEIGHT = 8;
        final int POS_X_BAR = (graphics.guiWidth() / 2) - (SPRITE_WIDTH / 2) - 54;
        final int POS_Y_BAR = graphics.guiHeight() - SPRITE_HEIGHT - 24;

        Player player = Minecraft.getInstance().player;
        if (!player.level().isClientSide) return;
        final double currentHealth = player.getData(PLAYER_STATE.get()).getCurrentStat(PlayerStateAttachment.Stat.HEALTH);
        final double healthPercentage = (player.getData(PLAYER_STATE.get()).getCurrentStat(PlayerStateAttachment.Stat.HEALTH) / player.getAttributeValue(AttributesRegistry.HEALTH));

        graphics.setColor(1.0f, 0.01f, 0.043f, 1.0f);
        // -1 because the texture is bad sorry
        // (int) (POS_X_BAR - (SPRITE_WIDTH * (maxHealth / currentHealth)))
        graphics.blit(SMALL_BAR, POS_X_BAR, POS_Y_BAR, 0, BAR_HEIGHT - 1, SPRITE_WIDTH, BAR_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
        graphics.blit(SMALL_BAR, POS_X_BAR, POS_Y_BAR, 0, 0, (int)(SPRITE_WIDTH * healthPercentage), BAR_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT);
        graphics.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        drawShadowText(graphics, String.valueOf((int) currentHealth), POS_X_BAR + (SPRITE_WIDTH / 2), POS_Y_BAR, 0xFC5454);
    }
}
