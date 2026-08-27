package io.github.moosyu.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.jspecify.annotations.NonNull;

public class RedSlashParticle extends SingleQuadParticle {
    public RedSlashParticle(ClientLevel level, double x, double y, double z, TextureAtlasSprite sprite) {
        super(level, x, y, z, sprite);
        this.gravity = 0.0f;
        this.lifetime = 12;
        this.hasPhysics = false;
        this.roll = (float) Math.toRadians(45);
        this.oRoll = this.roll;
        this.setColor(1f, 0.15f, 0.15f);
        this.setAlpha(1f);
    }

    @Override
    public void tick() {
        super.tick();
        this.setAlpha(1f - (float) this.age / this.lifetime);
    }

    @Override
    protected @NonNull Layer getLayer() {
        return Layer.TRANSLUCENT;
    }
}
