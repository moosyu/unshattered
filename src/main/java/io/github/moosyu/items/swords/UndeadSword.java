package io.github.moosyu.items.swords;

import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.data.components.DataComponentRegistry;
import io.github.moosyu.data.components.ToolAbility;
import io.github.moosyu.items.UnshatteredSword;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

public class UndeadSword extends UnshatteredSword {
    public UndeadSword(Properties properties) {
        super(properties.attributes(ItemAttributeModifiers.builder().add(UnshatteredAttributes.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "undead_sword_damage"), 30, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND).build()).component(DataComponentRegistry.ITEM_ABILITY.get(), new ToolAbility("mind_blowing", 0, 0, 0, true)));
    }

    // this doesnt seem to be triggering at all for some reason. i wonder why?
    @Override
    public void hurtEnemy(@NonNull ItemStack itemStack, @NonNull LivingEntity mob, @NonNull LivingEntity attacker) {
        System.out.println("hurt triggered");
        if (mob.is(EntityTypeTags.UNDEAD)) {
            System.out.println("undead");
            //AttributeInstance finalDamageModifier = attacker.getAttribute(UnshatteredAttributes.FINAL_DAMAGE_MODIFIER.holder);
            //if (finalDamageModifier == null) return;
            //finalDamageModifier.addTransientModifier(new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "undead_sword_mind_blowing"), 1.0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));
        }
    }
}
