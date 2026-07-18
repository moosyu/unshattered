package io.github.moosyu.events;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.util.TextUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.TriState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;

import java.util.Optional;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class RenderNameTagHandler {
    @SubscribeEvent
    public static void onNameTagCanRender(RenderNameTagEvent.CanRender event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null || event.getEntity().distanceToSqr(player) > 512) {
            event.setCanRender(TriState.FALSE);
            return;
        }

        if (event.getEntity() instanceof LivingEntity entity) {
            event.setCanRender(TriState.TRUE);
            AttributeInstance healthAttribute = entity.getAttribute(UnshatteredAttributeValues.HEALTH.holder);
            if (healthAttribute == null) {
                event.setContent(Component.literal("SOMETHING WENT WRONG!! (NO HEALTH ASSIGNED)").withColor(0xFFFF5555));
                return;
            }

            Optional<AttributeSupplier> supplier = getDefaultSupplier(entity);
            double baseHealth = supplier.map(s -> s.getBaseValue(UnshatteredAttributeValues.HEALTH.holder)).orElse(0.0);
            event.setContent(Component.literal(entity.getPlainTextName()).withColor(0xFFFF5555)
                            .append(Component.literal(" " + TextUtils.oneDecimalFormat.format(healthAttribute.getValue())).withColor(0xFF55FF55))
                            .append(Component.literal("/").withColor(0xFFFFFFFF))
                            .append(Component.literal(TextUtils.oneDecimalFormat.format(baseHealth)).withColor(0xFF55FF55))
                            .append(Component.literal("❤").withColor(0xFFFF5555))
            );
        }
    }

    @SubscribeEvent
    public static void onNameTagDoRender(RenderNameTagEvent.DoRender event) {
        event.setCanceled(true);

        PoseStack poseStack = event.getPoseStack();
        EntityRenderState state = event.getEntityRenderState();
        FormattedCharSequence text = event.getContent().getVisualOrderText();

        Camera camera = Minecraft.getInstance().getEntityRenderDispatcher().camera;
        if (camera == null) return;

        poseStack.pushPose();

        float entityHeight = state.boundingBoxHeight;
        poseStack.translate(0.0D, entityHeight + 0.5D, 0.0D);
        poseStack.mulPose(camera.rotation());
        poseStack.scale(EntityRenderer.NAMETAG_SCALE, -EntityRenderer.NAMETAG_SCALE, EntityRenderer.NAMETAG_SCALE);
        // text, x, y, color, ordered text, shadow, text layer type, light, color, background color, outline color
        event.getSubmitNodeCollector().submitText(
                poseStack,
                (float)(-Minecraft.getInstance().font.width(text) / 2),
                0.0f,
                text,
                true,
                Font.DisplayMode.NORMAL,
                state.lightCoords,
                -1,
                0,
                0
        );

        poseStack.popPose();
    }

    // random bullshit to hide the cast warning
    private static <T extends LivingEntity> Optional<AttributeSupplier> getDefaultSupplier(T entity) {
        @SuppressWarnings("unchecked")
        EntityType<T> type = (EntityType<T>) entity.getType();
        return Optional.of(DefaultAttributes.getSupplier(type));
    }
}