package io.github.moosyu.items.tools.rods;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.components.ItemAttachments;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.data.datagen.UnshatteredEntityTagsProvider;
import io.github.moosyu.data.datagen.UnshatteredItemTagsProvider;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.util.*;

public class UnshatteredRod extends FishingRodItem implements PassiveAbilityItem {
    public static final Identifier ABILITY_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("fish_out_of_water");
    public static final Identifier ABILITY_IDENTIFIER_LAVA = UnshatteredUtils.getUnshatteredIdentifier("fish_out_of_lava");
    private static final Map<UUID, InteractionHand> CAST_HANDS = new HashMap<>();

    public UnshatteredRod(Properties properties) {
        super(properties.component(UnshatteredDataComponents.ITEM_ATTACHMENTS.get(),
                ItemAttachments.withSlots(ItemAttachments.SlotType.HOOK,
                        ItemAttachments.SlotType.LINE,
                        ItemAttachments.SlotType.SINKER
                )).stacksTo(1)
        );
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> player.sendSystemMessage(Component.translatable("combat.messages.unshattered.failed").withColor(UnshatteredUtils.RED)));
    }

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        LivingEntity target = context.get(AbilityContextKey.TARGET).orElse(null);
        ItemStack rod = context.get(AbilityContextKey.ITEM_STACK).orElse(null);

        if (rod == null || target == null) {
            return true;
        } else {
            if (rod.is(UnshatteredItemTagsProvider.CAN_FISH_IN_LAVA)) {
                return !target.is(UnshatteredEntityTagsProvider.MAGMATIC);
            } else {
                return !target.is(EntityTypeTags.AQUATIC);
            }
        }
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
