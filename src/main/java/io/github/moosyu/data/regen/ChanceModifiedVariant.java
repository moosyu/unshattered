package io.github.moosyu.data.regen;

import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface ChanceModifiedVariant {
    /**
     * @return the chance that will be multiplied together with the base chance for the final spawn chance
     */
    float getModifiedRollChance(ServerPlayer player);
}
