package io.github.moosyu.items.tools;

import io.github.moosyu.abilities.AbilityItem;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

public class UnshatteredMiningToolBase extends Item {
    public UnshatteredMiningToolBase(Properties properties) {
        super(properties);
    }

    @Override
    public @NonNull InteractionResult use(Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        if (level.isClientSide()) return InteractionResult.FAIL;

        int abilityLength = 120;
        ItemStack itemStack = player.getItemInHand(hand);
        ItemCooldowns itemCooldowns = player.getCooldowns();
        PlayerAbilityEffectsAttachment abilities = player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get());
        if (itemCooldowns.isOnCooldown(itemStack)) {
            return InteractionResult.FAIL;
        } else {
            float cooldownModificationAmount = 1.0f;
            for (ItemStack abilityItemStack : abilities.getStoredNonOngoingItems()) {
                if (abilityItemStack.getItem() instanceof AbilityItem abilityItem
                        && abilityItem.triggerTypes().contains(AbilityTriggerType.PLAYER_USE_MINING_ABILITY)
                        && abilityItem.triggerResult().orElse(null) instanceof Float amount) {
                    cooldownModificationAmount += amount;
                }
            }

            itemCooldowns.addCooldown(itemStack, (int) (abilityLength * cooldownModificationAmount));
            return InteractionResult.PASS;
        }
    }
}
