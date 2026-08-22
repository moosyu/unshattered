package io.github.moosyu.items.weapons.swords;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.items.UnshatteredPassiveAbilityItem;
import io.github.moosyu.util.AbilityUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import static io.github.moosyu.Unshattered.MODID;

public class UndeadSword extends UnshatteredSword implements UnshatteredPassiveAbilityItem {
    private static final Identifier ABILITY_IDENTIFIER = Identifier.fromNamespaceAndPath(MODID, "mind_blowing");

    public UndeadSword(Properties properties) {
        super(properties
                .component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(ABILITY_IDENTIFIER, 0, 0, 0, true))
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "undead_sword_damage"), 30, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
                )
        );
    }

    public void onAbilityTriggered(Player player, LivingEntity target) {
        AbilityUtils.getAttributeInstance(player, UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder).ifPresent(attribute -> attribute.addTransientModifier(new AttributeModifier(ABILITY_IDENTIFIER, 1.0, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)));
    }

    @Override
    public void onAbilityFinished(Player player, LivingEntity target) {
        AbilityUtils.getAttributeInstance(player, UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder).ifPresent(attribute -> attribute.removeModifier(ABILITY_IDENTIFIER));
    }

    public boolean abilityConditionsMet(Player player, LivingEntity target) {
        return target != null  && target.is(EntityTypeTags.UNDEAD);
    }
}