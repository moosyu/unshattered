package io.github.moosyu.gui.layers;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.jspecify.annotations.NonNull;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

import static io.github.moosyu.data.attachments.UnshatteredAttachments.PLAYER_STATE;

public class BarLayer implements GuiLayer {
    final int SPRITE_WIDTH = 73;
    final int SPRITE_HEIGHT = 15;
    final int BAR_HEIGHT = 8;
    final Identifier SMALL_BAR = Identifier.fromNamespaceAndPath("unshattered", "textures/gui/small_bar.png");
    final int barColour;
    final int textColour;
    final ToDoubleFunction<Player> getCurrentValue;
    final ToDoubleFunction<Player> getCurrentPercentage;
    final int posXOffset;
    final int posYOffset;
    final Predicate<Player> visibilityConditions;

    /**
     * a coloured info bar gui layer that can display percentage and number value (converted to an int)
     * @param barColour the colour of the bar
     * @param textColour the colour of the bar's text
     * @param posXOffset the x offset of the bar and text
     * @param postYOffset the y offset of the bar and text
     * @param getCurrentValue the current value of whatever information is being displayed by the info bar
     * @param getMaxValue the current value of whatever information is being displayed by the info bar to find percentage of the bar required to be filled
     * @param visibilityConditions conditions aside from the base ones required for the bar to display
     */
    public BarLayer(int barColour, int textColour, int posXOffset, int postYOffset, ToDoubleFunction<Player> getCurrentValue, ToDoubleFunction<Player> getMaxValue, Predicate<Player> visibilityConditions) {
        this.barColour = barColour;
        this.textColour = textColour;
        this.posXOffset = posXOffset;
        this.posYOffset = postYOffset;
        this.getCurrentValue = getCurrentValue;
        this.getCurrentPercentage = player -> getCurrentValue.applyAsDouble(player) / getMaxValue.applyAsDouble(player);
        this.visibilityConditions = visibilityConditions;
    }

    /**
     * a coloured info bar gui layer that can display percentage and number value (converted to an int)
     * @param barColour the colour of the bar
     * @param textColour the colour of the bar's text
     * @param posXOffset the x offset of the bar and text
     * @param postYOffset the y offset of the bar and text
     * @param getCurrentValue the current value of whatever information is being displayed by the info bar
     * @param getMaxValue the current value of whatever information is being displayed by the info bar to find percentage of the bar required to be filled
     */
    public BarLayer(int barColour, int textColour, int posXOffset, int postYOffset, ToDoubleFunction<Player> getCurrentValue, ToDoubleFunction<Player> getMaxValue) {
        this.barColour = barColour;
        this.textColour = textColour;
        this.posXOffset = posXOffset;
        this.posYOffset = postYOffset;
        this.getCurrentValue = getCurrentValue;
        this.getCurrentPercentage = player -> getCurrentValue.applyAsDouble(player) / getMaxValue.applyAsDouble(player);
        this.visibilityConditions = _ -> true;
    }


    @Override
    public void render(GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        int POS_X_BAR = (graphics.guiWidth() / 2) - (SPRITE_WIDTH / 2) + posXOffset;
        int POS_Y_BAR = graphics.guiHeight() - SPRITE_HEIGHT + posYOffset;

        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null
                || !player.level().isClientSide()
                || minecraft.options.hideGui
                || player.gameMode() != GameType.SURVIVAL
                || player.getData(PLAYER_STATE.get()).isDialogueOpen()
                || !visibilityConditions.test(player)
        ) return;

        Font font = Minecraft.getInstance().font;
        String currentText = String.valueOf((int) getCurrentValue.applyAsDouble(player));
        double currentPercentage = getCurrentPercentage.applyAsDouble(player);
        graphics.blit(RenderPipelines.GUI_TEXTURED, SMALL_BAR, POS_X_BAR, POS_Y_BAR, 0, BAR_HEIGHT - 1, SPRITE_WIDTH, BAR_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT, barColour);
        graphics.blit(RenderPipelines.GUI_TEXTURED, SMALL_BAR, POS_X_BAR, POS_Y_BAR, 0, 0, (int) (SPRITE_WIDTH * currentPercentage), BAR_HEIGHT, SPRITE_WIDTH, SPRITE_HEIGHT, barColour);

        graphics.text(font, currentText, POS_X_BAR + (SPRITE_WIDTH / 2) - (font.width(currentText) / 2), POS_Y_BAR - 8, textColour, true);
    }
}
