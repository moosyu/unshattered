package io.github.moosyu.gui.screens;

import io.github.moosyu.gui.widgets.ScrollerWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

public abstract class ScrollableListScreen extends SimpleScreen {
    protected final int lineHeight;
    protected final int viewportHeight;
    protected double scrollOffset = 0;
    protected int contentHeight = 0;
    protected ScrollerWidget scroller;

    protected ScrollableListScreen(Component title, int imageWidth, int imageHeight, String backgroundTexture, int lineHeight) {
        super(title, imageWidth, imageHeight, backgroundTexture);
        this.lineHeight = lineHeight;
        this.viewportHeight = imageHeight - 24;
    }

    /**
     * @return number of visible entries/lines to render this frame. Called once per
     * frame before rendering; keep it cheap and stable across calls within a frame.
     */
    protected abstract int getItemCount();

    /**
     * render a single entry within the scrollable list. only called when the line is within the visible scissor region.
     * @param graphics GuiGraphicsExtractor
     * @param index the visible index of this line, in render order
     * @param lineY the y position for this line, already offset by scroll
     * @param scissorTop top of the visible scissor region
     * @param scissorBottom bottom of the visible scissor region
     */
    protected abstract void renderLine(GuiGraphicsExtractor graphics, int index, int lineY, int scissorTop, int scissorBottom);

    /**
     * for extra entries that need to show that arent a part of the main list
     * @param graphics GuiGraphicsExtractor
     * @param startIndex the next free visible index to continue the layout from
     * @param scissorTop scissor top position
     * @param scissorBottom scissor bottom position
     * @return the number of extra lines rendered so content height accounts for them
     */
    protected int renderExtraLines(GuiGraphicsExtractor graphics, int startIndex, int scissorTop, int scissorBottom) {
        return 0;
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        int itemCount = getItemCount();

        int scissorTop = backgroundTopLeft.y + 6;
        int scissorBottom = viewportHeight + (scissorTop + 8);

        graphics.enableScissor(backgroundTopLeft.x, scissorTop, backgroundTopLeft.x + imageWidth, scissorBottom);

        for (int index = 0; index < itemCount; index++) {
            int lineY = computeLineY(index);
            if (isLineVisible(lineY, scissorTop, scissorBottom)) {
                renderLine(graphics, index, lineY, scissorTop, scissorBottom);
            }
        }

        int extraLines = renderExtraLines(graphics, itemCount, scissorTop, scissorBottom);

        contentHeight = (itemCount + extraLines) * lineHeight;

        graphics.disableScissor();
    }

    /**
     * @param visibleIndex current visible index
     * @return y position of a line
     */
    protected int computeLineY(int visibleIndex) {
        return backgroundTopLeft.y + lineHeight + (visibleIndex * lineHeight) - (int) scrollOffset;
    }

    /**
     * @param lineY line y
     * @param scissorTop scissor top position
     * @param scissorBottom scissor bottom position
     * @return whether a line will be visible at the current moment
     */
    protected boolean isLineVisible(int lineY, int scissorTop, int scissorBottom) {
        return lineY + lineHeight >= scissorTop && lineY <= scissorBottom;
    }

    @Override
    protected void init() {
        super.init();

        int trackTop = backgroundTopLeft.y() + 6;
        int trackHeight = imageHeight - 12 - ScrollerWidget.SCROLLER_HEIGHT;
        int maxScroll = Math.max(0, contentHeight - viewportHeight);

        scroller = new ScrollerWidget(
                backgroundTopLeft.x() + 156,
                trackTop,
                trackHeight,
                progress -> scrollOffset = progress * Math.max(0, contentHeight - viewportHeight)
        );

        addRenderableWidget(scroller);

        scroller.setScrollProgress(maxScroll == 0 ? 0.0 : Mth.clamp(scrollOffset, 0, maxScroll) / maxScroll);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (contentHeight > viewportHeight) {
            int maxScroll = contentHeight - viewportHeight;

            scrollOffset = Mth.clamp(scrollOffset - scrollY * 10, 0, maxScroll);
            if (scroller != null) {
                scroller.setScrollProgress(maxScroll == 0 ? 0.0 : scrollOffset / maxScroll);
            }

            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }
}