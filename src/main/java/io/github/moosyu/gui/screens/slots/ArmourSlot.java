package io.github.moosyu.gui.screens.slots;

import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentEffectComponents;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.equipment.Equippable;
import org.jspecify.annotations.NonNull;

public class ArmourSlot extends Slot {
    private final LivingEntity owner;
    private final EquipmentSlot slot;
    private final UIElement icon;

    public ArmourSlot(Container container, int slotIndex, LivingEntity owner, EquipmentSlot slot, UIElement icon) {
        super(container, slotIndex, 0, 0);
        this.owner = owner;
        this.slot = slot;
        this.icon = icon;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        if (itemStack.canEquip(this.slot, this.owner)) {
            triggerArmourIconUpdate(false);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mayPickup(@NonNull Player player) {
        ItemStack itemStack = this.getItem();
        if ((itemStack.isEmpty() || player.isCreative() || !EnchantmentHelper.has(itemStack, EnchantmentEffectComponents.PREVENT_ARMOR_CHANGE)) && super.mayPickup(player)) {
            triggerArmourIconUpdate(true);
            return true;
        } else {
            return false;
        }
    }

    // todo: for whatever reason this doesn't fire when you shift click in, not a clue why
    // playArmourEquipSound seems to trigger and owner still seems to be the player...
    @Override
    public void setByPlayer(@NonNull ItemStack itemStack, @NonNull ItemStack previous) {
        super.setByPlayer(itemStack, previous);
        if (!itemStack.isEmpty()) {
            playArmourEquipSound(itemStack);
        }
    }

    private void triggerArmourIconUpdate(boolean visible) {
        icon.setVisible(visible);
    }

    private void playArmourEquipSound(ItemStack itemStack) {
        System.out.println(owner.getName().getString());
        if (owner instanceof Player player) {
            Equippable equipable = itemStack.get(DataComponents.EQUIPPABLE);
            UnshatteredUtils.playClientsideSound(player, equipable == null ? SoundEvents.ARMOR_EQUIP_GENERIC.value() : equipable.equipSound().value(), SoundSource.UI, 1.0f);
        }
    }
}
