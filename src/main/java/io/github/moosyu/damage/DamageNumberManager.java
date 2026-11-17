package io.github.moosyu.damage;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import org.joml.Matrix3x2fStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class DamageNumberManager {
    public static final List<DamageNumber> NUMBERS = new ArrayList<>();

    public static List<DamageNumber> getNumbers() {
        return NUMBERS;
    }

    public static void tick() {
        NUMBERS.removeIf(DamageNumber::tick);
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        DamageNumberManager.tick();
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        List<DamageNumber> numbers = getNumbers();
        if (numbers.isEmpty()) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        Camera camera = minecraft.gameRenderer.getMainCamera();
        Vec3 cameraPos = camera.position();
        GuiGraphicsExtractor graphics = event.getGuiGraphics();
        float partialTick = minecraft.getDeltaTracker().getGameTimeDeltaPartialTick(false);
        double aspect = (double) graphics.guiWidth() / graphics.guiHeight();
        double projectionScale = 1.0 / Math.tan(Math.toRadians(minecraft.options.fov().get()) / 2.0);
        Quaternionf inverse = new Quaternionf(camera.rotation()).conjugate();
        Font font = minecraft.font;

        for (DamageNumber damageNumber : numbers) {
            float progress = Math.min((damageNumber.age + partialTick) / DamageNumber.MAX_LIFETIME, 1.0f);
            Vector3f relativePosition = new Vector3f((float) (damageNumber.position.x - cameraPos.x),
                    (float) (damageNumber.position.y + progress * 0.75 - cameraPos.y),
                    (float) (damageNumber.position.z - cameraPos.z)
            );
            relativePosition.rotate(inverse);

            // needs to be rotated first before getting depth. -z is the distance in front of the camera.
            float depth = -relativePosition.z;

            // roughly behind the camera i think
            if (depth <= 0.05f) {
                continue;
            }

            double normalizedDeviceX = (relativePosition.x / depth) * projectionScale / aspect;
            double normalizedDeviceY = (relativePosition.y / depth) * projectionScale;

            float screenX = (float) ((normalizedDeviceX * 0.5 + 0.5) * graphics.guiWidth());
            float screenY = (float) ((0.5 - normalizedDeviceY * 0.5) * graphics.guiHeight());
            double pixelsPerBlock = (graphics.guiHeight() * 0.5) * projectionScale / depth;
            // 0.35 is the text height in blocks
            float fontScale = Mth.clamp((float) (0.35f * pixelsPerBlock / font.lineHeight), 0.1f, 3.0f);
            int colour = UnshatteredUtils.getOpacityColor(damageNumber.damageType.colour, 1.0f - (progress * progress));
            String text = String.format("%,d", damageNumber.number);
            Matrix3x2fStack pose = graphics.pose();

            pose.pushMatrix();
            pose.translate(screenX, screenY);
            pose.scale(fontScale, fontScale);
            graphics.text(font, text, -font.width(text) / 2, -font.lineHeight / 2, colour, true);
            pose.popMatrix();
        }
    }
}