package io.github.moosyu.events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class RenderLevelStageHandler {
    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent.AfterLevel event) {
        PoseStack poseStack = event.getPoseStack();
    }
}
