package io.github.moosyu.events;

import io.github.moosyu.Unshattered;
import io.github.moosyu.abilities.*;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.drops.MobItemDropData;
import io.github.moosyu.data.drops.MobRewardData;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

import java.util.ArrayList;
import java.util.List;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.data.attachments.UnshatteredAttachments.PLAYER_SKILLS;

@EventBusSubscriber(modid = MODID)
public class EntityDeathHandler {
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        DamageSource source = event.getSource();
        Entity attacker = source.getEntity();

        if (attacker instanceof Player player) {
            if (player.level().isClientSide()) return;

            PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
            LivingEntity entity = event.getEntity();
            AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, (ServerPlayer) player).add(AbilityContextKey.TARGET, entity);
            List<PassiveAbilityItem> relevantPassiveItems = UnshatteredUtils.triggerInstantPassiveAbilities(player, AbilityTriggerType.ENTITY_DEATH, context);

            MobRewardData mobLoot = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entity.getType()).getData(UnshatteredDataMaps.COMBATABLE_MOBS_LOOT_DATA);
            if (mobLoot == null) {
                Unshattered.LOGGER.warn("entity {} is missing loot", entity.getPlainTextName());
                return;
            }

            boolean rolledAboveOccasional = false;
            // may end up doing it weighted instead of rolling them all at once, we'll see though
            for (MobItemDropData itemDrop : mobLoot.drops()) {
                rolledAboveOccasional = UnshatteredUtils.getNonGuaranteedDrop(itemDrop.dropData(), itemDrop.combatFortuneBoosted(), rolledAboveOccasional, player, UnshatteredAttributeValues.COMBAT_FORTUNE);
            }

            if (mobLoot.experience() > 0.0f) {
                skills.addExp(mobLoot.skill(), mobLoot.experience(), player);
                player.syncData(PLAYER_SKILLS);
            }

            relevantPassiveItems.forEach(item -> item.onAbilityFinished(context));
        }
    }
}
