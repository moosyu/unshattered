package io.github.moosyu.blocks;

import io.github.moosyu.blocks.regenerating.RegeneratingStoneBlockAnchor;
import io.github.moosyu.data.regen.RegenPath;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

import static io.github.moosyu.Unshattered.MODID;

public final class UnshatteredBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredBlock<Block> FIG_LOG_BLOCK = BLOCKS.register("fig_log", registerName -> new RotatedPillarBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, registerName))));
    public static final DeferredBlock<Block> ROCK_TALKABLE_BLOCK = BLOCKS.registerBlock("rock_talkable", TalkingRockBlock::new);

    public static final DeferredBlock<Block> BREAKABLE_FIG_LOG_BLOCK = BLOCKS.register("breakable_fig_log",
            registerName -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registerName))
                    .destroyTime(10.0f)
                    .sound(SoundType.WOOD)
                    .requiresCorrectToolForDrops()
            )
    );
    public static final DeferredBlock<RegeneratingBlock> BREAKABLE_COBBLESTONE_BLOCK = BLOCKS.registerBlock("breakable_cobblestone_block",
            props -> new RegeneratingBlock(props
                    .destroyTime(1.5f)
                    .sound(SoundType.STONE)
                    .requiresCorrectToolForDrops(),
                    new RegenPath(List.of(), 2)
            )
    );
    public static final DeferredBlock<Block> BREAKABLE_STONE_BLOCK = BLOCKS.registerBlock("breakable_stone_block", RegeneratingStoneBlockAnchor::new);
}
