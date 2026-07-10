package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.fishing.tables.WaterEntries;
import io.github.moosyu.attachments.UnshatteredAttachments;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemFishedEvent;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class ItemFishedHandler {
    @SubscribeEvent
    public static void onItemFished(ItemFishedEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide()) return;
        event.setCanceled(true);

        PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
        AttributeInstance fishingFortuneAttribute = player.getAttribute(UnshatteredAttributeValues.FISHING_FORTUNE.holder);
        Identifier selectedResultsTable;
        FishingHook hook = event.getHookEntity();
        double totalWeight;

        if (fishingFortuneAttribute == null) {
            return;
        }

        final int SELECTED_TABLE_RANGE = ThreadLocalRandom.current().nextInt(200);
        final double FISHING_FORTUNE = fishingFortuneAttribute.getValue();
        Map<?, Double> selectedMap;

        if (SELECTED_TABLE_RANGE < 160) {
            selectedMap = WaterEntries.filterEntries(WaterEntries.WATER_ITEM_WEIGHTS, player);
            totalWeight = WaterEntries.calculateTableWeight(selectedMap);
            double currentWeight = ThreadLocalRandom.current().nextDouble(0.0d, totalWeight);

        } else if (SELECTED_TABLE_RANGE < 194) {
            selectedMap = WaterEntries.filterEntries(WaterEntries.WATER_MOB_WEIGHTS, player);
            totalWeight = WaterEntries.calculateTableWeight(selectedMap);
            double currentWeight = ThreadLocalRandom.current().nextDouble(0.0d, totalWeight);

        } else {
            selectedMap = WaterEntries.filterEntries(WaterEntries.WATER_MISC_WEIGHTS, player);
            totalWeight = WaterEntries.calculateTableWeight(selectedMap);
            double currentWeight = ThreadLocalRandom.current().nextDouble(0.0d, totalWeight);

            for (Map.Entry<?, Double> entry : selectedMap.entrySet()) {
                currentWeight -= calculateAdjustedWeight(entry.getValue(), FISHING_FORTUNE);
                if (currentWeight <= 0) {
                    // do something
                    break;
                }
            }
        }



//        for (FishingResultsEntry entry : filteredFishingOptions) {
//            weightSelected -= calculateAdjustedWeight(entry.weight(), fishingFortuneAttribute.getValue());
//            if (weightSelected <= 0) {
//                selectedEntry = entry;
//                break;
//            }
//        }
//
//        if (selectedEntry == null) {
//            Unshattered.LOGGER.error("something went wrong! no entry has been selected!");
//            return;
//        }
//
//        skills.addExp(PlayerSkillsAttachment.Skill.FISHING, selectedEntry.exp(), player);
//        player.syncData(PLAYER_SKILLS);
//        PacketDistributor.sendToPlayer((ServerPlayer) player, new ExpSoundEffectPacket());
    }

    private static double calculateAdjustedWeight(double weight, double fishingFortune) {
        return Math.pow(weight, 1.0 - ((fishingFortune / UnshatteredAttributeValues.FISHING_FORTUNE.max) * 0.5));
    }
}
