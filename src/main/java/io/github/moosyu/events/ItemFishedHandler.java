package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.experience.ItemsFishingExperience;
import io.github.moosyu.registers.AttachmentRegistry;
import io.github.moosyu.sounds.ModSounds;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;

import static io.github.moosyu.NNO.MODID;

public class ItemFishedHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onItemFished(ItemFishedEvent event) {
            Player player = event.getEntity();
            if (player.level().isClientSide()) return;

            PlayerSkillsAttachment skills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());

            for (ItemStack fishingItem : event.getDrops()) {
                skills.addExp(PlayerSkillsAttachment.Skill.FISHING, ItemsFishingExperience.getExp(fishingItem.getItem()));
                ModSounds.playerExperienceSound(player);
            }
        }
    }
}
