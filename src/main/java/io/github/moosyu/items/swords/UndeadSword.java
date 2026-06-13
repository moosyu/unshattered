package io.github.moosyu.items.swords;

import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.data.components.DataComponentRegistry;
import io.github.moosyu.data.components.ToolAbility;
import io.github.moosyu.items.UnshatteredAbilitySword;
import io.github.moosyu.items.UnshatteredSword;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import static io.github.moosyu.Unshattered.MODID;

public class UndeadSword extends UnshatteredSword implements UnshatteredAbilitySword {
    public UndeadSword(Properties properties) {
        super(properties.attributes(ItemAttributeModifiers.builder().add(UnshatteredAttributes.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "undead_sword_damage"), 30, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()).component(DataComponentRegistry.ITEM_ABILITY.get(), new ToolAbility("mind_blowing", 0, 0, 0, true)));
    }

    public void onAbilityTriggered(Player player, LivingEntity target) {
        AttributeInstance finalDamageModifier = player.getAttribute(UnshatteredAttributes.FINAL_DAMAGE_MODIFIER.holder);
        if (finalDamageModifier == null) return;
        finalDamageModifier.setBaseValue(finalDamageModifier.getValue() + 1);
    }

    public boolean abilityConditionsMet(Player player, LivingEntity target) {
        return target.is(EntityTypeTags.UNDEAD);
    }
}