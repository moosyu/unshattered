package io.github.moosyu;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.items.UnshatteredItems.*;

public class UnshatteredCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    static {
        CREATIVE_MODE_TABS.register("unshattered_items_tab",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("item_group.unshattered.items"))
                        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                        .icon(() -> BAT_THE_FISH.get().getDefaultInstance())
                        .displayItems((_, output) -> {
                            output.accept(BAT_THE_FISH);
                            output.accept(CENTURY_THE_FISH);
                            output.accept(CHILL_THE_FISH);
                            output.accept(CLUNK_THE_FISH);
                            output.accept(DIAMOND_THE_FISH);
                            output.accept(DUST_THE_FISH);
                            output.accept(EGG_THE_FISH);
                            output.accept(EON_THE_FISH);
                            output.accept(FLAKE_THE_FISH);
                            output.accept(EXPERIMENT_THE_FISH);
                            output.accept(FOSSIL_THE_FISH);
                            output.accept(GABAGOOL_THE_FISH);
                            output.accept(GIFT_THE_FISH);
                            output.accept(HERRING_THE_FISH);
                            output.accept(NOPE_THE_FISH);
                            output.accept(OOPS_THE_FISH);
                            output.accept(PARTY_THE_FISH);
                            output.accept(ROCK_THE_FISH);
                            output.accept(SHRIMP_THE_FISH);
                            output.accept(SKELETON_THE_FISH);
                            output.accept(SPOOK_THE_FISH);
                            output.accept(STEW_THE_FISH);
                            output.accept(SWAMP_THE_FISH);
                            output.accept(ZOOP_THE_FISH);
                            output.accept(BEDROCK);
                            output.accept(CAKE_SOUL);
                        }).build()
        );

        CREATIVE_MODE_TABS.register("unshattered_decorative_blocks_tab",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("item_group.unshattered.decorative_blocks"))
                        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                        .icon(() -> FIG_LOG.get().getDefaultInstance())
                        .displayItems((_, output) -> {
                            output.accept(FIG_LOG);
                            output.accept(ROCK_TALKABLE_BLOCK);
                        }).build()
        );

        CREATIVE_MODE_TABS.register("unshattered_breakable_blocks_tab",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("item_group.unshattered.breakable_blocks"))
                        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                        .icon(() -> BREAKABLE_COBBLESTONE.get().getDefaultInstance())
                        .displayItems((_, output) -> {
                            output.accept(BREAKABLE_STONE);
                            output.accept(BREAKABLE_COBBLESTONE);
                            output.accept(BREAKABLE_FIG_LOG);
                            output.accept(BREAKABLE_COAL_ORE);
                            output.accept(BREAKABLE_IRON_ORE);
                            output.accept(BREAKABLE_COPPER_ORE);
                            output.accept(BREAKABLE_GOLD_ORE);
                            output.accept(BREAKABLE_REDSTONE_ORE);
                            output.accept(BREAKABLE_LAPIS_ORE);
                            output.accept(BREAKABLE_EMERALD_ORE);
                            output.accept(BREAKABLE_DIAMOND_ORE);
                            output.accept(PURE_DIAMOND);
                            output.accept(BREAKABLE_OBSIDIAN);
                            output.accept(BREAKABLE_WHEAT);
                            output.accept(BREAKABLE_SOFT_MITHRIL_BLOCK);
                            output.accept(BREAKABLE_HARD_MITHRIL_BLOCK);
                            output.accept(BREAKABLE_COBBLED_MITHRIL_BLOCK);
                            output.accept(BREAKABLE_OAK_LOG);
                            output.accept(BREAKABLE_BIRCH_LOG);
                            output.accept(BREAKABLE_SPRUCE_LOG);
                            output.accept(BREAKABLE_JUNGLE_LOG);
                            output.accept(BREAKABLE_ACACIA_LOG);
                            output.accept(BREAKABLE_DARK_OAK_LOG);
                            output.accept(BREAKABLE_ICE);
                            output.accept(BREAKABLE_TITANIUM);
                            output.accept(BREAKABLE_POTATO);
                            output.accept(BREAKABLE_CARROT);
                            output.accept(BREAKABLE_MELON);
                            output.accept(BREAKABLE_SUGAR_CANE);
                            output.accept(BREAKABLE_CACTUS);
                            output.accept(BREAKABLE_SAND);
                            output.accept(BREAKABLE_BROWN_MUSHROOM_BLOCK);
                            output.accept(BREAKABLE_RED_MUSHROOM_BLOCK);
                            output.accept(BREAKABLE_BROWN_MUSHROOM);
                            output.accept(BREAKABLE_RED_MUSHROOM);
                            output.accept(BREAKABLE_COCOA_BEAN);
                            output.accept(BREAKABLE_PUMPKIN);
                            output.accept(GIANT_POTATO);
                            output.accept(GIANT_CARROT);
                        }).build()
        );

        CREATIVE_MODE_TABS.register("unshattered_resources_tab",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("item_group.unshattered.unshattered_resources"))
                        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                        .icon(() -> ENCHANTED_FIG_LOG.get().getDefaultInstance())
                        .displayItems((_, output) -> {
                            output.accept(ENCHANTED_FIG_LOG);
                            output.accept(ENCHANTED_ROTTEN_FLESH);
                            output.accept(ZOMBIE_HEART);
                            output.accept(GOLDEN_POWDER);
                            output.accept(ENCHANTED_GOLD_INGOT);
                            output.accept(ENCHANTED_GOLD_BLOCK);
                            output.accept(HEALING_TISSUE);
                            output.accept(ENCHANTED_DIAMOND);
                            output.accept(ENCHANTED_DIAMOND_BLOCK);
                            output.accept(ENCHANTED_EMERALD);
                            output.accept(ENCHANTED_EMERALD_BLOCK);
                            output.accept(ENCHANTED_IRON);
                            output.accept(ENCHANTED_IRON_BLOCK);
                            output.accept(ENCHANTED_COAL);
                            output.accept(ENCHANTED_COAL_BLOCK);
                            output.accept(ENCHANTED_LAPIS);
                            output.accept(ENCHANTED_LAPIS_BLOCK);
                            output.accept(ENCHANTED_REDSTONE);
                            output.accept(ENCHANTED_REDSTONE_BLOCK);
                            output.accept(MITHRIL);
                            output.accept(ENCHANTED_MITHRIL);
                            output.accept(ENCHANTED_OAK_LOG);
                            output.accept(ENCHANTED_BIRCH_LOG);
                            output.accept(ENCHANTED_SPRUCE_LOG);
                            output.accept(ENCHANTED_JUNGLE_LOG);
                            output.accept(ENCHANTED_ACACIA_LOG);
                            output.accept(ENCHANTED_DARK_OAK_LOG);
                            output.accept(ENCHANTED_POISONOUS_POTATO);
                            output.accept(ENCHANTED_BONE);
                            output.accept(ENCHANTED_BONE_BLOCK);
                            output.accept(ENCHANTED_COBBLESTONE);
                            output.accept(ENCHANTED_FLINT);
                            output.accept(ENCHANTED_STRING);
                            output.accept(ENCHANTED_OBSIDIAN);
                            output.accept(ENCHANTED_CLAY_BALL);
                            output.accept(ENCHANTED_CLAY_BLOCK);
                            output.accept(ENCHANTED_INK_SAC);
                            output.accept(ENCHANTED_LILY_PAD);
                            output.accept(ENCHANTED_PRISMARINE_SHARD);
                            output.accept(ENCHANTED_PRISMARINE_CRYSTALS);
                            output.accept(ENCHANTED_PUFFERFISH);
                            output.accept(ENCHANTED_COD);
                            output.accept(ENCHANTED_COOKED_COD);
                            output.accept(ENCHANTED_SALMON);
                            output.accept(ENCHANTED_COOKED_SALMON);
                            output.accept(ENCHANTED_SPONGE);
                            output.accept(ENCHANTED_WET_SPONGE);
                            output.accept(ENCHANTED_TROPICAL_FISH);
                            output.accept(ENCHANTED_HONEYCOMB);
                            output.accept(ENCHANTED_ICE);
                            output.accept(ENCHANTED_PACKED_ICE);
                            output.accept(TITANIUM);
                            output.accept(ENCHANTED_TITANIUM);
                            output.accept(ENCHANTED_BLAZE_POWDER);
                            output.accept(ENCHANTED_BLAZE_ROD);
                            output.accept(ENCHANTED_WOOL);
                            output.accept(REFINED_MITHRIL);
                            output.accept(TREASURITE);
                            output.accept(REFINED_DIAMOND);
                            output.accept(GLACITE_JEWEL);
                            output.accept(FUEL_CANISTER);
                            output.accept(GOLDEN_PLATE);
                            output.accept(REFINED_TITANIUM);
                            output.accept(MITHRIL_PLATE);
                            output.accept(DRILL_MOTOR);
                            output.accept(GOBLIN_EGG);
                            output.accept(OIL_BARREL);
                            output.accept(ENCHANTED_POPPY);
                            output.accept(ENCHANTED_DANDELION);
                            output.accept(BIOFUEL);
                            output.accept(ENCHANTED_WHEAT);
                            output.accept(ENCHANTED_POTATO);
                            output.accept(ENCHANTED_CARROT);
                            output.accept(ENCHANTED_MELON_SLICE);
                            output.accept(ENCHANTED_MELON);
                            output.accept(ENCHANTED_SUGAR);
                            output.accept(ENCHANTED_SUGAR_CANE);
                            output.accept(ENCHANTED_CACTUS);
                            output.accept(ENCHANTED_CACTUS_GREEN);
                            output.accept(ENCHANTED_SAND);
                            output.accept(ENCHANTED_BROWN_MUSHROOM_BLOCK);
                            output.accept(ENCHANTED_RED_MUSHROOM_BLOCK);
                            output.accept(ENCHANTED_BROWN_MUSHROOM);
                            output.accept(ENCHANTED_RED_MUSHROOM);
                            output.accept(ENCHANTED_COOKIE);
                            output.accept(ENCHANTED_COCOA_BEANS);
                            output.accept(ENCHANTED_PUMPKIN);
                            output.accept(ENCHANTED_BAKED_POTATO);
                            output.accept(ENCHANTED_HAY_BALE);
                            output.accept(ENCHANTED_GOLDEN_CARROT);
                            output.accept(POLISHED_PUMPKIN);
                            output.accept(STRETCHING_STICK);
                            output.accept(FIGSTONE);
                            output.accept(PLASMA);
                            output.accept(BEJEWELED_HANDLE);
                            output.accept(GREEN_CANDY);
                            output.accept(PURPLE_CANDY);
                            output.accept(ENCHANTED_GUNPOWDER);
                            output.accept(ENCHANTED_SLIME_BALL);
                            output.accept(ENCHANTED_SLIME_BLOCK);
                        }).build()
        );

        CREATIVE_MODE_TABS.register("unshattered_armour_tab",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("item_group.unshattered.armour"))
                        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                        .icon(() -> LEAFLET_CHESTPLATE.get().getDefaultInstance())
                        .displayItems((_, output) -> {
                            output.accept(LEAFLET_HELMET);
                            output.accept(LEAFLET_CHESTPLATE);
                            output.accept(LEAFLET_LEGGINGS);
                            output.accept(LEAFLET_BOOTS);
                            output.accept(GLOW_SQUID_BOOTS);
                            output.accept(SKELETON_HAT);
                            output.accept(LAPIS_HELMET);
                            output.accept(LAPIS_CHESTPLATE);
                            output.accept(LAPIS_LEGGINGS);
                            output.accept(LAPIS_BOOTS);
                        }).build()
        );

        CREATIVE_MODE_TABS.register("unshattered_tools_tab",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("item_group.unshattered.tools"))
                        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                        .icon(() -> TREECAPITATOR.get().getDefaultInstance())
                        .displayItems((_, output) -> {
                            output.accept(TREECAPITATOR);
                            output.accept(SPRUCE_AXE);
                            output.accept(SERIOUSLY_DAMAGED_AXE);
                            output.accept(DECENT_AXE);
                            output.accept(FIG_HEW);
                            output.accept(FIGSTONE_SPLITTER);
                            output.accept(CHALLENGING_ROD);
                            output.accept(FISHING_ROD);
                            output.accept(BROKEN_MITHRIL_PICKAXE);
                            output.accept(BANDAGED_MITHRIL_PICKAXE);
                            output.accept(MITHRIL_PICKAXE);
                            output.accept(RUSTED_TITANIUM_PICKAXE);
                            output.accept(TITANIUM_PICKAXE);
                            output.accept(LAPIS_PICKAXE);
                            output.accept(ZOMBIE_PICKAXE);
                            output.accept(PROMISING_PICKAXE);
                            output.accept(PROMISING_AXE);
                            output.accept(MITHRIL_DRILL_SX_R226);
                            output.accept(MITHRIL_DRILL_SX_R326);
                            output.accept(TITANIUM_DRILL_DR_X355);
                            output.accept(TITANIUM_DRILL_DR_X455);
                            output.accept(TITANIUM_DRILL_DR_X555);
                            output.accept(TITANIUM_DRILL_DR_X655);
                            output.accept(MITHRIL_INFUSED_FUEL_TANK);
                            output.accept(MITHRIL_PLATED_DRILL_ENGINE);
                            output.accept(FRIED_GOBLIN_EGG);
                            output.accept(ROOKIE_HOE);
                        }).build()
        );

        CREATIVE_MODE_TABS.register("unshattered_weapons_tab",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("item_group.unshattered.weapons"))
                        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                        .icon(() -> MERCENARY_AXE.get().getDefaultInstance())
                        .displayItems((_, output) -> {
                            output.accept(MERCENARY_AXE);
                            output.accept(ROGUE_SWORD);
                            output.accept(SQUIRE_SWORD);
                            output.accept(UNDEAD_SWORD);
                            output.accept(ZOMBIE_SWORD);
                            output.accept(ORNATE_ZOMBIE_SWORD);
                            output.accept(FLORID_ZOMBIE_SWORD);
                            output.accept(RUSTY_CLEAVER);
                            output.accept(GOLDEN_CLEAVER);
                            output.accept(SUPER_CLEAVER);
                            output.accept(HYPER_CLEAVER);
                            output.accept(GIANT_CLEAVER);
                            output.accept(IRON_DAGGER);
                            output.accept(EMERALD_DAGGER);
                            output.accept(ARTISANAL_SHORTBOW);
                        }).build()
        );

        CREATIVE_MODE_TABS.register("unshattered_talismans_tab",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("item_group.unshattered.talismans"))
                        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                        .icon(() -> BAT_TALISMAN.get().getDefaultInstance())
                        .displayItems((_, output) -> {
                            output.accept(BAT_TALISMAN);
                            output.accept(COINS_TALISMAN);
                            output.accept(RAINBOW_YARN_TALISMAN);
                            output.accept(DWARF_TURTLE_SHELL);
                            output.accept(PIGGY_BANK_TALISMAN);
                            output.accept(HASTE_RING);
                            output.accept(HONEYCOMB_RING);
                            output.accept(BLOOD_CHALICE);
                            output.accept(INFINITE_QUIVER);
                            output.accept(FIRE_TALISMAN);
                            output.accept(BAT_ARTIFACT);
                        }).build()
        );

        CREATIVE_MODE_TABS.register("unshattered_sacks_tab",
                () -> CreativeModeTab.builder()
                        .title(Component.translatable("item_group.unshattered.sacks"))
                        .withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
                        .icon(Items.BUNDLE::getDefaultInstance)
                        .displayItems((_, output) -> {
                            output.accept(BEGINNER_AGRONOMY_SACK);
                        }).build()
        );
    }
}
