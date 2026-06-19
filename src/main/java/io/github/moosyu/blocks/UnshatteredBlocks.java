package io.github.moosyu.blocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.Unshattered.MODID;

public final class UnshatteredBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredBlock<Block> FIG_LOG_BLOCK = BLOCKS.register("fig_log", registerName -> new RotatedPillarBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, registerName))));
    public static final DeferredBlock<Block> BREAKABLE_FIG_LOG_BLOCK = BLOCKS.register("breakable_fig_log", registerName -> new RotatedPillarBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, registerName)).destroyTime(2.0f).sound(SoundType.WOOD)));
    public static final DeferredBlock<Block> BREAKABLE_COBBLESTONE_BLOCK = BLOCKS.registerBlock("breakable_cobblestone_block", props -> new Block(props.destroyTime(2.0f).sound(SoundType.STONE)));
    public static final DeferredBlock<Block> BREAKABLE_STONE_BLOCK = BLOCKS.registerBlock("breakable_stone_block", props -> new Block(props.destroyTime(2.0f).sound(SoundType.STONE)));
}
