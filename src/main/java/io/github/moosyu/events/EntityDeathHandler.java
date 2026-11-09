package io.github.moosyu.events;

import io.github.moosyu.Unshattered;
import io.github.moosyu.abilities.*;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.attributes.UnshatteredAttributeValue;
import io.github.moosyu.data.drops.MobItemDropData;
import io.github.moosyu.data.UnshatteredDataMaps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;

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
            if (player.level().isClientSide() || !(player instanceof ServerPlayer serverPlayer)) {
                return;
            }

            LivingEntity entity = event.getEntity();
            AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, serverPlayer).add(AbilityContextKey.TARGET, entity);

            try (AbilityScope _ = AbilityScope.open(serverPlayer, AbilityTriggerType.ENTITY_DEATH, context)) {
                List<MobItemDropData> mobLoot = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(entity.getType()).getData(UnshatteredDataMaps.COMBATABLE_MOBS_LOOT_DATA);
                if (mobLoot == null) {
                    Unshattered.LOGGER.warn("entity {} is missing loot", entity.getPlainTextName());
                    return;
                }

                boolean rolledAboveOccasional = false;
                // may end up doing it weighted instead of rolling them all at once, we'll see though
                for (MobItemDropData itemDrop : mobLoot) {
                    rolledAboveOccasional = itemDrop.dropData().getNonGuaranteedDrop(rolledAboveOccasional, serverPlayer, itemDrop.combatFortuneBoosted() ? UnshatteredAttributeValue.COMBAT_FORTUNE : null);
                }
            }
        }
    }
}
