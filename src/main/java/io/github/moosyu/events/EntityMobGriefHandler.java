package io.github.moosyu.events;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityMobGriefingEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class EntityMobGriefHandler {
    @SubscribeEvent
    public static void onEntityMobGriefAttempt(EntityMobGriefingEvent event) {
        event.setCanGrief(false);
    }
}
