package io.github.moosyu.gui.components;

import com.mojang.blaze3d.platform.cursor.CursorTypes;
import io.github.moosyu.data.ShopItem;
import io.github.moosyu.packets.AttemptPurchasePacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.jspecify.annotations.NonNull;

import java.util.function.Consumer;

/**
 * for items that are displayed in shop menus
 */
public class ShopItemWidget extends AbstractWidget {
    private final ShopItem shopItem;
    private final ItemStack itemStack;
    private final Consumer<ShopItem> onRightClick;

    public ShopItemWidget(int x, int y, int width, int height, ShopItem shopItem, Consumer<ShopItem> onRightClick) {
        super(x, y, width, height, Component.translatable("widget.unshattered.narration.shop_item"));

        this.shopItem = shopItem;
        this.onRightClick = onRightClick;
        itemStack = shopItem.item().value().getDefaultInstance();
    }

    @Override
    protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        graphics.item(itemStack, getX(), getY());

        if (isHovered) {
            graphics.requestCursor(CursorTypes.POINTING_HAND);
        }
    }

    @Override
    protected void updateWidgetNarration(@NonNull NarrationElementOutput narrationElementOutput) {
        defaultButtonNarrationText(narrationElementOutput);
    }

    @Override
    protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
        return buttonInfo.button() == UnshatteredUtils.MouseButton.LEFT.getButton() || buttonInfo.button() == UnshatteredUtils.MouseButton.RIGHT.getButton();
    }

    @Override
    public void onClick(@NonNull MouseButtonEvent event, boolean doubleClick) {
        if (event.button() == UnshatteredUtils.MouseButton.LEFT.getButton()) {
            ClientPacketDistributor.sendToServer(new AttemptPurchasePacket(itemStack, shopItem.price(), shopItem.itemTradeRequirements()));
        } else if (shopItem.sellMultiple()) {
            onRightClick.accept(shopItem);
        }
    }
}
