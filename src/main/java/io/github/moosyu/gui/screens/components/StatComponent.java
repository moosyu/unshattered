package io.github.moosyu.gui.screens.components;

import com.daqem.uilib.gui.component.AbstractComponent;
import com.daqem.uilib.gui.component.text.TextComponent;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

/**
 * displays two spaced stats
 */
public class StatComponent extends AbstractComponent {
    final UnshatteredAttributeValues attributeLeft;
    final UnshatteredAttributeValues attributeRight;

    public StatComponent(int x, int y, int width, int height, UnshatteredAttributeValues attributeLeft, UnshatteredAttributeValues attributeRight) {
        super(x, y, width, height);
        this.attributeLeft = attributeLeft;
        this.attributeRight = attributeRight;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        new TextComponent(this.getX(), this.getY(), Component.translatable(attributeLeft.id), attributeLeft.color);
        new TextComponent(this.getX(), this.getY(), Component.translatable(attributeRight.id), attributeRight.color);
    }
}
