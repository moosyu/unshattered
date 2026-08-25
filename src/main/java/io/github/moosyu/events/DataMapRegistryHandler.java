package io.github.moosyu.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.data.UnshatteredDataMaps.*;

@EventBusSubscriber(modid = MODID)
public class DataMapRegistryHandler {
    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event) {
        event.register(HARVESTABLE_BLOCKS_EXP_DATA);
        event.register(FISHABLE_ITEMS_EXP_DATA);
        event.register(FISHABLE_MOBS_EXP_DATA);
        event.register(COMBATABLE_MOBS_LOOT_DATA);
        event.register(BLOCK_BREAKING_POWER_DATA);
        event.register(BLOCK_REGEN_DATA);
        event.register(BREAKABLE_DROPS);
    }
}
