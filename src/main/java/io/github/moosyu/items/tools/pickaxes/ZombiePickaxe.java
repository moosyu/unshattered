package io.github.moosyu.items.tools.pickaxes;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.items.tools.UnshatteredMiningToolBase;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.phys.AABB;

import java.util.Set;

public class ZombiePickaxe extends UnshatteredMiningToolBase implements PassiveAbilityItem {
    public static Identifier ABILITY_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("rotten");
    public static int DIAMETER = 10;

    public ZombiePickaxe(Properties properties) {
        super(properties.stacksTo(1)
                .component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemType.PICKAXE)
                .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarity.COMMON)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 3)
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("rotten"), 0, 0, 0, true))
                .attributes(ItemAttributeModifiers.builder()
                        .add(UnshatteredAttributeValues.DAMAGE.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("zombie_pickaxe_damage"), 5, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValues.MINING_SPEED.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("zombie_pickaxe_mining_speed"), 3, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValues.BREAKING_POWER.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("zombie_pickaxe_breaking_power"), 3, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(Attributes.ATTACK_SPEED,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("zombie_pickaxe_attack_speed"), -2.8, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).build()
                )
        );
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            AABB boundingBox = AABB.ofSize(player.position(), DIAMETER + 1, DIAMETER + 1, DIAMETER + 1);

            int fortuneBoost = player.level().getEntities(null, boundingBox).stream().filter(entity -> entity.is(EntityTypeTags.UNDEAD)).mapToInt(_ -> 5).sum();
            if (fortuneBoost > 0) {
                UnshatteredUtils.getAttributeInstance(player, UnshatteredAttributeValues.MINING_FORTUNE.holder).ifPresent(attribute ->
                        attribute.addTransientModifier(new AttributeModifier(ABILITY_IDENTIFIER, fortuneBoost, AttributeModifier.Operation.ADD_VALUE))
                );
            }
        });
    }

    @Override
    public void onAbilityFinished(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).flatMap(player -> UnshatteredUtils.getAttributeInstance(player,
                UnshatteredAttributeValues.MINING_FORTUNE.holder)
        ).ifPresent(attribute -> attribute.removeModifier(ABILITY_IDENTIFIER));
    }

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        return true;
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_BREAK_MINING_BLOCK);
    }
}
