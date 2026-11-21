package io.github.moosyu.items.weapons.wands;

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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.Supplier;

public class FrostyArm extends Item implements AbilityItem {
    public FrostyArm(Properties properties) {
        super(properties
                .stacksTo(1)
                .attributes(ItemAttributeModifiers.builder()
                        .add(UnshatteredAttributeValue.MANA.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("frosty_arm_mana"), 20, AttributeModifier.Operation.ADD_VALUE),
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

        return UnshatteredAbilities.EVIL_SNOWMAN.get().tryActivate(serverPlayer, player.getItemInHand(hand))
                ? InteractionResult.PASS : InteractionResult.FAIL;
    }

    @Override
    public List<? extends Supplier<? extends Ability>> abilities() {
        return List.of(UnshatteredAbilities.EVIL_SNOWMAN);
    }
}
