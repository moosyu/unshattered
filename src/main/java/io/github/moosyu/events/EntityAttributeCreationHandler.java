package io.github.moosyu.events;

import io.github.moosyu.entities.UnshatteredEntities;
import io.github.moosyu.entities.npcs.NPCEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class EntityAttributeCreationHandler {
    @SubscribeEvent
    public static void onEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(UnshatteredEntities.WOOL_WEAVER.get(), NPCEntity.createNpcAttributes().build());
        event.put(UnshatteredEntities.JOTRAELINE_GREATFORGE.get(), NPCEntity.createNpcAttributes().build());
        event.put(UnshatteredEntities.FORGER.get(), NPCEntity.createNpcAttributes().build());
    }
}
