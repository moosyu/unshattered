package io.github.moosyu.items;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.equipment.EquipmentAsset;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredArmourMaterials {
    public record UnshatteredArmourMaterial(Holder<SoundEvent> equipSound, ResourceKey<EquipmentAsset> assetId) {}

    public static final ResourceKey<? extends Registry<EquipmentAsset>> ROOT_ID = ResourceKey.createRegistryKey(Identifier.withDefaultNamespace("equipment_asset"));

    public static ResourceKey<EquipmentAsset> LEAFLET_KEY = ResourceKey.create(ROOT_ID, Identifier.fromNamespaceAndPath(MODID, "leaflet"));
    public static ResourceKey<EquipmentAsset> GLOW_SQUID_BOOTS_KEY = ResourceKey.create(ROOT_ID, Identifier.fromNamespaceAndPath(MODID, "glow_squid_boots"));
    public static ResourceKey<EquipmentAsset> SKELETON_HAT_KEY = ResourceKey.create(UnshatteredArmourMaterials.ROOT_ID, UnshatteredUtils.getUnshatteredIdentifier("skeleton_hat"));
    public static ResourceKey<EquipmentAsset> LAPIS_ARMOUR_KEY = ResourceKey.create(UnshatteredArmourMaterials.ROOT_ID, UnshatteredUtils.getUnshatteredIdentifier("lapis_armour"));
    public static ResourceKey<EquipmentAsset> MINER_ARMOUR_KEY = ResourceKey.create(UnshatteredArmourMaterials.ROOT_ID, UnshatteredUtils.getUnshatteredIdentifier("miner_armour"));

    public static UnshatteredArmourMaterial LEAFLET_MATERIAL = new UnshatteredArmourMaterial(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.AZALEA_LEAVES_PLACE), LEAFLET_KEY);
    public static UnshatteredArmourMaterial GLOW_SQUID_BOOTS_MATERIAL = new UnshatteredArmourMaterial(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.GLOW_SQUID_SQUIRT), GLOW_SQUID_BOOTS_KEY);
    public static UnshatteredArmourMaterial LAPIS_ARMOUR_MATERIAL = new UnshatteredArmourMaterial(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ARMOR_EQUIP_GENERIC.value()), LAPIS_ARMOUR_KEY);
    public static UnshatteredArmourMaterial MINER_ARMOUR_MATERIAL = new UnshatteredArmourMaterial(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ARMOR_EQUIP_LEATHER.value()), MINER_ARMOUR_KEY);
}
