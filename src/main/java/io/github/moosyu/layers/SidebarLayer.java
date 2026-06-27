package io.github.moosyu.layers;

import io.github.moosyu.attachments.PlayerCurrencyAttachment;
import io.github.moosyu.attachments.UnshatteredAttachments;
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
        final String TITLE = "UNSHATTERED";
        final int SIDEBAR_HEIGHT = 96;
        final int HORIZONTAL_MARGIN = 3;
        final int VERTICAL_MARGIN = 12;

        final PlayerCurrencyAttachment CURRENCY_ATTACHMENT = player.getData(UnshatteredAttachments.PLAYER_CURRENCY.get());
        final int PURSE_WIDTH = font.width("Purse: " + CURRENCY_ATTACHMENT.getCoins()) + 20;
        final int TITLE_WIDTH = font.width(Component.literal(TITLE).withStyle(ChatFormatting.BOLD));
        final int SIDEBAR_WIDTH = Math.max(PURSE_WIDTH, TITLE_WIDTH) + (HORIZONTAL_MARGIN * 4);
        final int CORNER_POS_X = (graphics.guiWidth() - SIDEBAR_WIDTH) - 2;
        final int CORNER_POS_Y = (graphics.guiHeight() - SIDEBAR_HEIGHT) / 2;

        graphics.fill(CORNER_POS_X, CORNER_POS_Y, CORNER_POS_X + SIDEBAR_WIDTH, CORNER_POS_Y + SIDEBAR_HEIGHT, 0x66000000);
        graphics.text(font, Component.literal(TITLE).withStyle(ChatFormatting.BOLD), CORNER_POS_X + ((SIDEBAR_WIDTH - TITLE_WIDTH) / 2), CORNER_POS_Y + 2, 0xFFFFFF55);
        graphics.text(font, "Purse: ", CORNER_POS_X + HORIZONTAL_MARGIN, CORNER_POS_Y + VERTICAL_MARGIN, 0xFFFFFFFF, false);
        graphics.text(font, String.format("%,d", CURRENCY_ATTACHMENT.getCoins()), CORNER_POS_X + font.width("Purse: ") + HORIZONTAL_MARGIN, CORNER_POS_Y + VERTICAL_MARGIN, 0xFFFFFF55, false);
    }
}
