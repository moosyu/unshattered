package io.github.moosyu.events;

import io.github.moosyu.entities.UnshatteredEntities;
import io.github.moosyu.entities.renderers.GraveyardZombieVillagerRenderer;
import io.github.moosyu.entities.renderers.HomingBeeRenderer;
import io.github.moosyu.entities.renderers.NPCEntityRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class EntityRenderersHandler {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), GraveyardZombieVillagerRenderer::new);
        event.registerEntityRenderer(UnshatteredEntities.HOMING_BEE.get(), HomingBeeRenderer::new);
        event.registerEntityRenderer(UnshatteredEntities.JOTRAELINE_GREATFORGE.get(), context -> new NPCEntityRenderer(context, "jotraeline_greatforge"));
        event.registerEntityRenderer(UnshatteredEntities.WOOL_WEAVER.get(), context -> new NPCEntityRenderer(context, "wool_weaver", true));
        event.registerEntityRenderer(UnshatteredEntities.FORGER.get(), context -> new NPCEntityRenderer(context, "forger"));
        event.registerEntityRenderer(UnshatteredEntities.BUBU.get(), context -> new NPCEntityRenderer(context, "bubu"));
    }
}
