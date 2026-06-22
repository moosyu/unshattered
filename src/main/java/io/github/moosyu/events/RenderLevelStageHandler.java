package io.github.moosyu.events;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Matrix4f;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class RenderLevelStageHandler {
    @SubscribeEvent
    public static void onRenderLevel(RenderLevelStageEvent.AfterLevel event) {
        Minecraft minecraft = Minecraft.getInstance();
        PoseStack poseStack = event.getPoseStack();
        Camera camera = minecraft.gameRenderer.getMainCamera();
        Vec3 camPos = camera.position();

        poseStack.pushPose();
        poseStack.translate(-camPos.x, -camPos.y, -camPos.z);
        BufferBuilder builder = new BufferBuilder(new ByteBufferBuilder(RenderType.SMALL_BUFFER_SIZE), VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        Matrix4f matrix = poseStack.last().pose();
        builder.addVertex(matrix, 0, 100, 0).setColor(0xFFFF0000);
        builder.addVertex(matrix, 1, 100, 0).setColor(0xFFFF0000);
        builder.addVertex(matrix, 1, 101, 0).setColor(0xFFFF0000);
        builder.addVertex(matrix, 0, 101, 0).setColor(0xFFFF0000);
        poseStack.popPose();
    }
}