package io.github.moosyu.items.armours;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;

import java.util.Set;

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
    public void onAbilityTriggered(AbilityContext context) {
        // TODO: INCLUDE PROPER DAMAGE LOGIC HERE
        context.get(AbilityContextKey.TARGET)
                .ifPresent(target -> context.get(AbilityContextKey.PLAYER)
                        .ifPresent(player -> target.level().explode(player, target.getX(), target.getY(), target.getZ(), 8f, Level.ExplosionInteraction.NONE))
                );
    }

    @Override
    public void onAbilityFinished(AbilityContext context) {}

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        return context.get(AbilityContextKey.ITEM_TYPE).map(itemType -> itemType == ItemTypes.BOW || itemType == ItemTypes.SHORTBOW).orElse(false);
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_DEAL_DAMAGE);
    }
}
