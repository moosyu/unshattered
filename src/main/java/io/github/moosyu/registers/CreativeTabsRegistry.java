package io.github.moosyu.registers;

import
net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.NNO.MODID;
import static io.github.moosyu.registers.ItemsRegistry.*;
public class CreativeTabsRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NNO_ITEMS_TAB = CREATIVE_MODE_TABS.register("nno_items_tab",
        () -> CreativeModeTab.builder()
        .title(Component.translatable("item_group.nno.items"))
        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
        .icon(() -> BAT_THE_FISH.get().getDefaultInstance())
        .displayItems((parameters, output) -> {
            output.accept(BAT_THE_FISH.get());
            output.accept(CENTURY_THE_FISH.get());
            output.accept(CHILL_THE_FISH.get());
            output.accept(CLUNK_THE_FISH.get());
            output.accept(DIAMOND_THE_FISH.get());
            output.accept(DUST_THE_FISH.get());
            output.accept(EGG_THE_FISH.get());
            output.accept(EON_THE_FISH.get());
            output.accept(FLAKE_THE_FISH.get());
            output.accept(EXPERIMENT_THE_FISH.get());
            output.accept(FOSSIL_THE_FISH.get());
            output.accept(GABAGOOL_THE_FISH.get());
            output.accept(GIFT_THE_FISH.get());
            output.accept(HERRING_THE_FISH.get());
            output.accept(NOPE_THE_FISH.get());
            output.accept(OOPS_THE_FISH.get());
            output.accept(PARTY_THE_FISH.get());
            output.accept(ROCK_THE_FISH.get());
            output.accept(SHRIMP_THE_FISH.get());
            output.accept(SKELETON_THE_FISH.get());
            output.accept(SPOOK_THE_FISH.get());
            output.accept(STEW_THE_FISH.get());
            output.accept(SWAMP_THE_FISH.get());
            output.accept(ZOOP_THE_FISH.get());
        }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NNO_ARMOR_TAB = CREATIVE_MODE_TABS.register("nno_armor_tab",
        () -> CreativeModeTab.builder()
        .title(Component.translatable("item_group.nno.armor"))
        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
        .icon(() -> LEAFLET_CHESTPLATE.get().getDefaultInstance())
        .displayItems((parameters, output) -> {
            output.accept(LEAFLET_HELMET.get());
            output.accept(LEAFLET_CHESTPLATE.get());
            output.accept(LEAFLET_LEGGINGS.get());
            output.accept(LEAFLET_BOOTS.get());
        }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NNO_TOOLS_TAB = CREATIVE_MODE_TABS.register("nno_tools_tab",
        () -> CreativeModeTab.builder()
        .title(Component.translatable("item_group.nno.tools"))
        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
        .icon(() -> TREECAPITATOR.get().getDefaultInstance())
        .displayItems((parameters, output) -> {
            output.accept(TREECAPITATOR.get());
            output.accept(SPRUCE_AXE.get());
            output.accept(SERIOUSLY_DAMAGED_AXE.get());
            output.accept(DECENT_AXE.get());
            output.accept(FIG_HEW.get());
            output.accept(FIGSTONE_SPLITTER.get());
        }).build());

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> NNO_WEAPONS_TAB = CREATIVE_MODE_TABS.register("nno_weapons_tab",
        () -> CreativeModeTab.builder()
        .title(Component.translatable("item_group.nno.weapons"))
        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
        .icon(() -> MERCENARY_AXE.get().getDefaultInstance())
        .displayItems((parameters, output) -> {
            output.accept(MERCENARY_AXE.get());
        }).build());
}
