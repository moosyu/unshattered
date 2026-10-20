package io.github.moosyu.events;

import io.github.moosyu.entities.UnshatteredEntities;
import io.github.moosyu.entities.mobs.*;
import io.github.moosyu.entities.npcs.NPCEntity;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.entity.monster.Creeper;
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
        event.put(UnshatteredEntities.BUBU.get(), NPCEntity.createNpcAttributes().build());
        event.put(UnshatteredEntities.JENNY.get(), NPCEntity.createNpcAttributes().build());
        event.put(UnshatteredEntities.GRAVEYARD_ZOMBIE_VILLAGER.get(), GraveyardZombieVillager.createAttributes().build());
        event.put(UnshatteredEntities.SNEAKY_CREEPER.get(), SneakyCreeper.createAttributes().build());
        event.put(UnshatteredEntities.LAPIS_ZOMBIE.get(), LapisZombie.createAttributes().build());
        event.put(UnshatteredEntities.SLIME_CHILD.get(), SlimeChild.createAttributes().build());
        event.put(UnshatteredEntities.EMERALD_SLIME.get(), EmeraldSlime.createAttributes().build());
        event.put(UnshatteredEntities.MASSIVE_EMERALD_SLIME.get(), MassiveEmeraldSlime.createAttributes().build());
        event.put(UnshatteredEntities.RHYS.get(), NPCEntity.createNpcAttributes().build());
        event.put(UnshatteredEntities.SKELETON_MINER.get(), SkeletonMiner.createAttributes().build());
    }
}
