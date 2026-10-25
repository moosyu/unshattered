package io.github.moosyu.items.tools.rods;

import io.github.moosyu.abilities.*;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.util.*;

public class UnshatteredRod extends FishingRodItem implements PassiveAbilityItem {
    public static final Identifier ABILITY_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("fish_out_of_water");
    private static final Map<UUID, InteractionHand> CAST_HANDS = new HashMap<>();

    public UnshatteredRod(Properties properties) {
        super(properties);
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            player.sendSystemMessage(Component.translatable("combat.messages.unshattered.failed").withColor(UnshatteredUtils.RED));
        });
    }

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        return context.get(AbilityContextKey.TARGET).map(target ->
                !target.is(EntityTypeTags.AQUATIC)
        ).orElse(false);

    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_DEAL_DAMAGE);
    }

    @Override
    public Optional<AbilityTriggerResult> triggerResult() {
        return Optional.of(AbilityTriggerResult.CANCEL_EVENT);
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        if (!level.isClientSide()) {
            if (player.fishing != null) {
                CAST_HANDS.remove(player.getUUID());
            } else {
                CAST_HANDS.put(player.getUUID(), hand);
            }
        }
        return super.use(level, player, hand);
    }

    public static @Nullable InteractionHand getCastingHand(Player player) {
        UUID id = player.getUUID();

        if (player.fishing == null) {
            CAST_HANDS.remove(id);
            return null;
        }

        InteractionHand hand = CAST_HANDS.get(id);
        if (hand != null && player.getItemInHand(hand).getItem() instanceof UnshatteredRod) {
            return hand;
        }
        return null;
    }
}
