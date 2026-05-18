package io.github.moosyu.events;

import io.github.moosyu.registers.AttributesRegistry;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

import static io.github.moosyu.NNO.MODID;

public class LivingDamageHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onLivingDamage(LivingDamageEvent.Pre event) {
            if (event.getEntity() instanceof Player player) {
                event.setNewDamage(0.0f);
                Entity attacker = event.getSource().getEntity();
                if (attacker instanceof LivingEntity entity) {
                    double mobDamage = entity.getAttribute(AttributesRegistry.DAMAGE).getBaseValue();
                    float currentHealth = player.getHealth();

                    if (currentHealth - mobDamage > 0.0f) {
                        player.setHealth((float)(currentHealth - mobDamage));
                    } else {
                        // this doesnt work, i assume due to setting damage to 0.0f earlier.
                        // probably fix by doing the same thing with healing manager
                        System.out.println("death triggered");
                        player.kill();
                    }
                }
            } else if (event.getSource().is(DamageTypeTags.IS_FIRE)) {
                // seems to be required to allow players to damage enemies on fire
                // todo: make it so this doesnt go batshit when the entity is in lava
                event.getEntity().invulnerableTime = 0;
            }
        }
    }
}
