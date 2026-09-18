package io.github.moosyu.abilities;

import io.github.moosyu.items.ItemTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public record AbilityContextKey<T>(Class<T> type) {
    public static final AbilityContextKey<ServerPlayer> PLAYER = new AbilityContextKey<>(ServerPlayer.class);
    public static final AbilityContextKey<LivingEntity> TARGET = new AbilityContextKey<>(LivingEntity.class);
    public static final AbilityContextKey<BlockPos> POSITION = new AbilityContextKey<>(BlockPos.class);
    public static final AbilityContextKey<ItemStack> ITEM_STACK = new AbilityContextKey<>(ItemStack.class);
    public static final AbilityContextKey<Double> DAMAGE_AMOUNT = new AbilityContextKey<>(Double.class);
    public static final AbilityContextKey<BlockState> BLOCKSTATE = new AbilityContextKey<>(BlockState.class);
    public static final AbilityContextKey<ItemTypes> ITEM_TYPE = new AbilityContextKey<>(ItemTypes.class);
}