package io.github.moosyu.gui.components.tabs;

import io.github.moosyu.gui.components.SoundlessImageButton;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public final class TabBar {
    private record PlacedTab(UnshatteredTabs.UnshatteredTab unshatteredTab, int x, int y, int iconYOffset) {}

    public static final int GENERIC_OFFSET_TOP = -26;
    public static final int GENERIC_OFFSET_BOTTOM = 166;
    private static final int TAB_SIZE = 26;
    private final UnshatteredTabs.UnshatteredTab[] tabs;
    private final UnshatteredTabs.@Nullable UnshatteredTab selected;
    private final List<PlacedTab> placedTabs = new ArrayList<>();
    private final int yOffsetTop;
    private final int yOffsetBottom;

    /**
     * increasing offset moves tabs down from centre
     */
    public TabBar(UnshatteredTabs.UnshatteredTab[] tabs, UnshatteredTabs.@Nullable UnshatteredTab selected, int yOffsetTop, int yOffsetBottom) {
        this.tabs = tabs;
        this.selected = selected;
        this.yOffsetTop = yOffsetTop;
        this.yOffsetBottom = yOffsetBottom;
    }

    public void init(int leftPos, int topPos, Consumer<AbstractWidget> add) {
        placedTabs.clear();

        for (int i = 0; i < tabs.length; i++) {
            boolean flipped = i > 5;
            UnshatteredTabs.UnshatteredTab tab = tabs[i];
            int x = leftPos + 2 + ((i % 6) * (TAB_SIZE + 3));
            int y = topPos;

            if (flipped) {
                y += yOffsetBottom;
                y += tab == selected ? -4 : 0;
            } else {
                y += yOffsetTop;
                y += tab == selected ? 2 : 0;
            }

            SoundlessImageButton button = new SoundlessImageButton(x,
                    y,
                    TAB_SIZE,
                    tab == selected ? TAB_SIZE + 2 : TAB_SIZE,
                    tabSprites(flipped, (i % 6 == 0) ? 1 : 2),
                    _ -> tab.onClick().run()
            );
            button.active = tab != selected;
            button.setTooltip(Tooltip.create(tab.tooltip()));
            add.accept(button);

            placedTabs.add(new PlacedTab(tab, x, y, flipped ? 3 : 8));
        }
    }

    /**
     * needs to be called after super.extractRenderState
     */
    public void displayIcons(GuiGraphicsExtractor graphics) {
        for (PlacedTab placedTab : placedTabs) {
            placedTab.unshatteredTab().icon().render(graphics, placedTab.x() + 5, placedTab.y() + placedTab.iconYOffset());
        }
    }

    private static WidgetSprites tabSprites(boolean bottom, int n) {
        String side = bottom ? "bottom" : "top";
        Identifier unselected = Identifier.withDefaultNamespace("container/creative_inventory/tab_" + side + "_unselected_" + n);
        Identifier selected = Identifier.withDefaultNamespace("container/creative_inventory/tab_" + side + "_selected_" + n);
        return new WidgetSprites(unselected, selected, unselected, selected);
    }
}