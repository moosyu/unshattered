package io.github.moosyu.screens.components;

import com.daqem.uilib.gui.component.EmptyComponent;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class StatsComponent extends EmptyComponent {
    public StatsComponent(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick, parentWidth, parentHeight);
    }
}
