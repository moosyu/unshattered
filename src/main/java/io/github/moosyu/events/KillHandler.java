package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.packets.ExpSoundEffectPacket;
import io.github.moosyu.skills.experience.EntityCombatExperience;
import io.github.moosyu.skills.experience.EntityFarmingExperience;
import io.github.moosyu.attachments.AttachmentRegistry;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.network.PacketDistributor;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.attachments.AttachmentRegistry.PLAYER_SKILLS;

@EventBusSubscriber(modid = MODID)
public class KillHandler {
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();

        if (attacker instanceof Player player) {
            if (player.level().isClientSide()) return;
            PlayerSkillsAttachment skills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());

            float combatExp = EntityCombatExperience.getExp(event.getEntity().getType());
            if (combatExp > 0.0f) {
                skills.addExp(PlayerSkillsAttachment.Skill.COMBAT, combatExp, player);
                player.syncData(PLAYER_SKILLS);
                PacketDistributor.sendToPlayer((ServerPlayer) player, new ExpSoundEffectPacket());
                return;
            }

            float farmingExp = EntityFarmingExperience.getExp(event.getEntity().getType());
            if (farmingExp > 0.0f) {
                skills.addExp(PlayerSkillsAttachment.Skill.FARMING, farmingExp, player);
                player.syncData(PLAYER_SKILLS);
                PacketDistributor.sendToPlayer((ServerPlayer) player, new ExpSoundEffectPacket());
                return;
            }
        }
    }
}
