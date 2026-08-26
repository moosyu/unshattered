package io.github.moosyu.blocks;

import io.github.moosyu.data.regen.RegenPath;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class RegeneratingBlock extends Block {
    // generous max starting from 0 and counting up
    public static final IntegerProperty STAGE = IntegerProperty.create("regeneration_stage", 0, 5);
    public final RegenPath regenPath;

    public RegeneratingBlock(Properties properties, RegenPath regenPath) {
        super(properties);
        this.registerDefaultState(getStateDefinition().any().setValue(STAGE, 0));
        this.regenPath = regenPath;
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE);
    }

}
