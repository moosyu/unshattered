package io.github.moosyu.entities.states;

import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

import java.util.ArrayList;
import java.util.List;

public class TossedTerrainRenderState extends EntityRenderState {
    public final List<MovingBlockRenderState> blocks = new ArrayList<>();

    public float dirX;
    public float dirZ;
}