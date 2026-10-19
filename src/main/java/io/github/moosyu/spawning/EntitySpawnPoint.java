package io.github.moosyu.spawning;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.Item;

import java.util.Map;

public record EntitySpawnPoint(EntityType<?> entityType, BlockPos blockPos, Map<EquipmentSlot, Holder<Item>> equipment) {
    public static Codec<EntitySpawnPoint> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    EntityType.CODEC.fieldOf("entity_type").forGetter(EntitySpawnPoint::entityType),
                    BlockPos.CODEC.fieldOf("block_pos").forGetter(EntitySpawnPoint::blockPos),
                    Codec.unboundedMap(EquipmentSlot.CODEC, Item.CODEC)
                            .optionalFieldOf("equipment", Map.of())
                            .forGetter(EntitySpawnPoint::equipment)
            ).apply(instance, EntitySpawnPoint::new)
    );
}
