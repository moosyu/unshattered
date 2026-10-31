package io.github.moosyu.items.tools.pickaxes;

import io.github.moosyu.abilities.*;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.datagen.UnshatteredBlockTagsProvider;
import io.github.moosyu.items.tools.UnshatteredMiningToolBase;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public class MithriPickaxeBase extends UnshatteredMiningToolBase implements AbilityItem {
    private final Supplier<? extends Ability> ability;

    public MithriPickaxeBase(Properties properties, int miningSpeed, int miningFortune, String identifier, Supplier<? extends Ability> ability) {
        super(properties.stacksTo(1)
                .attributes(ItemAttributeModifiers.builder()
                        .add(UnshatteredAttributeValues.DAMAGE.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(identifier + "_damage"), 4, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValues.MINING_SPEED.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(identifier + "_mining_speed"), miningSpeed, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValues.MINING_FORTUNE.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(identifier + "_mining_fortune"), miningFortune, AttributeModifier.Operation.ADD_VALUE),
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

        this.ability = ability;
    }

    @Override
    public List<? extends Supplier<? extends Ability>> abilities() {
        return List.of(ability);
    }
}