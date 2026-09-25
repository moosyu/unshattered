package io.github.moosyu.data.fishing;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

public interface TriggerMiscReward extends FishingEntry {
    void trigger(ServerPlayer player);

    MapCodec<? extends TriggerMiscReward> codec();
}
