package io.github.moosyu.data.regen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;

import java.util.List;

/**
 * defines a path that a block can be regenerated through
 * @param regenBlocks the order of blocks that get broken and regenerated with original being first and the final stage (generally bedrock) at the end
 * @param ticksPerStage the amount of ticks required to have occurred for a block to regenerate
 */
public record RegenPath(List<Block> regenBlocks, int ticksPerStage) {
    public static Codec<RegenPath> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.BLOCK.byNameCodec().listOf().fieldOf("regen_blocks").forGetter(RegenPath::regenBlocks),
                    Codec.INT.fieldOf("ticks_per_stage").forGetter(RegenPath::ticksPerStage)
            ).apply(instance, RegenPath::new)
    );
}
