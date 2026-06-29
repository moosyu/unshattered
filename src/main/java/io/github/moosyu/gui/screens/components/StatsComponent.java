package io.github.moosyu.gui.screens.components;

import com.daqem.uilib.gui.component.EmptyComponent;
import com.daqem.uilib.gui.component.text.TextComponent;
import com.daqem.uilib.gui.widget.ScrollContainerWidget;
import io.github.moosyu.attributes.UnshatteredAttributeTypes;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class StatsComponent extends EmptyComponent {
    boolean initialList = false;
    ScrollContainerWidget list = new ScrollContainerWidget(this.getWidth(), this.getHeight(), 5);
    final UnshatteredAttributeValues[] usableUnshatteredAttributes = Arrays.stream(UnshatteredAttributeValues.values()).filter(a -> a.type != UnshatteredAttributeTypes.INVISIBLE).toArray(UnshatteredAttributeValues[]::new);

    public StatsComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        if (!initialList) {
            super.extractRenderState(graphics, mouseX, mouseY, partialTick, parentWidth, parentHeight);
            for (UnshatteredAttributeValues currentAttribute : usableUnshatteredAttributes) {
                list.addComponent(new TextComponent(this.getX(), 350, Component.translatable("attribute.name.unshattered." + currentAttribute.id), currentAttribute.color));
            }
            this.addWidget(list);
            initialList = true;
        }
    }
}
