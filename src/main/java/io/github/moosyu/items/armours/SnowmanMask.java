package io.github.moosyu.items.armours;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityItem;
import io.github.moosyu.abilities.UnshatteredAbilities;
import io.github.moosyu.attributes.UnshatteredAttributeValue;
import io.github.moosyu.items.UnshatteredArmourMaterials;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.Equippable;

import java.util.List;
import java.util.function.Supplier;

public class SnowmanMask extends Item implements AbilityItem {
    public SnowmanMask(Properties properties) {
        super(properties.component(DataComponents.EQUIPPABLE,
                Equippable.builder(EquipmentSlot.HEAD)
                        .setEquipSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.ARMOR_EQUIP_LEATHER.value()))
                        .setAsset(UnshatteredArmourMaterials.SNOWMAN_MASK_KEY)
                        .build()
        ) .attributes(ItemAttributeModifiers.builder()
                .add(UnshatteredAttributeValue.MAX_HEALTH.holder,
                        new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("snowman_mask_helmet_health"),
                                10,
                                AttributeModifier.Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD
                ).build()
        ));
    }

    @Override
    public List<? extends Supplier<? extends Ability>> abilities() {
        return List.of(UnshatteredAbilities.FEELIN_FESTIVE_SNOWMAN_MASK);
    }
}
