package io.github.moosyu.gui.menus;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MODID);

    public static final Supplier<MenuType<TalismansMenu>> TALISMAN_MENU_TYPE = MENUS.register("talisman_menu_type", () ->
            new MenuType<>(TalismansMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    public static final Supplier<MenuType<StorageMenu>> STORAGE_MENU_TYPE = MENUS.register("storage_menu_type", () ->
            new MenuType<>(StorageMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    public static final Supplier<MenuType<ReforgeAnvilMenu>> REFORGE_ANVIL_MENU_TYPE = MENUS.register("reforge_anvil_menu_type", () ->
            new MenuType<>(ReforgeAnvilMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    public static final Supplier<MenuType<DrillAttachmentMenu>> DRILL_ATTACHMENT_MENU_TYPE = MENUS.register("drill_attachment_menu_type", () ->
            new MenuType<>(DrillAttachmentMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    public static final Supplier<MenuType<VendorMenu>> VENDOR_MENU_TYPE = MENUS.register("vendor_menu_type", () ->
            IMenuTypeExtension.create(VendorMenu::new)
    );

    public static final Supplier<MenuType<ForgeMenu>> FORGE_MENU_TYPE = MENUS.register("forge_menu_type", () ->
            new MenuType<>(ForgeMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );
}