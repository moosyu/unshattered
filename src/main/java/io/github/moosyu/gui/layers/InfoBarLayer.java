package io.github.moosyu.gui.layers;

import com.lowdragmc.lowdraglib2.gui.hud.ModularHudLayer;
import com.lowdragmc.lowdraglib2.gui.sync.bindings.impl.SupplierDataSource;
import com.lowdragmc.lowdraglib2.gui.texture.ColorRectTexture;
import com.lowdragmc.lowdraglib2.gui.ui.ModularUI;
import com.lowdragmc.lowdraglib2.gui.ui.UI;
import com.lowdragmc.lowdraglib2.gui.ui.UIElement;
import com.lowdragmc.lowdraglib2.gui.ui.elements.Label;
import dev.vfyjxf.taffy.style.AlignItems;
import dev.vfyjxf.taffy.style.FlexDirection;
import io.github.moosyu.data.attachments.PlayerCurrencyAttachment;
import io.github.moosyu.data.attachments.PlayerRegionAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.regions.Region;
import io.github.moosyu.data.regions.TemperatureTypes;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.include.com.google.common.base.Suppliers;

import java.util.function.Supplier;

public class InfoBarLayer implements ModularHudLayer {
    private static final Supplier<ModularUI> MODULAR_UI = Suppliers.memoize(() -> ModularUI.of(UI.of(createSidebarLayer())));

    @Override
    public @Nullable ModularUI getModularUI() {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.options.hideGui) return null;

        return MODULAR_UI.get();
    }

    public static UIElement createSidebarLayer() {
        UIElement root = new UIElement().layout(layout -> layout.widthPercent(100).heightPercent(100));
        Player player = Minecraft.getInstance().player;

        if (player == null) return root;

        UIElement sidebarBox = new UIElement()
                .layout(layout -> layout
                        .flexDirection(FlexDirection.ROW)
                        .widthPercent(100)
                        .height(20)
                        .gapAll(8)
                        .alignItems(AlignItems.CENTER)
                        .paddingLeft(6)
                        .paddingRight(6)
                )
                .style(style -> style.background(new ColorRectTexture(0x64000000)));

        Label purseLabel = new Label();
        purseLabel.textStyle(style -> style.adaptiveWidth(true).textColor(0xFFF9A604));
        purseLabel.bindDataSource(SupplierDataSource.of(() -> {
            PlayerCurrencyAttachment currency = player.getData(UnshatteredAttachments.PLAYER_CURRENCY.get());
            return Component.literal("Purse: " + String.format("%,d", currency.getCoins()));
        }));

        Label regionLabel = new Label();
        regionLabel.textStyle(style -> style.adaptiveWidth(true));
        regionLabel.bindDataSource(SupplierDataSource.of(() -> {
            PlayerRegionAttachment regionAttachment = player.getData(UnshatteredAttachments.PLAYER_REGION.get());
            Region region = Region.getRegion(regionAttachment.regionKey(), player);
            return Component.literal("⏣ ").withColor(0xFFAAAAAA).append(Component.translatable(Region.getRegionTranslationKey(regionAttachment.regionKey())).withColor(region.colour()));
        }));

        Label temperatureLabel = new Label();
        temperatureLabel.textStyle(style -> style.adaptiveWidth(true));
        temperatureLabel.bindDataSource(SupplierDataSource.of(() -> {
            float playerTemperature = player.getData(UnshatteredAttachments.PLAYER_TEMPERATURE.get());
            TemperatureTypes temperatureTypes;

            if (playerTemperature >= TemperatureTypes.HIGH_TEMP.getValue()) {
                temperatureTypes = TemperatureTypes.HIGH_TEMP;
            } else if (playerTemperature <= TemperatureTypes.LOW_TEMP.getValue()) {
                temperatureTypes = TemperatureTypes.LOW_TEMP;
            } else {
                temperatureTypes = TemperatureTypes.BASE_TEMP;
            }
            return Component.literal(temperatureTypes.getSymbol() + " ")
                    .append(Component.literal(UnshatteredUtils.oneDecimalFormat.format(playerTemperature))).withColor(temperatureTypes.getColour());
        }));

        sidebarBox.addChildren(purseLabel, regionLabel, temperatureLabel);
        root.addChild(sidebarBox);
        return root;
    }
}