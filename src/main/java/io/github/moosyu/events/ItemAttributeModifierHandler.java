package io.github.moosyu.events;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.util.UnshatteredUtils.getUnshatteredIdentifier;

@EventBusSubscriber(modid = MODID)
public class ItemAttributeModifierHandler {
    @SubscribeEvent
    public static void modifyAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack itemStack = event.getItemStack();

        if (itemStack.is(Items.WOODEN_PICKAXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(getUnshatteredIdentifier("wooden_pickaxe_damage"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(getUnshatteredIdentifier("wooden_pickaxe_mining_speed"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(getUnshatteredIdentifier("wooden_pickaxe_breaking_power"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.GOLDEN_PICKAXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(getUnshatteredIdentifier("golden_pickaxe_damage"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(getUnshatteredIdentifier("golden_pickaxe_mining_speed"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(getUnshatteredIdentifier("golden_pickaxe_breaking_power"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.STONE_PICKAXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(getUnshatteredIdentifier("stone_pickaxe_damage"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(getUnshatteredIdentifier("stone_pickaxe_mining_speed"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(getUnshatteredIdentifier("stone_pickaxe_breaking_power"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.IRON_PICKAXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(getUnshatteredIdentifier("iron_pickaxe_damage"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(getUnshatteredIdentifier("iron_pickaxe_mining_speed"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(getUnshatteredIdentifier("iron_pickaxe_breaking_power"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.DIAMOND_PICKAXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(getUnshatteredIdentifier("diamond_pickaxe_damage"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(getUnshatteredIdentifier("diamond_pickaxe_mining_speed"), 8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(getUnshatteredIdentifier("diamond_pickaxe_breaking_power"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.WOODEN_SWORD)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(getUnshatteredIdentifier("wooden_sword_damage"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.STONE_SWORD)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(getUnshatteredIdentifier("stone_sword_damage"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.IRON_SWORD)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(getUnshatteredIdentifier("iron_sword_damage"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.GOLDEN_SWORD)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(getUnshatteredIdentifier("golden_sword_damage"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.DIAMOND_SWORD)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(getUnshatteredIdentifier("diamond_sword_damage"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.WOODEN_AXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(getUnshatteredIdentifier("wooden_axe_damage"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.STONE_AXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(getUnshatteredIdentifier("stone_axe_damage"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.IRON_AXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(getUnshatteredIdentifier("iron_axe_damage"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.GOLDEN_AXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(getUnshatteredIdentifier("golden_axe_damage"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.DIAMOND_AXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(getUnshatteredIdentifier("diamond_axe_damage"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.IRON_HELMET)) {
            event.addModifier(UnshatteredAttributeValues.DEFENSE.holder, new AttributeModifier(getUnshatteredIdentifier("iron_helmet_defense"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.IRON_CHESTPLATE)) {
            event.addModifier(UnshatteredAttributeValues.DEFENSE.holder, new AttributeModifier(getUnshatteredIdentifier("iron_chestplate_defense"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.IRON_LEGGINGS)) {
            event.addModifier(UnshatteredAttributeValues.DEFENSE.holder, new AttributeModifier(getUnshatteredIdentifier("iron_leggings_defense"), 5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.IRON_BOOTS)) {
            event.addModifier(UnshatteredAttributeValues.DEFENSE.holder, new AttributeModifier(getUnshatteredIdentifier("iron_boots_defense"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.DIAMOND_HELMET)) {
            event.addModifier(UnshatteredAttributeValues.DEFENSE.holder, new AttributeModifier(getUnshatteredIdentifier("diamond_helmet_defense"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.DIAMOND_CHESTPLATE)) {
            event.addModifier(UnshatteredAttributeValues.DEFENSE.holder, new AttributeModifier(getUnshatteredIdentifier("diamond_chestplate_defense"), 8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.DIAMOND_LEGGINGS)) {
            event.addModifier(UnshatteredAttributeValues.DEFENSE.holder, new AttributeModifier(getUnshatteredIdentifier("diamond_leggings_defense"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.DIAMOND_BOOTS)) {
            event.addModifier(UnshatteredAttributeValues.DEFENSE.holder, new AttributeModifier(getUnshatteredIdentifier("diamond_boots_defense"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        }
    }
}
