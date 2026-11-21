package io.github.moosyu.entities;

import io.github.moosyu.Unshattered;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.List;

public class UnshatteredEntityDataSerializers {
    public static final DeferredRegister<EntityDataSerializer<?>> ENTITY_DATA_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, Unshattered.MODID);


    public static final DeferredHolder<EntityDataSerializer<?>, EntityDataSerializer<List<BlockState>>> BLOCK_LIST =
            ENTITY_DATA_SERIALIZERS.register("block_list", () -> EntityDataSerializer.forValueType(
                    ByteBufCodecs.<ByteBuf, BlockState>list().apply(ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY))
            ));
}