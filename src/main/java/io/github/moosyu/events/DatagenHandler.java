package io.github.moosyu.events;

import io.github.moosyu.datagen.UnshatteredModelProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import static io.github.moosyu.Unshattered.MODID;

public class DatagenHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onGatherData(GatherDataEvent.Client event) {
            DataGenerator generator = event.getGenerator();
            PackOutput packOutput = generator.getPackOutput();

            generator.addProvider(true, new UnshatteredModelProvider(packOutput));
        }
    }
}
