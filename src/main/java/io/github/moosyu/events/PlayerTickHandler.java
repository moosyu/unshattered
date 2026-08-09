package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerRegionAttachment;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.regions.Region;
import io.github.moosyu.data.regions.RegionAreas;
import io.github.moosyu.data.regions.RegionTemperatureTypes;
import io.github.moosyu.data.regions.TemperatureTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.attachments.UnshatteredAttachments.PLAYER_STATE;
import static io.github.moosyu.events.DataPackRegistryHandler.REGION_REGISTRY_KEY;

@EventBusSubscriber(modid = MODID)
public class PlayerTickHandler {
    static boolean fishApproaching = false;
    static boolean fishNibbling = false;
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (player.level().isClientSide()) return;
        PlayerStateAttachment state = player.getData(PLAYER_STATE.get());
        final double MAX_HEALTH_VALUE = player.getAttributeValue(UnshatteredAttributeValues.HEALTH.holder);
        final double HEALTH_REGEN_VALUE = player.getAttributeValue(UnshatteredAttributeValues.HEALTH_REGEN.holder);
        final double MAX_MANA_VALUE = player.getAttributeValue(UnshatteredAttributeValues.MANA.holder);
        final double MANA_REGEN_VALUE = player.getAttributeValue(UnshatteredAttributeValues.MANA_REGEN.holder);

        // disable hunger effects
        player.getFoodData().setFoodLevel(20);
        player.getFoodData().setSaturation(5.0f);
        //player.getFoodData().setExhaustion(0.0f);

        // incremental for every 2 seconds
        if (player.tickCount % 40 == 0) {
            double healthGained = (1.5 + MAX_HEALTH_VALUE / 100) * (HEALTH_REGEN_VALUE / 100);
            double manaGained = (MAX_MANA_VALUE * 0.04) * (MANA_REGEN_VALUE / 100);

            state.addCurrentStat(PlayerStateAttachment.Stat.HEALTH, healthGained, MAX_HEALTH_VALUE);
            player.syncData(PLAYER_STATE);
            state.addCurrentStat(PlayerStateAttachment.Stat.MANA, manaGained, MAX_MANA_VALUE);
            player.syncData(PLAYER_STATE);
        } else {
            // update health if attribute changed. itll already be updated on the healing tick though
            if (MAX_HEALTH_VALUE < state.getCurrentStat(PlayerStateAttachment.Stat.HEALTH)) {
                state.setCurrentStat(PlayerStateAttachment.Stat.HEALTH, MAX_HEALTH_VALUE);
            }
            player.syncData(PLAYER_STATE);
        }

        if (player.tickCount % 20 == 0) {
            // may become a problem later but probably doesn't need to be updated more than once a second
            RegionAreas.updatePlayerRegion(player);

            PlayerRegionAttachment regionAttachment = player.getData(UnshatteredAttachments.PLAYER_REGION.get());
            Region region = player.level().registryAccess().lookupOrThrow(REGION_REGISTRY_KEY).getValue(regionAttachment.regionKey());
            float temperature = player.getData(UnshatteredAttachments.PLAYER_TEMPERATURE.get());
            if (region == null) return;
            float temperatureChange =  region.temperatureType().getRegionTemperatureChange();
            float newTemperature = temperature + temperatureChange;

            if (newTemperature < TemperatureTypes.HIGH_TEMP.getValue()
                    && newTemperature > TemperatureTypes.LOW_TEMP.getValue()
                    || !region.temperatureType().isRegionSafe()
            ) {
                player.setData(UnshatteredAttachments.PLAYER_TEMPERATURE.get(), newTemperature);
            } else {
                // i dont think this works
                player.setData(UnshatteredAttachments.PLAYER_TEMPERATURE.get(),
                        temperature + Math.abs(temperature + 0.01f) < Math.abs(temperature - 0.01f) ? 0.01f : -0.01f
                );
            }
        }

        state.decrementInvulnerableTime();

        PlayerAbilityEffectsAttachment abilities = player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get());
        if (abilities.hasAnyActiveEffect()) {
            abilities.tickEffects(level, player);
        }
    }
}
