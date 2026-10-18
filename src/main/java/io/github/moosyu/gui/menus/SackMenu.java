package io.github.moosyu.gui.menus;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class SackMenu extends AbstractContainerMenu {
    public static final int SLOTS_PER_ROW = 10;
    public static final int TOP_HEIGHT = 17;
    public static final int BORDER_HEIGHT = 1;
    public static final int BOTTOM_HEIGHT = 97;
    public final int sackSlots;

    public SackMenu(int containerId, Inventory inventory, RegistryFriendlyByteBuf buf) {
        this(containerId, inventory, ByteBufCodecs.INT.decode(buf));
    }

    public SackMenu(int containerId, Inventory inventory, int sackSlots) {
        super(UnshatteredMenus.SACK_MENU_TYPE.get(), containerId);

        this.sackSlots = sackSlots;

        addStandardInventorySlots(inventory, 8, getInventoryY(sackSlots));
    }

    public static int getMiddleHeight(int sackSlots) {
        return (Math.ceilDiv(sackSlots, SLOTS_PER_ROW) + 1) * 16 + BORDER_HEIGHT;
    }

    public static int getInventoryY(int sackSlots) {
        return TOP_HEIGHT + getMiddleHeight(sackSlots) + BORDER_HEIGHT + 15;
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return true;
    }
}
