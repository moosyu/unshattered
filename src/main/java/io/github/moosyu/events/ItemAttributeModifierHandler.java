package io.github.moosyu.events;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class ItemAttributeModifierHandler {
    @SubscribeEvent
    public static void modifyAttributeModifier(ItemAttributeModifierEvent event) {
        ItemStack itemStack = event.getItemStack();

        if (itemStack.is(Items.WOODEN_PICKAXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "wooden_pickaxe_damage"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "wooden_pickaxe_mining_speed"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "wooden_pickaxe_breaking_power"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.GOLDEN_PICKAXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "golden_pickaxe_damage"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "golden_pickaxe_mining_speed"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "golden_pickaxe_breaking_power"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.STONE_PICKAXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "stone_pickaxe_damage"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "stone_pickaxe_mining_speed"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "stone_pickaxe_breaking_power"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.IRON_PICKAXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "iron_pickaxe_damage"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "iron_pickaxe_mining_speed"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "iron_pickaxe_breaking_power"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.DIAMOND_PICKAXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "diamond_pickaxe_damage"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "diamond_pickaxe_mining_speed"), 8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "diamond_pickaxe_breaking_power"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.WOODEN_SWORD)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "wooden_sword_damage"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.STONE_SWORD)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "stone_sword_damage"), 5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.IRON_SWORD)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "iron_sword_damage"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.GOLDEN_SWORD)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "golden_sword_damage"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.DIAMOND_SWORD)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "diamond_sword_damage"), 7, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.WOODEN_AXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "wooden_axe_damage"), 6, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.STONE_AXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "stone_axe_damage"), 7, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.IRON_AXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "iron_axe_damage"), 8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.GOLDEN_AXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "golden_axe_damage"), 7, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (itemStack.is(Items.DIAMOND_AXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "diamond_axe_damage"), 10, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        }
    }
}
