package io.github.moosyu.gui.layers;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.data.components.ItemCharges;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.jspecify.annotations.NonNull;

public class ChargesLayer implements GuiLayer {
    @Override
    public void render(GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        final int SPRITE_SIZE = 8;
        Minecraft minecraft = Minecraft.getInstance();
        Player player = Minecraft.getInstance().player;
        ItemCharges itemCharges = player.getItemInHand(InteractionHand.MAIN_HAND).get(UnshatteredDataComponents.ITEM_CHARGES);
        if (!player.level().isClientSide() || itemCharges == null || minecraft.options.hideGui || player.gameMode() != GameType.SURVIVAL) return;
        for (int i = 0; i < itemCharges.maxCharges(); i++) {
            Identifier chargeTexture = itemCharges.maxCharges() - 1 - i < itemCharges.currentCharges() ? Identifier.fromNamespaceAndPath("unshattered", "textures/sprites/gui/charge_filled.png") : Identifier.fromNamespaceAndPath("unshattered", "textures/sprites/gui/charge_empty.png");
            graphics.blit(RenderPipelines.GUI_TEXTURED, chargeTexture, (graphics.guiWidth() / 2) - (SPRITE_SIZE / 2) - 95 - (i * 10), graphics.guiHeight() - SPRITE_SIZE - 25, 0, 0, SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE, SPRITE_SIZE);
        }
    }
}
