package io.github.moosyu.skills.fishing;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;

public record FishingContext(ServerLevel level, Player player, FishingHook hook) {}