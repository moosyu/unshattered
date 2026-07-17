package io.github.moosyu.mixins;

import net.minecraft.world.entity.npc.CatSpawner;
import net.minecraft.world.entity.npc.wanderingtrader.WanderingTraderSpawner;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import net.minecraft.world.level.levelgen.PhantomSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// should hopefully stop everything implementing CustomSpawner (because they dont get cancelled by mob spawn event...)
@Mixin({PhantomSpawner.class, CatSpawner.class, WanderingTraderSpawner.class, PatrolSpawner.class})
public abstract class CustomSpawnersMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(CallbackInfo callbackInfo) {
        callbackInfo.cancel();
    }
}
