package io.github.moosyu.layers;

import io.github.moosyu.rarities.RarityTypes;
import io.github.moosyu.data.components.DataComponentRegistry;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.jspecify.annotations.NonNull;

import java.util.Set;

// currently if you have two different items that are exactly the same (same stack size and everything) the tooltip wont reload as ItemStack.matches will return true
// perhaps ill fix this but what are the odds this becomes a problem or anyone actually cares
public class SelectedItemLayer implements GuiLayer {
    private long displayUntilTick = 0;
    // the vanilla one doesnt seem to work properly for me unfortunately
    private ItemStack lastSelected = ItemStack.EMPTY;

    @Override
    public void render(@NonNull GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        final int DISPLAYED_TICK_COUNT = 80;
        final int FADING_TICK_COUNT = 20;
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || !player.level().isClientSide() || minecraft.options.hideGui) return;

        ItemStack currentItemStack = player.getMainHandItem();
        Set<DataComponentType<?>> ignoredComponents = Set.of(DataComponentRegistry.ITEM_CHARGES.get());
        Item heldItem = currentItemStack.getItem();
        boolean hasItem = heldItem != Items.AIR;
        Level level = player.level();

        if (!hasItem) {
            lastSelected = ItemStack.EMPTY;
            return;
        }
        // for when a new item has been selected. _ -> true just makes data components not affect it, probably is a better way but idc
        if (!ItemStack.matchesIgnoringComponents(currentItemStack, lastSelected, _ -> true)) {
            lastSelected = currentItemStack.copy();
            displayUntilTick = level.getGameTime() + DISPLAYED_TICK_COUNT;
            displaySelectedText(heldItem, graphics, minecraft, 1.0f);
            return;
        }
        // for current selected texts
        if (level.getGameTime() < displayUntilTick) {
            displaySelectedText(heldItem, graphics, minecraft, displayUntilTick - FADING_TICK_COUNT >= level.getGameTime() ? 1.0f : (float) (displayUntilTick - level.getGameTime()) / FADING_TICK_COUNT);
        }
    }

    private void displaySelectedText(Item heldItem, GuiGraphicsExtractor graphics, Minecraft minecraft, float opacity) {
        ItemStack heldItemStack = heldItem.getDefaultInstance();
        Component itemName = heldItem.getName(heldItemStack);
        RarityTypes rarity = heldItem.components().get(DataComponentRegistry.RARITY);
        int itemColor = rarity != null ? rarity.getColor(opacity) : RarityTypes.COMMON.getColor(1.0f);
        graphics.text(Minecraft.getInstance().font, itemName, (graphics.guiWidth() / 2) - (minecraft.font.width(itemName) / 2), graphics.guiHeight() - 50, itemColor, true);
    }
}