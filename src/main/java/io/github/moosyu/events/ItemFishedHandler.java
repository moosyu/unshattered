package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.experience.ItemsFishingExperience;
import io.github.moosyu.registers.AttachmentRegistry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;

public class ItemFishedHandler {
    @EventBusSubscriber(modid = "nno")
    public static class EventHandler {
        @SubscribeEvent
        public static void onItemFished(ItemFishedEvent event) {
            Player player = event.getEntity();
            PlayerSkillsAttachment skills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());

            if (player.level().isClientSide()) {
                return;
            }

            for (ItemStack fishingItem : event.getDrops()) {
                skills.addFishingExp(ItemsFishingExperience.getExp(fishingItem.getItem()));
            }
        }
    }
}
