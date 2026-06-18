package io.github.moosyu.items.weapons.swords;

import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.data.components.DataComponentRegistry;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.items.UnshatteredPassiveAbilityItem;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import static io.github.moosyu.Unshattered.MODID;

public class FlamingSword extends UnshatteredSword implements UnshatteredPassiveAbilityItem {
    public FlamingSword(Properties properties) {
        super(properties.attributes(ItemAttributeModifiers.builder().add(UnshatteredAttributes.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "flaming_sword_damage"), 50, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).add(UnshatteredAttributes.STRENGTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "flaming_sword_strength"), 20, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()).component(DataComponentRegistry.ITEM_ABILITY.get(), new ItemAbility("ignition", 0, 0, 0, true)));
    }

    public void onAbilityTriggered(Player player, LivingEntity target) {
        System.out.println("ignition triggered");
        // todo: make custom fire damage so this works (and i can control how much a specific ignition may deal per damage tick)
        target.setRemainingFireTicks(60);
    }

    public boolean abilityConditionsMet(Player player, LivingEntity target) {
        return true;
    }
}
