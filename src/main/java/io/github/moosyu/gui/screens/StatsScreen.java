package io.github.moosyu.gui.screens;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

public class StatsScreen extends SimpleScreen {
    public StatsScreen(Component title) {
        super(title, 176, 166, "textures/gui/stats.png");
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        UnshatteredAttributeValues[] attributeValues = UnshatteredAttributeValues.values();
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        int lineHeight = 12;
        int visibleCount = 0;
        for (UnshatteredAttributeValues a : attributeValues) if (a.visible) visibleCount++;
        this.contentHeight = visibleCount * lineHeight;
        int scissorTop = this.backgroundTopLeft.y + 12;
        int scissorBottom = scissorTop + this.viewportHeight;
        int attributesDisplayed = 0;

        graphics.enableScissor(this.backgroundTopLeft.x, scissorTop, this.backgroundTopLeft.x + imageWidth, scissorBottom);

        for (UnshatteredAttributeValues currentAttribute : attributeValues) {
            if (currentAttribute.visible) {
                int lineY = this.backgroundTopLeft.y + 12 + (attributesDisplayed * lineHeight) - (int) scrollOffset;

                if (lineY + lineHeight >= scissorTop && lineY <= scissorBottom) {
                    int attributeBaseValue = (int) player.getAttributeBaseValue(currentAttribute.holder);
                    int attributeValue = (int) player.getAttributeValue(currentAttribute.holder);

                    graphics.text(font,
                            currentAttribute.symbol
                                    + Component.translatable("attribute.name.unshattered." + currentAttribute.id).getString()
                                    + ": "
                                    + (attributeValue == attributeBaseValue ? attributeBaseValue : attributeBaseValue + " (+" + attributeValue + ")")
                                    + (currentAttribute.percentage ? "%" : ""),
                            this.backgroundTopLeft.x + 12,
                            lineY,
                            currentAttribute.color
                    );
                }

                attributesDisplayed++;
            }
        }

        graphics.disableScissor();
    }
}
