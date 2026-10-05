package io.github.moosyu.items.tools.pickaxes;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.blocks.UnshatteredBlocks;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.items.UnshatteredRarity;
import io.github.moosyu.items.tools.UnshatteredMiningToolBase;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.Optional;
import java.util.Set;

public class TitaniumPickaxeBase extends UnshatteredMiningToolBase implements PassiveAbilityItem {
    private final Identifier abilityIdentifier;
    private final int fortuneBoost;

    public TitaniumPickaxeBase(Properties properties, String identifier, int fortuneBoost, int miningSpeed) {
        Identifier abilityId = UnshatteredUtils.getUnshatteredIdentifier(identifier + "_titanium_fanatic");

        super(properties.stacksTo(1)
                .attributes(ItemAttributeModifiers.builder()
                        .add(UnshatteredAttributeValues.DAMAGE.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(identifier + "_damage"), 7, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValues.MINING_SPEED.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(identifier + "_mining_speed"), miningSpeed, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValues.BREAKING_POWER.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(identifier + "_breaking_power"), 5, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(Attributes.ATTACK_SPEED,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(identifier + "_attack_speed"), -2.8, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).build()
                )
        );

        this.abilityIdentifier = abilityId;
        this.fortuneBoost = fortuneBoost;
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).flatMap(player -> UnshatteredUtils.getAttributeInstance(player,
                UnshatteredAttributeValues.MINING_FORTUNE.holder)
        ).ifPresent(attribute -> attribute.addTransientModifier(new AttributeModifier(abilityIdentifier, fortuneBoost, AttributeModifier.Operation.ADD_VALUE)));
    }

    @Override
    public void onAbilityFinished(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).flatMap(player -> UnshatteredUtils.getAttributeInstance(player,
                UnshatteredAttributeValues.MINING_FORTUNE.holder)
        ).ifPresent(attribute -> attribute.removeModifier(abilityIdentifier));
    }

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        Optional<ServerPlayer> player = context.get(AbilityContextKey.PLAYER);
        return player.map(serverPlayer -> UnshatteredUtils.getLookedAtBlock(serverPlayer, serverPlayer.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE))
                .map(blockHitResult -> serverPlayer.level().getBlockState(blockHitResult.getBlockPos()).is(UnshatteredBlocks.BREAKABLE_TITANIUM_BLOCK))
                .orElse(false)).orElse(false);
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_BREAK_MINING_BLOCK);
    }
}