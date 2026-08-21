package io.github.moosyu.blocks;

import com.mojang.serialization.MapCodec;
import io.github.moosyu.data.dialogue.*;
import io.github.moosyu.util.DialogueUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import static io.github.moosyu.Unshattered.MODID;

public class TalkingRockBlock extends HorizontalDirectionalBlock implements DialogueInteractable {
    private static final VoxelShape COLLISION_SHAPE = Block.box(5, 0, 5, 11, 9, 11);
    private static final Component NAME = Component.translatable("interactable.name.unshattered.rock").withStyle(ChatFormatting.BOLD);
    public static final Identifier ROCK_DIALOGUE_TREE = DialogueUtil.createDialogueTreeIdentifier(NAME.getString().toLowerCase(), "rock_dialogue_tree");
    public static final Identifier HI_MESSAGE_IDENTIFIER = DialogueUtil.createDialogueNodeIdentifier(ROCK_DIALOGUE_TREE, NAME.getString(), "hi");
    public static final Identifier HI2_MESSAGE_IDENTIFIER = DialogueUtil.createDialogueNodeIdentifier(ROCK_DIALOGUE_TREE, NAME.getString(), "hi_2");
    public static final Identifier ROCKS_QUEST = Identifier.fromNamespaceAndPath(MODID, "rocks_quest");

    public TalkingRockBlock(Properties properties) {
        super(properties.noOcclusion());
    }

    @Override
    protected @NonNull VoxelShape getShape(@NonNull BlockState state, @NonNull BlockGetter level, @NonNull BlockPos pos, @NonNull CollisionContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    protected @NonNull MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(TalkingRockBlock::new);
    }

    @Override
    public Component getInteractableName() {
        return NAME;
    }

    @Override
    public DialogueTree getDialogueTree(RegistryAccess registryAccess) {
        return DialogueUtil.getDialogueTreeObject(registryAccess, ROCK_DIALOGUE_TREE);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(@NonNull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
