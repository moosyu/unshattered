package io.github.moosyu.items.tools.rods;

import io.github.moosyu.data.components.ItemAttachments;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.util.*;

public class UnshatteredFishingRod extends FishingRodItem {
    // to know which rod was used for tracking
    private static final Map<UUID, InteractionHand> CAST_HANDS = new HashMap<>();

    public UnshatteredFishingRod(Properties properties) {
        super(properties.component(UnshatteredDataComponents.ITEM_ATTACHMENTS.get(),
                ItemAttachments.withSlots(ItemAttachments.SlotType.HOOK,
                        ItemAttachments.SlotType.LINE,
                        ItemAttachments.SlotType.SINKER
                )).stacksTo(1)
        );
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

    /**
     * @return the itemstack the player is actively casting out.
     */
    public static @Nullable ItemStack getCastingItemStack(Player player) {
        UUID id = player.getUUID();

        if (player.fishing == null) {
            CAST_HANDS.remove(id);
            return null;
        }

        InteractionHand hand = CAST_HANDS.get(id);
        if (hand != null) {
            return player.getItemInHand(hand);
        }

        return null;
    }
}
