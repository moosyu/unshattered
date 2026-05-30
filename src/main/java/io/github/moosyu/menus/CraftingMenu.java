package io.github.moosyu.menus;

import io.github.moosyu.registers.MenuTypeRegister;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CraftingMenu extends AbstractContainerMenu {
    private final Level level;

    public CraftingMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        super(MenuTypeRegister.CRAFTING_MENU.get(), containerId);
        this.level = inventory.player.level();
        BlockPos pos = extraData.readBlockPos();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
