package io.github.moosyu.gui.menus;

import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.items.enchantments.UnshatteredEnchantmentEffects;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ItemCombinerMenuSlotDefinition;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.*;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;

public class ReforgeAnvilMenu extends ItemCombinerMenu {
    private record CurrentEnchantment(UnshatteredEnchantmentEffects.UnshatteredSimpleEffect currentEnchantment, int level) {}

    private @Nullable CurrentEnchantment currentEnchantment;

    public ReforgeAnvilMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, ContainerLevelAccess.NULL);
    }

    public ReforgeAnvilMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(UnshatteredMenus.REFORGE_ANVIL_MENU_TYPE.get(),
                containerId,
                inventory,
                access,
                ItemCombinerMenuSlotDefinition.create()
                        .withSlot(0, 27, 36, (_) -> true)
                        .withSlot(1, 76, 36, (_) -> true)
                        .withResultSlot(2, 134, 36)
                        .build()
        );

        this.currentEnchantment = null;

        addStandardInventorySlots(inventory, 8, 84);
        addInventoryHotbarSlots(inventory, 8, 142);
    }

    @Override
    protected void onTake(@NonNull Player player, @NonNull ItemStack itemStack) {
        if (currentEnchantment != null && !player.level().isClientSide() && player instanceof ServerPlayer) {
            player.getData(UnshatteredAttachments.PLAYER_SKILLS.get()).addExp(PlayerSkillsAttachment.Skill.CARPENTRY, currentEnchantment.currentEnchantment().getCarpentryExperience(currentEnchantment.level), player);
            player.syncData(UnshatteredAttachments.PLAYER_SKILLS.get());
        }

        inputSlots.setItem(0, ItemStack.EMPTY);
        inputSlots.setItem(1, ItemStack.EMPTY);

        currentEnchantment = null;
    }

    @Override
    protected boolean isValidBlock(@NonNull BlockState blockState) {
        return false;
    }

    @Override
    public void createResult() {
        ItemStack leftInput = inputSlots.getItem(0);
        ItemStack rightInput = inputSlots.getItem(1);

        boolean leftIsBook = leftInput.is(Items.ENCHANTED_BOOK);
        boolean rightIsBook = rightInput.is(Items.ENCHANTED_BOOK);

        if (leftIsBook && rightIsBook) {
            ItemEnchantments leftEnchants = EnchantmentHelper.getEnchantmentsForCrafting(leftInput);
            ItemEnchantments rightEnchants = EnchantmentHelper.getEnchantmentsForCrafting(rightInput);

            if (leftEnchants.size() == 1 && rightEnchants.size() == 1) {
                Holder<Enchantment> leftEnchant = leftEnchants.keySet().iterator().next();
                Holder<Enchantment> rightEnchant = rightEnchants.keySet().iterator().next();

                if (leftEnchant.equals(rightEnchant)) {
                    int leftLevel = leftEnchants.getLevel(leftEnchant);
                    int rightLevel = rightEnchants.getLevel(rightEnchant);

                    if (leftLevel == rightLevel) {
                        resultSlots.setItem(2, EnchantmentHelper.createBook(new EnchantmentInstance(leftEnchant, Math.min(leftLevel + 1, leftEnchant.value().getMaxLevel()))));
                        return;
                    }
                }
            }
        } else if (leftIsBook != rightIsBook) {
            ItemStack item = leftIsBook ? rightInput : leftInput;
            ItemStack book = leftIsBook ? leftInput : rightInput;

            if (item.isEmpty()) {
                currentEnchantment = null;
            } else {
                ItemEnchantments bookEnchants = EnchantmentHelper.getEnchantmentsForCrafting(book);

                if (bookEnchants.size() == 1) {
                    Holder<Enchantment> bookEnchant = bookEnchants.keySet().iterator().next();

                    if (!item.supportsEnchantment(bookEnchant)) return;

                    ItemEnchantments itemEnchants = EnchantmentHelper.getEnchantmentsForCrafting(item);
                    int bookLevel = bookEnchants.getLevel(bookEnchant);
                    int itemLevel = itemEnchants.getLevel(bookEnchant);
                    int resultLevel;

                    if (itemLevel == bookLevel) {
                        resultLevel = Math.min(itemLevel + 1, bookEnchant.value().getMaxLevel());
                    } else if (itemLevel < bookLevel) {
                        resultLevel = bookLevel;
                    } else {
                        return;
                    }

                    ItemStack result = item.copy();
                    EnchantmentHelper.updateEnchantments(result, mutable -> mutable.set(bookEnchant, resultLevel));
                    resultSlots.setItem(2, result);

                    UnshatteredEnchantmentEffects.getEffect(bookEnchant.getKey()).ifPresent(unshatteredEffect -> this.currentEnchantment = new CurrentEnchantment(unshatteredEffect, bookLevel));
                    return;
                }
            }
        } else {
            currentEnchantment = null;
        }

        resultSlots.setItem(2, ItemStack.EMPTY);
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return true;
    }
}