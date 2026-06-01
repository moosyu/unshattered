package io.github.moosyu.events;

import io.github.moosyu.attributes.ModAttributes;
import io.github.moosyu.registers.AttributesRegistry;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class EntityAttributeModificationHandler {
    @SubscribeEvent
    public static void onModifyAttributesCalled(EntityAttributeModificationEvent event) {
        event.add(EntityType.PLAYER, ModAttributes.SWEEP.holder);
        event.add(EntityType.PLAYER, ModAttributes.FORAGING_FORTUNE.holder);
        event.add(EntityType.PLAYER, ModAttributes.HEALTH.holder);
        event.add(EntityType.PLAYER, ModAttributes.DAMAGE.holder);
        event.add(EntityType.PLAYER, ModAttributes.MANA.holder);
        event.add(EntityType.PLAYER, ModAttributes.STRENGTH.holder);
        event.add(EntityType.PLAYER, ModAttributes.CRITICAL_DAMAGE.holder);
        event.add(EntityType.PLAYER, ModAttributes.CRITICAL_CHANCE.holder);
        event.add(EntityType.PLAYER, ModAttributes.HEALTH_REGEN.holder);

        event.add(EntityType.ZOMBIE, ModAttributes.HEALTH.holder, 70.0d);
        event.add(EntityType.ZOMBIE, ModAttributes.DAMAGE.holder, 20.0d);

        event.add(EntityType.SKELETON, ModAttributes.HEALTH.holder, 100.0d);
        event.add(EntityType.SKELETON, ModAttributes.DAMAGE.holder, 15.0d);

        event.add(EntityType.SLIME, ModAttributes.HEALTH.holder, 80.0d);
        event.add(EntityType.SLIME, ModAttributes.DAMAGE.holder, 15.0d);

        event.add(EntityType.SPIDER, ModAttributes.HEALTH.holder, 120.0d);
        event.add(EntityType.SPIDER, ModAttributes.DAMAGE.holder, 35.0d);

        event.add(EntityType.CAVE_SPIDER, ModAttributes.HEALTH.holder, 110.0d);
        event.add(EntityType.CAVE_SPIDER, ModAttributes.DAMAGE.holder, 40.0d);

        event.add(EntityType.WITCH, ModAttributes.HEALTH.holder, 150.0d);
        event.add(EntityType.WITCH, ModAttributes.DAMAGE.holder, 20.0d);

        event.add(EntityType.ENDERMAN, ModAttributes.HEALTH.holder, 160.0d);
        event.add(EntityType.ENDERMAN, ModAttributes.DAMAGE.holder, 40.0d);

        event.add(EntityType.CREEPER, ModAttributes.HEALTH.holder, 80.0d);
        event.add(EntityType.CREEPER, ModAttributes.DAMAGE.holder, 20.0d);

        event.add(EntityType.SHEEP, ModAttributes.HEALTH.holder, 50.0d);

        event.add(EntityType.COW, ModAttributes.HEALTH.holder, 50.0d);

        event.add(EntityType.CHICKEN, ModAttributes.HEALTH.holder, 20.0d);

        event.add(EntityType.RABBIT, ModAttributes.HEALTH.holder, 130.0d);

        event.add(EntityType.PIG, ModAttributes.HEALTH.holder, 50.0d);

        event.add(EntityType.MOOSHROOM, ModAttributes.HEALTH.holder, 50.0d);
    }
}
