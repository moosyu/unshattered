package io.github.moosyu.layers;

import io.github.moosyu.attachments.AttachmentRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.jspecify.annotations.NonNull;

public class SidebarLayer implements GuiLayer {
    @Override
    public void render(@NonNull GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;
        Player player = minecraft.player;
        if (player == null || !player.level().isClientSide() || minecraft.options.hideGui) return;
        final int SCREEN_WIDTH = (int) (graphics.guiWidth() * 0.2);
        final int SCREEN_HEIGHT = (int) (graphics.guiHeight() * 0.4);

        final int CORNER_POS_X = (graphics.guiWidth() - SCREEN_WIDTH) - 2;
        final int CORNER_POS_Y = (graphics.guiHeight() - SCREEN_HEIGHT) / 2;

        graphics.fill(CORNER_POS_X, CORNER_POS_Y, CORNER_POS_X + SCREEN_WIDTH, CORNER_POS_Y + SCREEN_HEIGHT, 0x66000000);
        graphics.text(font, Component.literal("SKYBLOCK").withStyle(ChatFormatting.BOLD), CORNER_POS_X + ((SCREEN_WIDTH - font.width(Component.literal("SKYBLOCK").withStyle(ChatFormatting.BOLD))) / 2), CORNER_POS_Y + 2, 0xFFFFFF55);
        graphics.text(font, "Purse: ", CORNER_POS_X + 2, CORNER_POS_Y + 12, 0xFFFFFFFF, false);
        graphics.text(font, String.valueOf(player.getData(AttachmentRegistry.PLAYER_CURRENCY.get()).getCoins()), CORNER_POS_X + font.width("Purse: ") + 2, CORNER_POS_Y + 12, 0xFFFFFF55, false);
    }
}
