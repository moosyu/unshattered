package io.github.moosyu.particles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;

public class DamageNumberParticle extends Particle {
    private final String text;

    public DamageNumberParticle(ClientLevel level, double x, double y, double z, String text) {
        super(level, x, y, z);
        this.text = text;
    }

    @Override
    public void render(VertexConsumer buffer, Camera camera, float partialTicks) {
        // todo: figure out how to do this shit (im gonna die)
        System.out.println(x);
        System.out.println(y);
        System.out.println(z);
        System.out.println(text);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.CUSTOM;
    }
}