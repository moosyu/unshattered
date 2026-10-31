package io.github.moosyu.gui.screens;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.gui.components.ScrollerWidget;
import io.github.moosyu.gui.components.tabs.TabBar;
import io.github.moosyu.gui.components.tabs.UnshatteredTabs;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

public class StatsScreen extends SimpleScreen {
    private static final int LINE_HEIGHT = 12;
    public static final int IMAGE_WIDTH = 176;
    public static final int IMAGE_HEIGHT = 166;

    private final int viewportHeight;
    private double scrollOffset = 0;
    private int contentHeight = 0;
    private ScrollerWidget scroller;
    private final TabBar tabBar = new TabBar(UnshatteredTabs.ALL, UnshatteredTabs.STATS, TabBar.GENERIC_OFFSET_TOP, TabBar.GENERIC_OFFSET_BOTTOM);
    public int leftPos;
    public int topPos;

    public StatsScreen() {
        super(Component.translatable("screen.unshattered.stats"), IMAGE_WIDTH, IMAGE_HEIGHT, "textures/gui/generic_scrollable.png");

        viewportHeight = imageHeight - 24;
    }

    @Override
    protected void init() {
        super.init();

        int maxScroll = Math.max(0, contentHeight - viewportHeight);
        scroller = new ScrollerWidget(backgroundTopLeft.x() + 156, backgroundTopLeft.y() + 6, imageHeight - 12 - ScrollerWidget.SCROLLER_HEIGHT, progress -> scrollOffset = progress * Math.max(0, contentHeight - viewportHeight));
        addRenderableWidget(scroller);

        scroller.setScrollProgress(maxScroll == 0 ? 0.0 : Mth.clamp(scrollOffset, 0, maxScroll) / maxScroll);

        leftPos = (width - imageWidth) / 2;
        topPos = (height - imageHeight) / 2;
        tabBar.init(leftPos, topPos, this::addRenderableWidget);
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(graphics, mouseX, mouseY, partialTick);

        UnshatteredAttributeValues[] attributeValues = UnshatteredAttributeValues.values();
        Player player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }

        int visibleCount = 0;
        for (UnshatteredAttributeValues attribute : attributeValues) {
            if (attribute.visible) {
                visibleCount++;
            }
        }

        contentHeight = (visibleCount + 1) * LINE_HEIGHT;
        int scissorTop = backgroundTopLeft.y + 6;
        int scissorBottom =  viewportHeight + (scissorTop + 8);

        graphics.enableScissor(backgroundTopLeft.x, scissorTop, backgroundTopLeft.x + imageWidth, scissorBottom);

        int visibleIndex = 0;
        for (UnshatteredAttributeValues currentAttribute : attributeValues) {
            if (currentAttribute.visible) {
                addAttributeToList(player,
                        graphics,
                        visibleIndex,
                        scissorTop,
                        scissorBottom,
                        currentAttribute.holder,
                        currentAttribute.symbol,
                        currentAttribute.id,
                        currentAttribute.percentage,
                        currentAttribute.color,
                        1
                );
                visibleIndex++;
            }
        }

        addAttributeToList(player,
                graphics,
                visibleIndex,
                scissorTop,
                scissorBottom,
                Attributes.MOVEMENT_SPEED,
                "✦",
                "speed",
                false,
                0xFFFFFFFF,
                10
        );

        graphics.disableScissor();

        tabBar.displayIcons(graphics);
    }

    /**
     * add an attribute modifier to the stats screen
     * @param player the player
     * @param graphics GuiGraphicsExtractor
     * @param visibleIndex current visible index (needs to be incremented after adding)
     * @param scissorTop scissor top
     * @param scissorBottom scissor bottom
     * @param attribute attribute holder
     * @param symbol string symbol for the attribute
     * @param currentAttributeId the attributes id (not key, just like a name as it adds the attribute.name.unshattered. for you)
     * @param isPercentage whether the attribute should be displayed as a percentage
     * @param attributeColour attribute colour
     * @param attributeModifier value to multiply the attribute's values by
     */
    private void addAttributeToList(Player player,
                                    GuiGraphicsExtractor graphics,
                                    int visibleIndex,
                                    int scissorTop,
                                    int scissorBottom,
                                    Holder<Attribute> attribute,
                                    String symbol,
                                    String currentAttributeId,
                                    boolean isPercentage,
                                    int attributeColour,
                                    int attributeModifier
    ) {
        int lineY = backgroundTopLeft.y + LINE_HEIGHT + (visibleIndex * LINE_HEIGHT) - (int) scrollOffset;

        if (lineY + LINE_HEIGHT >= scissorTop && lineY <= scissorBottom) {
            double attributeBaseValue = player.getAttributeBaseValue(attribute) * attributeModifier;
            double attributeValue = player.getAttributeValue(attribute) * attributeModifier;
            String attributeBaseValueFormatted = UnshatteredUtils.oneDecimalFormat.format(attributeBaseValue);

            graphics.text(font,
                    symbol
                            + " "
                            + Component.translatable("attribute.name.unshattered." + currentAttributeId).getString()
                            + ": "
                            + (attributeValue == attributeBaseValue ? attributeBaseValueFormatted : attributeBaseValueFormatted + " (+" + UnshatteredUtils.oneDecimalFormat.format(attributeValue - attributeBaseValue) + ")")
                            + (isPercentage ? "%" : ""),
                    backgroundTopLeft.x + 9,
                    lineY,
                    attributeColour
            );
        }
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
