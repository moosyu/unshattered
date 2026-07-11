package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.data.UnshatteredDataMaps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.Objects;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.attachments.UnshatteredAttachments.PLAYER_SKILLS;

@EventBusSubscriber(modid = MODID)
public class KillHandler {
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();

        if (attacker instanceof Player player) {
            if (player.level().isClientSide()) return;
            PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
            Entity entity = event.getEntity();

            float combatExp = Objects.requireNonNullElse(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entity.getType()).getData(UnshatteredDataMaps.COMBATABLE_MOBS_EXP_DATA), 0.0f);
            if (combatExp > 0.0f) {
                skills.addExp(PlayerSkillsAttachment.Skill.COMBAT, combatExp, player);
                player.syncData(PLAYER_SKILLS);
                return;
            }

            float farmingExp = Objects.requireNonNullElse(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entity.getType()).getData(UnshatteredDataMaps.FARMING_MOBS_EXP_DATA), 0.0f);
            System.out.println(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entity.getType()));
            System.out.println(farmingExp);
            if (farmingExp > 0.0f) {
                skills.addExp(PlayerSkillsAttachment.Skill.FARMING, farmingExp, player);
                player.syncData(PLAYER_SKILLS);
            }
        }
    }
}
