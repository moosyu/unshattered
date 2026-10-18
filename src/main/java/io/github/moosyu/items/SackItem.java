package io.github.moosyu.items;

import com.google.common.collect.ImmutableSet;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.gui.components.tabs.UnshatteredTabs;
import io.github.moosyu.gui.menus.SackMenu;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;

public class SackItem extends Item {
    public final ImmutableSet<Item> acceptedItems;
    public final int sackCapacity;

    /**
     * @param sackCapacity capacity per slot of the sack
     */
    public SackItem(Properties properties, ImmutableSet<Item> acceptedItems, int sackCapacity) {
        super(properties.stacksTo(1).component(UnshatteredDataComponents.SACK_CONTENTS.get(), new HashMap<>()));

        this.acceptedItems = acceptedItems;
        this.sackCapacity = sackCapacity;
    }

    @Override
    public boolean overrideOtherStackedOnMe(@NonNull ItemStack sack, @NonNull ItemStack other, @NonNull Slot slot, @NonNull ClickAction clickAction, @NonNull Player player, @NonNull SlotAccess carriedItem) {
        if (clickAction == ClickAction.SECONDARY) {
            if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                UnshatteredTabs.closeContainerKeepScreen();
                serverPlayer.openMenu(new SimpleMenuProvider(
                                (containerId, inventory, _) -> new SackMenu(containerId, inventory, acceptedItems.size()),
                                sack.getItemName()
                        ),
                        buf -> ByteBufCodecs.INT.encode(buf, acceptedItems.size())
                );
            }

            return true;
        }
        return false;
    }
}
