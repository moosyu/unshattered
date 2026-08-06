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
import io.github.moosyu.attachments.PlayerCurrencyAttachment;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.data.regions.Region;
import io.github.moosyu.events.DataPackRegistryHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.include.com.google.common.base.Suppliers;

import java.util.function.Supplier;

public class SidebarLayer implements ModularHudLayer {
    private static final Supplier<ModularUI> MODULAR_UI = Suppliers.memoize(() -> ModularUI.of(UI.of(createSidebarLayer())));

    @Override
    public @Nullable ModularUI getModularUI() {
        return MODULAR_UI.get();
    }

    public static UIElement createSidebarLayer() {
        UIElement root = new UIElement()
                .layout(l -> l.widthPercent(100).heightPercent(100));

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

        Label regionLabel = new Label();
        regionLabel.textStyle(style -> style.adaptiveWidth(true));
        regionLabel.bindDataSource(SupplierDataSource.of(() -> {
            Player player = Minecraft.getInstance().player;
            if (player == null) return Component.literal("");
            ResourceKey<Region> regionResourceKey = player.getData(UnshatteredAttachments.PLAYER_REGION.get());
            Region region = Region.getRegion(regionResourceKey, player);
            return Component.literal("⏣ ").withColor(0xFFAAAAAA).append(Component.translatable(Region.getRegionTranslationKey(regionResourceKey)).withColor(region.colour()));
        }));

        Label purseLabel = new Label();
        purseLabel.textStyle(style -> style.adaptiveWidth(true).textColor(0xFFFFFF55));
        purseLabel.bindDataSource(SupplierDataSource.of(() -> {
            Player player = Minecraft.getInstance().player;
            if (player == null) return Component.literal("Purse: -");
            PlayerCurrencyAttachment currency = player.getData(UnshatteredAttachments.PLAYER_CURRENCY.get());
            return Component.literal("Purse: " + String.format("%,d", currency.getCoins()));
        }));

        sidebarBox.addChildren(regionLabel, purseLabel);
        root.addChild(sidebarBox);
        return root;
    }
}