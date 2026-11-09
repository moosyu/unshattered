package io.github.moosyu.entities.states;

import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;

public class NPCRenderState extends EntityRenderState {
    // getRenderer in EntityRenderDispatcher doesn't work if AvatarRenderState is used normally
    public final AvatarRenderState avatar = new AvatarRenderState();
    public Identifier texture;
}