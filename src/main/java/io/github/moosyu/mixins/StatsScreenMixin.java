package io.github.moosyu.mixins;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.screens.achievement.StatsScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.Set;

@Mixin(StatsScreen.GeneralStatisticsList.class)
public abstract class StatsScreenMixin {
    // values to leave over
    private static final Set<Identifier> acceptedVanillaValues = Set.of(Stats.DAMAGE_BLOCKED_BY_SHIELD,
            Stats.DAMAGE_DEALT,
            Stats.DAMAGE_TAKEN,
            Stats.CLIMB_ONE_CM,
            Stats.CROUCH_ONE_CM,
            Stats.FALL_ONE_CM,
            Stats.SPRINT_ONE_CM,
            Stats.SWIM_ONE_CM,
            Stats.WALK_ONE_CM,
            Stats.WALK_ON_WATER_ONE_CM,
            Stats.WALK_UNDER_WATER_ONE_CM,
            Stats.LEAVE_GAME,
            Stats.INTERACT_WITH_ANVIL,
            Stats.INTERACT_WITH_CRAFTING_TABLE,
            Stats.DROP,
            Stats.JUMP,
            Stats.MOB_KILLS,
            Stats.DEATHS,
            Stats.CROUCH_TIME,
            Stats.PLAY_TIME,
            Stats.TIME_SINCE_DEATH,
            Stats.TIME_SINCE_REST,
            Stats.TOTAL_WORLD_TIME
    );

    @ModifyVariable(method = "<init>", at = @At(value = "STORE", ordinal = 0))
    private ObjectArrayList<Stat<Identifier>> filterStats(ObjectArrayList<Stat<Identifier>> stats) {
        stats.removeIf(stat -> !acceptedVanillaValues.contains(stat.getValue()));
        return stats;
    }
}
