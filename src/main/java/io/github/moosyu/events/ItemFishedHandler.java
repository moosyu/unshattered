package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.packets.ExpSoundEffectPacket;
import io.github.moosyu.skills.experience.ItemsFishingExperience;
import io.github.moosyu.attachments.UnshatteredAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.attachments.UnshatteredAttachments.PLAYER_SKILLS;

@EventBusSubscriber(modid = MODID)
public class ItemFishedHandler {
    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;

        PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());

        for (ItemStack fishingItem : event.getDrops()) {
            skills.addExp(PlayerSkillsAttachment.Skill.FISHING, ItemsFishingExperience.getExp(fishingItem.getItem()), player);
            player.syncData(PLAYER_SKILLS);
            PacketDistributor.sendToPlayer((ServerPlayer) player, new ExpSoundEffectPacket());
        }
    }
}
