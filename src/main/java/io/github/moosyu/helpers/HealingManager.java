package io.github.moosyu.helpers;

import net.minecraft.world.entity.LivingEntity;

public class HealingManager {
    private static final ThreadLocal<Boolean> ALLOWED_HEAL = ThreadLocal.withInitial(() -> false);

    public static void heal(LivingEntity entity, float amount) {
        ALLOWED_HEAL.set(true);
        try {
            entity.heal(amount);
        } finally {
            ALLOWED_HEAL.set(false);
        }
    }

    public static boolean isAllowed() {
        return ALLOWED_HEAL.get();
    }
}
