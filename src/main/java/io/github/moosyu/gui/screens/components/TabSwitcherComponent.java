package io.github.moosyu.gui.screens.components;

import com.daqem.uilib.gui.component.EmptyComponent;
import com.daqem.uilib.gui.component.sprite.SpriteComponent;
import io.github.moosyu.gui.screens.states.TabSwitcherState;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;

import static io.github.moosyu.Unshattered.MODID;

public class TabSwitcherComponent extends EmptyComponent {
    private final TabSwitcherState state;
    private TabSwitcherState.Tabs cachedTab;

    public TabSwitcherComponent(int x, int y, int width, int height, TabSwitcherState state) {
        super(x, y, width, height);
        this.state = state;
        this.cachedTab = state.getSelectedTab();
    }

    private void rebuild() {
        this.clearComponents();

        if (state.getSelectedTab().equals(TabSwitcherState.Tabs.STATS)) {
            this.addComponent(new StatsComponent(0, 0, 350, 150));
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick, int parentWidth, int parentHeight) {
        if (!this.cachedTab.equals(state.getSelectedTab())) {
            this.cachedTab = state.getSelectedTab();
            this.rebuild();
            this.updateParentPosition(getParentX(), getParentY(), parentWidth, parentHeight);
        }

        for (int i = 0; i < TabSwitcherState.Tabs.values().length; i++) {
            this.addComponent(new SpriteComponent((i * 48), this.getHeight() - 32, 32, 32, Identifier.fromNamespaceAndPath(MODID, "tab_closed")));
        }

        super.extractRenderState(graphics, mouseX, mouseY, partialTick, parentWidth, parentHeight);
    }
}
