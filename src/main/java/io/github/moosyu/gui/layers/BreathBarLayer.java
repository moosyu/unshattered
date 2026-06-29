package io.github.moosyu.gui.layers;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.jspecify.annotations.NonNull;

public class BreathBarLayer implements GuiLayer {
    @Override
    public void render(GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        final int SPRITE_WIDTH = 73;
        final int SPRITE_HEIGHT = 15;
        final int BAR_HEIGHT = 8;
        final int POS_X_BAR = (graphics.guiWidth() / 2) - (SPRITE_WIDTH / 2) + 130;
        final int POS_Y_BAR = (graphics.guiHeight() - SPRITE_HEIGHT) + 5;
        final Identifier SMALL_BAR = Identifier.fromNamespaceAndPath("unshattered", "textures/gui/sprites/small_bar.png");

        Minecraft minecraft = Minecraft.getInstance();
        Player player = Minecraft.getInstance().player;
        if (!player.level().isClientSide() || minecraft.options.hideGui || player.gameMode() != GameType.SURVIVAL) return;
        final float remainingAir = player.getAirSupply();
        // divided by 3 because 100 is nicer than 300
        final float remainingRespiration = Math.max(remainingAir / 3, 0);
        if (player.getAirSupply() == player.getMaxAirSupply()) return;
        final String currentAirText = String.valueOf((int) remainingRespiration);
        final Font font = Minecraft.getInstance().font;

        graphics.blit(RenderPipelines.GUI_TEXTURED, SMALL_BAR, POS_X_BAR, POS_Y_BAR, 0, BAR_HEIGHT - 1, SPRITE_WIDTH, BAR_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT, 0xFF56B8FF);
        graphics.blit(RenderPipelines.GUI_TEXTURED, SMALL_BAR, POS_X_BAR, POS_Y_BAR, 0, 0, (int) (SPRITE_WIDTH * (remainingAir / player.getMaxAirSupply())), BAR_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT, 0xFF56B8FF);

        graphics.text(Minecraft.getInstance().font, currentAirText, POS_X_BAR + (SPRITE_WIDTH / 2) - (font.width(currentAirText) / 2), POS_Y_BAR - 8, 0xFF56B8FF, true);
    }
}
