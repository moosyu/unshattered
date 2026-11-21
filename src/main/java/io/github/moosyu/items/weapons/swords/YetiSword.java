package io.github.moosyu.items.weapons.swords;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityItem;
import io.github.moosyu.abilities.UnshatteredAbilities;
import io.github.moosyu.attributes.UnshatteredAttributeValue;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.Supplier;

public class YetiSword extends Item implements AbilityItem {
    public YetiSword(Properties properties) {
        super(properties.stacksTo(1)
                .attributes(ItemAttributeModifiers.builder()
                        .add(UnshatteredAttributeValue.DAMAGE.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("yeti_sword_damage"), 36, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValue.STRENGTH.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("yeti_sword_strength"), 40, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValue.MANA.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("yeti_sword_mana"), 30, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(Attributes.ATTACK_SPEED,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("yeti_sword_attack_speed"), -2.4f, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).build()
                )
        );
    }

    @Override
    public @NonNull InteractionResult use(Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        if (level.isClientSide() || !(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResult.FAIL;
        }

        return UnshatteredAbilities.TERRAIN_TOSS.get().tryActivate(serverPlayer, player.getItemInHand(hand))
                ? InteractionResult.PASS : InteractionResult.FAIL;
    }

    @Override
    public List<? extends Supplier<? extends Ability>> abilities() {
        return List.of(UnshatteredAbilities.TERRAIN_TOSS);
    }
}
