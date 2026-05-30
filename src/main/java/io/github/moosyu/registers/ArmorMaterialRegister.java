package io.github.moosyu.registers;

import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Util;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;
import net.neoforged.neoforge.common.Tags;

import java.util.EnumMap;

public class ArmorMaterialRegister {
    public static final ArmorMaterial LEAFLET_ARMOR_MATERIAL = new ArmorMaterial(
1,
        // Determines the defense value of this armor material, depending on what armor piece it is.
        Util.make(new EnumMap<>(ArmorType.class), map -> {
            map.put(ArmorType.BOOTS, 0);
            map.put(ArmorType.LEGGINGS, 0);
            map.put(ArmorType.CHESTPLATE, 0);
            map.put(ArmorType.HELMET, 0);
            map.put(ArmorType.BODY, 0);
        }),
        // Determines the enchantability of the tier. This represents how good the enchantments on this armor will be.
        // Gold uses 25, we put copper slightly below that.
        20,
        // Determines the sound played when equipping this armor.
        // This is wrapped with a Holder.
        SoundEvents.ARMOR_EQUIP_GENERIC,
0.0f,
0.0f,
        Tags.Items.NUGGETS,
        ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath("unshattered", "leaflet"))
    );
}
