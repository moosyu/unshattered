package io.github.moosyu.events;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class ItemAttributeModifierHandler {
    @SubscribeEvent
    public static void modifyAttributeModifier(ItemAttributeModifierEvent event) {
        if (event.getItemStack().is(Items.WOODEN_PICKAXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "wooden_pickaxe_damage"), 15, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "wooden_pickaxe_mining_speed"), 70, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "wooden_pickaxe_breaking_power"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (event.getItemStack().is(Items.GOLDEN_PICKAXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "golden_pickaxe_damage"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "golden_pickaxe_mining_speed"), 110, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "golden_pickaxe_breaking_power"), 1, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (event.getItemStack().is(Items.STONE_PICKAXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "stone_pickaxe_damage"), 15, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "stone_pickaxe_mining_speed"), 250, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "stone_pickaxe_breaking_power"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (event.getItemStack().is(Items.IRON_PICKAXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "iron_pickaxe_damage"), 25, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "iron_pickaxe_mining_speed"), 160, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "iron_pickaxe_breaking_power"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        } else if (event.getItemStack().is(Items.DIAMOND_PICKAXE)) {
            event.addModifier(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "diamond_pickaxe_damage"), 30, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.MINING_SPEED.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "diamond_pickaxe_mining_speed"), 220, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
            event.addModifier(UnshatteredAttributeValues.BREAKING_POWER.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "diamond_pickaxe_breaking_power"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND);
        }
    }
}
