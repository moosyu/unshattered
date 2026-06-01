package io.github.moosyu.layers;

import io.github.moosyu.rarities.RarityTypes;
import io.github.moosyu.registers.DataComponentRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.jspecify.annotations.NonNull;

// todo: make this fade
public class SelectedItemLayer implements GuiLayer {
    private long displayUntilTick = 0;

    @Override
    public void render(@NonNull GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || !player.level().isClientSide()) return;

        Item heldItem = player.getMainHandItem().getItem();
        boolean hasItem = heldItem != Items.AIR;
        Level level = player.level();

        if (!hasItem) {
            return;
        }
        if (heldItem != minecraft.gui.lastToolHighlight.getItem()) {
            displayUntilTick = level.getGameTime() + 100;
            displaySelectedText(heldItem, graphics, minecraft);
            return;
        }
        if (level.getGameTime() < displayUntilTick) {
            displaySelectedText(heldItem, graphics, minecraft);
        }
    }

    private void displaySelectedText(Item heldItem, GuiGraphicsExtractor graphics, Minecraft minecraft) {
        ItemStack heldItemStack = heldItem.getDefaultInstance();
        Component itemName = heldItem.getName(heldItemStack);
        RarityTypes rarity = heldItem.components().get(DataComponentRegistry.RARITY);
        int itemColor = rarity != null ? rarity.getColor() : RarityTypes.COMMON.getColor();
        graphics.text(Minecraft.getInstance().font, itemName, (graphics.guiWidth() / 2) - (minecraft.font.width(itemName) / 2), graphics.guiHeight() - 50, itemColor, true);
    }
}