package io.github.moosyu.items.weapons.swords;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.items.UnshatteredPassiveAbilityItem;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import static io.github.moosyu.Unshattered.MODID;

public class UndeadSword extends UnshatteredSword implements UnshatteredPassiveAbilityItem {
    public UndeadSword(Properties properties) {
        super(properties
                .component(UnshatteredDataComponents.ITEM_ABILITY.get(), new ItemAbility("mind_blowing", 0, 0, 0, true))
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .attributes(ItemAttributeModifiers.builder()
                    .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "undead_sword_damage"), 30, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                    .build()
                )
        );
    }

    public void onAbilityTriggered(Player player, LivingEntity target) {
        AttributeInstance finalDamageModifier = player.getAttribute(UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder);
        if (finalDamageModifier == null || finalDamageModifier.getValue() == 0.0d) return;
        finalDamageModifier.setBaseValue(finalDamageModifier.getValue() + 1);
    }

    public boolean abilityConditionsMet(Player player, LivingEntity target) {
        return target.is(EntityTypeTags.UNDEAD);
    }
}