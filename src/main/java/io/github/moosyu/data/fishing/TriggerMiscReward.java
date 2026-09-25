package io.github.moosyu.data.fishing;

import com.mojang.serialization.MapCodec;
import net.minecraft.server.level.ServerPlayer;

public interface TriggerMiscReward {
    void trigger(ServerPlayer player);

    MapCodec<? extends TriggerMiscReward> codec();
}
