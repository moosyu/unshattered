package io.github.moosyu.events;

import io.github.moosyu.attributes.UnshatteredAttributes;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class EntityAttributeModificationHandler {
    @SubscribeEvent
    public static void onModifyAttributesCalled(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, UnshatteredAttributes.SWEEP.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.FORAGING_FORTUNE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.HEALTH.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.DAMAGE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.MANA.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.STRENGTH.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.CRITICAL_DAMAGE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.CRITICAL_CHANCE.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.HEALTH_REGEN.holder);
        event.add(EntityType.PLAYER, UnshatteredAttributes.DEFENSE.holder);

        event.add(EntityType.ZOMBIE, UnshatteredAttributes.HEALTH.holder, 70.0d);
        event.add(EntityType.ZOMBIE, UnshatteredAttributes.DAMAGE.holder, 20.0d);

        event.add(EntityType.SKELETON, UnshatteredAttributes.HEALTH.holder, 100.0d);
        event.add(EntityType.SKELETON, UnshatteredAttributes.DAMAGE.holder, 15.0d);

        event.add(EntityType.SLIME, UnshatteredAttributes.HEALTH.holder, 80.0d);
        event.add(EntityType.SLIME, UnshatteredAttributes.DAMAGE.holder, 15.0d);

        event.add(EntityType.SPIDER, UnshatteredAttributes.HEALTH.holder, 120.0d);
        event.add(EntityType.SPIDER, UnshatteredAttributes.DAMAGE.holder, 35.0d);

        event.add(EntityType.CAVE_SPIDER, UnshatteredAttributes.HEALTH.holder, 110.0d);
        event.add(EntityType.CAVE_SPIDER, UnshatteredAttributes.DAMAGE.holder, 40.0d);

        event.add(EntityType.WITCH, UnshatteredAttributes.HEALTH.holder, 150.0d);
        event.add(EntityType.WITCH, UnshatteredAttributes.DAMAGE.holder, 20.0d);

        event.add(EntityType.ENDERMAN, UnshatteredAttributes.HEALTH.holder, 160.0d);
        event.add(EntityType.ENDERMAN, UnshatteredAttributes.DAMAGE.holder, 40.0d);

        event.add(EntityType.CREEPER, UnshatteredAttributes.HEALTH.holder, 80.0d);
        event.add(EntityType.CREEPER, UnshatteredAttributes.DAMAGE.holder, 20.0d);

        event.add(EntityType.SHEEP, UnshatteredAttributes.HEALTH.holder, 50.0d);

        event.add(EntityType.COW, UnshatteredAttributes.HEALTH.holder, 50.0d);

        event.add(EntityType.CHICKEN, UnshatteredAttributes.HEALTH.holder, 20.0d);

        event.add(EntityType.RABBIT, UnshatteredAttributes.HEALTH.holder, 130.0d);

        event.add(EntityType.PIG, UnshatteredAttributes.HEALTH.holder, 50.0d);

        event.add(EntityType.MOOSHROOM, UnshatteredAttributes.HEALTH.holder, 50.0d);
    }
}
