package io.github.moosyu.layers;

import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.UnshatteredAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.registers.AttachmentRegistry.PLAYER_STATE;
import static io.github.moosyu.layers.UnshatteredGuiLayers.SMALL_BAR;

public class HealthBarLayer implements GuiLayer {
    @Override
    public void render(GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        final int SPRITE_WIDTH = 73;
        final int SPRITE_HEIGHT = 15;
        final int BAR_HEIGHT = 8;
        final int POS_X_BAR = (graphics.guiWidth() / 2) - (SPRITE_WIDTH / 2) - 54;
        final int POS_Y_BAR = graphics.guiHeight() - SPRITE_HEIGHT - 18;

        Player player = Minecraft.getInstance().player;
        if (!player.level().isClientSide()) return;
        final double currentHealth = player.getData(PLAYER_STATE.get()).getCurrentStat(PlayerStateAttachment.Stat.HEALTH);
        final double healthPercentage = (player.getData(PLAYER_STATE.get()).getCurrentStat(PlayerStateAttachment.Stat.HEALTH) / player.getAttributeValue(UnshatteredAttributes.HEALTH.holder));
        final String currentHealthText = String.valueOf((int) currentHealth);
        final Font font = Minecraft.getInstance().font;

        // -1 because the texture is bad sorry
        // (int) (POS_X_BAR - (SPRITE_WIDTH * (maxHealth / currentHealth)))
        graphics.blit(RenderPipelines.GUI_TEXTURED, SMALL_BAR, POS_X_BAR, POS_Y_BAR, 0, BAR_HEIGHT - 1, SPRITE_WIDTH, BAR_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT, 0xFFFF030B);
        graphics.blit(RenderPipelines.GUI_TEXTURED, SMALL_BAR, POS_X_BAR, POS_Y_BAR, 0, 0, (int)(SPRITE_WIDTH * healthPercentage), BAR_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT, 0xFFFF030B);

        graphics.text(font, currentHealthText, POS_X_BAR + (SPRITE_WIDTH / 2) - (font.width(currentHealthText) / 2), POS_Y_BAR - 8, 0xFFFC5454, true);
    }
}
