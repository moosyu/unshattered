package io.github.moosyu.entities.states;

import net.minecraft.client.renderer.block.BlockModelRenderState;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;

public class SkeletonMinerRenderState extends SkeletonRenderState {
    public boolean holdingTnt;
    public final BlockModelRenderState heldTnt = new BlockModelRenderState();
}