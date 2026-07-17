package io.github.moosyu.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.MobSpawnEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class MobSpawnHandler {
    @SubscribeEvent
    public static void onMobSpawn(MobSpawnEvent.SpawnPlacementCheck event) {
        event.setResult(MobSpawnEvent.SpawnPlacementCheck.Result.FAIL);
    }
}
