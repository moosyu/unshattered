package io.github.moosyu.items.armours;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.items.PassiveAbilityItem;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorType;
import org.jspecify.annotations.Nullable;

import static io.github.moosyu.items.UnshatteredArmourMaterials.LEAFLET_ARMOUR_MATERIAL;

public class SkeletonHat extends Item implements PassiveAbilityItem {
    public SkeletonHat(Properties properties) {
        super(properties.component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.HELMET)
                .humanoidArmor(LEAFLET_ARMOUR_MATERIAL, ArmorType.HELMET)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 8)
                .component(UnshatteredDataComponents.ABILITY.get(),
                        new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("skeleton_hat_explosive_arrows"),
                                0,
                                0,
                                0,
                                true
                        )
                )
                .attributes(ItemAttributeModifiers.builder()
                        .add(Attributes.MOVEMENT_SPEED,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("skeleton_hat_speed"),
                                        0.01,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HEAD
                        ).add(UnshatteredAttributeValues.MANA.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("skeleton_hat_mana"),
                                        3,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HEAD
                        ).build()
                )
        );
    }

    @Override
    public void onAbilityTriggered(ServerPlayer player, @Nullable LivingEntity target) {
        // trigger explosion
    }

    @Override
    public void onAbilityFinished(ServerPlayer player, @Nullable LivingEntity target) {}

    @Override
    public boolean abilityConditionsMet(ServerPlayer player, @Nullable LivingEntity target) {
        return true;
    }

    @Override
    public boolean isOngoing() {
        return false;
    }
}
