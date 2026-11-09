package io.github.moosyu.items.armours;

import io.github.moosyu.abilities.*;
import io.github.moosyu.attributes.UnshatteredAttributeValue;
import io.github.moosyu.items.UnshatteredArmourMaterials;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.Equippable;

import java.util.List;
import java.util.function.Supplier;

public class SkeletonHat extends Item implements AbilityItem {
    public SkeletonHat(Properties properties) {
        super(properties.component(DataComponents.EQUIPPABLE,
                        Equippable.builder(EquipmentSlot.HEAD)
                                .setEquipSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.SKELETON_AMBIENT))
                                .setAsset(UnshatteredArmourMaterials.SKELETON_HAT_KEY)
                                .build()
                )
                .attributes(ItemAttributeModifiers.builder()
                        .add(Attributes.MOVEMENT_SPEED,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("skeleton_hat_speed"),
                                        0.01,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HEAD
                        ).add(UnshatteredAttributeValue.MANA.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("skeleton_hat_mana"),
                                        3,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HEAD
                        ).build()
                )
        );
    }

    @Override
    public List<? extends Supplier<? extends Ability>> abilities() {
        return List.of(UnshatteredAbilities.EXPLOSIVE_ARROWS);
    }
}
