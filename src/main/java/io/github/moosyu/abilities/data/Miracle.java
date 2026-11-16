package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.items.UnshatteredItems;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class Miracle extends Ability {
    public Miracle() {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_FISH);
    }

    @Override
    public void onTrigger(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).ifPresent(player -> {
            int selectedNumber = player.getRandom().nextInt(0, 1000);

            if (selectedNumber < 50) {
                ItemStack itemStack;

                if (selectedNumber < 40) {
                    itemStack = new ItemStack(UnshatteredItems.WHITE_PRESENT.get());
                } else if (selectedNumber < 42) {
                    itemStack = new ItemStack(UnshatteredItems.GREEN_PRESENT.get());
                } else {
                    itemStack = new ItemStack(UnshatteredItems.RED_PRESENT.get());
                }

                UnshatteredUtils.addItemStackToInventory(player, itemStack);
                player.sendSystemMessage(Component.empty()
                        .append(Component.translatable("skills.messages.unshattered.fishing.lucky").withStyle(ChatFormatting.BOLD).withColor(UnshatteredUtils.RED))
                        .append(Component.literal(" "))
                        .append(Component.translatable("skills.messages.unshattered.fishing.you_found_a").withColor(UnshatteredUtils.CYAN))
                        .append(Component.literal(" "))
                        .append(itemStack.getItemName().copy().withColor(UnshatteredUtils.getItemRarity(itemStack).getColour()))
                );
            }
        });
    }
}
