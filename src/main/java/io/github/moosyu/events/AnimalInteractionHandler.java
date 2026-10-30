package io.github.moosyu.events;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.BabyEntitySpawnEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class AnimalInteractionHandler {
    @SubscribeEvent
    public static void onFeed(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof Animal animal && animal.isFood(event.getItemStack())) {
            event.setCancellationResult(InteractionResult.FAIL);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onBreed(BabyEntitySpawnEvent event) {
        event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onJoin(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide()) {
            return;
        }

        if (event.getEntity() instanceof Animal animal) {
            animal.goalSelector.removeAllGoals(goal -> goal instanceof TemptGoal);
        }
    }
}
