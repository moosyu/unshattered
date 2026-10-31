package io.github.moosyu.items.weapons.swords;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityItem;
import io.github.moosyu.abilities.ActiveAbility;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.Supplier;

public class ZombieSwordBase extends UnshatteredSword implements AbilityItem {
    private final Supplier<? extends ActiveAbility> ability;

    public ZombieSwordBase(Properties properties, String identifier, int damage, int strength, int mana, Supplier<? extends ActiveAbility> ability) {
        super(properties.attributes(attributes(UnshatteredUtils.convertToSnakeCase(identifier), damage, strength, mana)));
        this.ability = ability;
    }

    private static ItemAttributeModifiers attributes(String id, int damage, int strength, int mana) {
        return ItemAttributeModifiers.builder()
                .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(id + "_damage"), damage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(id + "_strength"), strength, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(UnshatteredAttributeValues.MANA.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(id + "_mana"), mana, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(id + "_attack_speed"), -2.4f, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build();
    }

    @Override
    public @NonNull InteractionResult use(Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        if (level.isClientSide() || !(player instanceof ServerPlayer sp)) return InteractionResult.FAIL;
        return ability.get().tryActivate(sp, player.getItemInHand(hand))
                ? InteractionResult.PASS : InteractionResult.FAIL;
    }

    @Override
    public boolean shouldCauseReequipAnimation(@NonNull ItemStack oldStack, @NonNull ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    @Override
    public List<? extends Supplier<? extends Ability>> abilities() {
        return List.of(ability);
    }
}