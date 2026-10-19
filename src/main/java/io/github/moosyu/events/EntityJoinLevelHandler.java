package io.github.moosyu.events;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.monster.Slime;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class EntityJoinLevelHandler {
    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        // loaded from disk would redo this all every time the entity is loaded not just on first spawn
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof Mob mob && !event.loadedFromDisk()) {
            mob.setCanPickUpLoot(false);

            if (mob instanceof Slime slime) {
                AttributeInstance maxHealthAttribute = slime.getAttribute(UnshatteredAttributeValues.CURRENT_HEALTH.holder);
                AttributeInstance healthAttribute = slime.getAttribute(UnshatteredAttributeValues.MAX_HEALTH.holder);
                AttributeInstance damageAttribute = slime.getAttribute(UnshatteredAttributeValues.DAMAGE.holder);

                if (healthAttribute != null && damageAttribute != null && maxHealthAttribute != null) {
                    maxHealthAttribute.setBaseValue(10.0d * slime.getSize());
                    healthAttribute.setBaseValue(10.0d * slime.getSize());
                    damageAttribute.setBaseValue(3.0d * slime.getSize());
                }
            }
        }
    }
}
