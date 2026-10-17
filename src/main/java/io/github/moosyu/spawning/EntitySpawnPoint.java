package io.github.moosyu.spawning;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;

public record EntitySpawnPoint(EntityType<?> entityType, BlockPos blockPos) {
    public static Codec<EntitySpawnPoint> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    EntityType.CODEC.fieldOf("entity_type").forGetter(EntitySpawnPoint::entityType),
                    BlockPos.CODEC.fieldOf("block_pos").forGetter(EntitySpawnPoint::blockPos)
            ).apply(instance, EntitySpawnPoint::new)
    );
}
