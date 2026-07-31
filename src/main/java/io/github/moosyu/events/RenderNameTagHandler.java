package io.github.moosyu.events;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.util.TextUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.TriState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;

import java.util.Optional;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class RenderNameTagHandler {
    @SubscribeEvent
    public static void onNameTagCanRender(RenderNameTagEvent.CanRender event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        Entity entity = event.getEntity();

        if (player == null || entity.distanceToSqr(player) > 512 || entity == player) {
            event.setCanRender(TriState.FALSE);
            return;
        }

        if (entity instanceof LivingEntity livingEntity) {
            event.setCanRender(TriState.TRUE);
            AttributeInstance healthAttribute = livingEntity.getAttribute(UnshatteredAttributeValues.HEALTH.holder);
            if (healthAttribute == null) {
                event.setContent(Component.literal("SOMETHING WENT WRONG!! (NO HEALTH ASSIGNED)").withColor(0xFFFF5555));
                return;
            }

            Optional<AttributeSupplier> supplier = getDefaultSupplier(livingEntity);
            double baseHealth = supplier.map(s -> s.getBaseValue(UnshatteredAttributeValues.HEALTH.holder)).orElse(0.0);
            int healthColour;

            double healthFraction = healthAttribute.getValue() / baseHealth;
            if (healthAttribute.getValue() / baseHealth <= 0.1) {
                healthColour = 0xFFFF5555;
            } else if (healthFraction <= 0.5) {
                healthColour = 0xFFFFFF55;
            } else {
                healthColour = 0xFF55FF55;
            }

            event.setContent(Component.literal(livingEntity.getPlainTextName()).withColor(0xFFFF5555)
                            .append(Component.literal(" " + TextUtils.oneDecimalFormat.format(healthAttribute.getValue())).withColor(healthColour))
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
        poseStack.translate(0.0d, entityHeight + 0.5d, 0.0d);
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