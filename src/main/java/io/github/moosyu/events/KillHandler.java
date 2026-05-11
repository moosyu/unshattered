package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.experience.EntityCombatExperience;
import io.github.moosyu.experience.EntityFarmingExperience;
import io.github.moosyu.registers.AttachmentRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

public class KillHandler {
    @EventBusSubscriber(modid = "nno")
    public static class EventHandler {
        @SubscribeEvent
        public static void onEntityDeath(LivingDeathEvent event) {
            DamageSource source = event.getSource();
            Entity attacker = source.getEntity();

            if (attacker instanceof Player player) {
                if (player.level().isClientSide()) {
                    return;
                }

                PlayerSkillsAttachment skills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());

                float combatExp = EntityCombatExperience.getExp(event.getEntity().getType());
                if (combatExp > 0.0f) {
                    skills.addCombatExp(combatExp);
                    return;
                }

                float farmingExp = EntityFarmingExperience.getExp(event.getEntity().getType());
                if (farmingExp > 0.0f) {
                    skills.addFarmingExp(farmingExp);
                    return;
                }
            }
        }
    }
}
