package io.github.moosyu.layers;

import io.github.moosyu.attributes.ModAttributes;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.layers.UnshatteredGuiLayers.SMALL_BAR;

public class ManaBarLayer implements GuiLayer {
    @Override
    public void render(GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        final int SPRITE_WIDTH = 73;
        final int SPRITE_HEIGHT = 15;
        final int BAR_HEIGHT = 8;
        final int POS_X_BAR = (graphics.guiWidth() / 2) - (SPRITE_WIDTH / 2) + 54;
        final int POS_Y_BAR = graphics.guiHeight() - SPRITE_HEIGHT - 18;

        Player player = Minecraft.getInstance().player;
        if (!player.level().isClientSide()) return;
        final AttributeInstance manaAttribute = Minecraft.getInstance().player.getAttribute(ModAttributes.MANA.holder);
        if (manaAttribute == null) return;
        final String currentManaText = String.valueOf((int) manaAttribute.getValue());
        final Font font = Minecraft.getInstance().font;

        graphics.blit(RenderPipelines.GUI_TEXTURED, SMALL_BAR, POS_X_BAR, POS_Y_BAR, 0, BAR_HEIGHT - 1, SPRITE_WIDTH, BAR_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT, 0xFF00A6FF);
        graphics.blit(RenderPipelines.GUI_TEXTURED, SMALL_BAR, POS_X_BAR, POS_Y_BAR, 0, 0, SPRITE_WIDTH, BAR_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT, 0xFF00A6FF);

        graphics.text(Minecraft.getInstance().font, currentManaText, POS_X_BAR + (SPRITE_WIDTH / 2) - (font.width(currentManaText) / 2), POS_Y_BAR - 8, 0xFF4E5BC6, true);
    }
}
