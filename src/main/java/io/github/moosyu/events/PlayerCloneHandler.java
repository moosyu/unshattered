package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.registers.AttachmentRegistry;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

// Makes it so you don't mysteriously lose all experience when you die/switch dimensions.
// I can definitely see this being volatile so a little note to self to check this is there are saving problems
public class PlayerCloneHandler {
    @EventBusSubscriber(modid = "nno")
    public static class EventHandler {
        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            PlayerSkillsAttachment oldSkills = event.getOriginal().getData(AttachmentRegistry.PLAYER_SKILLS.get());
            PlayerSkillsAttachment newSkills = event.getEntity().getData(AttachmentRegistry.PLAYER_SKILLS.get());
            newSkills.transferSkills(oldSkills);
        }
    }
}