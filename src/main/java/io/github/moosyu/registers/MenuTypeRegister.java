package io.github.moosyu.registers;

import io.github.moosyu.menus.CraftingMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.Unshattered.MODID;

public class MenuTypeRegister {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Registries.MENU, MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<CraftingMenu>> CRAFTING_MENU = MENUS.register("crafting_menu", () -> IMenuTypeExtension.create(CraftingMenu::new));
}
