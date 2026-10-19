package io.github.moosyu.events;

import io.github.moosyu.entities.UnshatteredEntities;
import io.github.moosyu.entities.renderers.*;
import net.minecraft.client.renderer.entity.CreeperRenderer;
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
        event.registerEntityRenderer(UnshatteredEntities.JOTRAELINE_GREATFORGE.get(), NPCEntityRenderer::new);
        event.registerEntityRenderer(UnshatteredEntities.WOOL_WEAVER.get(), context -> new NPCEntityRenderer(context, true));
        event.registerEntityRenderer(UnshatteredEntities.FORGER.get(), NPCEntityRenderer::new);
        event.registerEntityRenderer(UnshatteredEntities.BUBU.get(), NPCEntityRenderer::new);
        event.registerEntityRenderer(UnshatteredEntities.JENNY.get(), context -> new NPCEntityRenderer(context, true));
        event.registerEntityRenderer(UnshatteredEntities.SNEAKY_CREEPER.get(), CreeperRenderer::new);
        event.registerEntityRenderer(UnshatteredEntities.LAPIS_ZOMBIE.get(), LapisZombieRenderer::new);
        event.registerEntityRenderer(UnshatteredEntities.SLIME_CHILD.get(), context ->  new UnshatteredSlimeRenderer(context, 2));
        event.registerEntityRenderer(UnshatteredEntities.EMERALD_SLIME.get(), context ->  new UnshatteredSlimeRenderer(context, 3));
        event.registerEntityRenderer(UnshatteredEntities.MASSIVE_EMERALD_SLIME.get(), context ->  new UnshatteredSlimeRenderer(context, 8));
    }
}
