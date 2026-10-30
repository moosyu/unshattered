package io.github.moosyu.events;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.entities.UnshatteredEntities;
import io.github.moosyu.entities.mobs.*;
import io.github.moosyu.entities.npcs.NPCEntity;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.world.entity.animal.rabbit.Rabbit;
import net.minecraft.world.entity.animal.sheep.Sheep;
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
        event.put(UnshatteredEntities.REDSTONE_PIGMAN.get(), RedstonePigman.createAttributes().build());
        event.put(UnshatteredEntities.SEA_WALKER.get(), SeaWalker.createAttributes().build());
        event.put(UnshatteredEntities.OASIS_SHEEP.get(), Sheep.createAttributes()
                .add(UnshatteredAttributeValues.MAX_HEALTH.holder, 50.0f)
                .add(UnshatteredAttributeValues.CURRENT_HEALTH.holder, 50.0f)
                .build()
        );
        event.put(UnshatteredEntities.OASIS_RABBIT.get(), Rabbit.createAttributes()
                .add(UnshatteredAttributeValues.MAX_HEALTH.holder, 50.0f)
                .add(UnshatteredAttributeValues.CURRENT_HEALTH.holder, 50.0f)
                .build()
        );
    }
}
