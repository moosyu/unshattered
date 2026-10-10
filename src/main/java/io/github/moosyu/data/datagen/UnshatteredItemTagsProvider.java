package io.github.moosyu.data.datagen;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.items.UnshatteredItems.*;

public class UnshatteredItemTagsProvider extends ItemTagsProvider {
    public UnshatteredItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, MODID);
    }

    public static final TagKey<Item> HAS_DESCRIPTION = ItemTags.create(UnshatteredUtils.getUnshatteredIdentifier("has_description"));

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(ItemTags.HEAD_ARMOR)
                .add(LEAFLET_HELMET.get())
                .add(SKELETON_HAT.get());

        tag(ItemTags.CHEST_ARMOR).add(LEAFLET_CHESTPLATE.get());

        tag(ItemTags.LEG_ARMOR).add(LEAFLET_LEGGINGS.get());

        tag(ItemTags.FOOT_ARMOR)
                .add(LEAFLET_BOOTS.get())
                .add(GLOW_SQUID_BOOTS.get());

        tag(ItemTags.AXES)
                .remove(Items.WOODEN_AXE)
                .remove(Items.GOLDEN_AXE)
                .remove(Items.STONE_AXE)
                .remove(Items.COPPER_AXE)
                .remove(Items.IRON_AXE)
                .remove(Items.DIAMOND_AXE)
                .remove(Items.NETHERITE_AXE)
                .add(TREECAPITATOR.get())
                .add(SPRUCE_AXE.get())
                .add(SERIOUSLY_DAMAGED_AXE.get())
                .add(DECENT_AXE.get())
                .add(FIG_HEW.get())
                .add(FIGSTONE_SPLITTER.get())
                .add(PROMISING_AXE.get());

        tag(ItemTags.PICKAXES)
                .add(BROKEN_MITHRIL_PICKAXE.get())
                .add(RUSTED_TITANIUM_PICKAXE.get())
                .add(TITANIUM_PICKAXE.get())
                .add(BANDAGED_MITHRIL_PICKAXE.get())
                .add(MITHRIL_PICKAXE.get())
                .add(LAPIS_PICKAXE.get())
                .add(ZOMBIE_PICKAXE.get())
                .add(PROMISING_PICKAXE.get())
                .add(MITHRIL_DRILL_SX_R226.get())
                .add(MITHRIL_DRILL_SX_R326.get())
                .add(TITANIUM_DRILL_DR_X355.get())
                .add(TITANIUM_DRILL_DR_X455.get())
                .add(TITANIUM_DRILL_DR_X555.get())
                .add(TITANIUM_DRILL_DR_X655.get());

        tag(ItemTags.SWORDS)
                .add(ROGUE_SWORD.get())
                .add(UNDEAD_SWORD.get())
                .add(SQUIRE_SWORD.get())
                .add(ZOMBIE_SWORD.get())
                .add(ORNATE_ZOMBIE_SWORD.get())
                .add(FLORID_ZOMBIE_SWORD.get());

        tag(ItemTags.SHARP_WEAPON_ENCHANTABLE)
                .add(IRON_DAGGER.get())
                .add(EMERALD_DAGGER.get())
                .add(RUSTY_CLEAVER.get())
                .add(GOLDEN_CLEAVER.get())
                .add(SUPER_CLEAVER.get())
                .add(HYPER_CLEAVER.get())
                .add(GIANT_CLEAVER.get())
                .add(ZOMBIE_SWORD.get())
                .add(ORNATE_ZOMBIE_SWORD.get())
                .add(FLORID_ZOMBIE_SWORD.get())
                .add(ROGUE_SWORD.get())
                .add(SQUIRE_SWORD.get())
                .add(MERCENARY_AXE.get());

        tag(HAS_DESCRIPTION)
                .add(MERCENARY_AXE.get())
                .add(SPRUCE_AXE.get())
                .add(SERIOUSLY_DAMAGED_AXE.get())
                .add(DECENT_AXE.get())
                .add(FIG_HEW.get())
                .add(FIGSTONE_SPLITTER.get())
                .add(BAT_THE_FISH.get())
                .add(CHILL_THE_FISH.get())
                .add(CLUNK_THE_FISH.get())
                .add(DIAMOND_THE_FISH.get())
                .add(DUST_THE_FISH.get())
                .add(EGG_THE_FISH.get())
                .add(EON_THE_FISH.get())
                .add(FLAKE_THE_FISH.get())
                .add(EXPERIMENT_THE_FISH.get())
                .add(FOSSIL_THE_FISH.get())
                .add(GABAGOOL_THE_FISH.get())
                .add(GIFT_THE_FISH.get())
                .add(HERRING_THE_FISH.get())
                .add(NOPE_THE_FISH.get())
                .add(OOPS_THE_FISH.get())
                .add(PARTY_THE_FISH.get())
                .add(ROCK_THE_FISH.get())
                .add(SHRIMP_THE_FISH.get())
                .add(SKELETON_THE_FISH.get())
                .add(SPOOK_THE_FISH.get())
                .add(STEW_THE_FISH.get())
                .add(SWAMP_THE_FISH.get())
                .add(ZOOP_THE_FISH.get())
                .add(BEDROCK.get())
                .add(CAKE_SOUL.get())
                .add(CHALLENGING_ROD.get())
                .add(FISHING_ROD.get())
                .add(FIG_LOG.get())
                .add(ROGUE_SWORD.get())
                .add(SQUIRE_SWORD.get())
                .add(UNDEAD_SWORD.get())
                .add(ZOMBIE_SWORD.get())
                .add(BROKEN_MITHRIL_PICKAXE.get())
                .add(BANDAGED_MITHRIL_PICKAXE.get())
                .add(MITHRIL_PICKAXE.get())
                .add(RUSTED_TITANIUM_PICKAXE.get())
                .add(TITANIUM_PICKAXE.get())
                .add(LAPIS_PICKAXE.get())
                .add(ORNATE_ZOMBIE_SWORD.get())
                .add(FLORID_ZOMBIE_SWORD.get())
                .add(GOLDEN_CLEAVER.get())
                .add(SUPER_CLEAVER.get())
                .add(HYPER_CLEAVER.get())
                .add(GIANT_CLEAVER.get())
                .add(RUSTY_CLEAVER.get())
                .add(BAT_TALISMAN.get())
                .add(IRON_DAGGER.get())
                .add(EMERALD_DAGGER.get())
                .add(COINS_TALISMAN.get())
                .add(GOBLIN_EGG.get())
                .add(OIL_BARREL.get())
                .add(TITANIUM.get())
                .add(BLOOD_CHALICE.get())
                .add(MITHRIL_INFUSED_FUEL_TANK.get())
                .add(FRIED_GOBLIN_EGG.get())
                .add(PROMISING_PICKAXE.get())
                .add(PROMISING_AXE.get())
                .add(RAINBOW_YARN_TALISMAN.get())
                .add(PIGGY_BANK_TALISMAN.get())
                .add(INFINITE_QUIVER.get())
                .add(HONEYCOMB_RING.get())
                .add(HASTE_RING.get())
                .add(FIRE_TALISMAN.get())
                .add(ARTISANAL_SHORTBOW.get())
                .add(ZOMBIE_PICKAXE.get())
                .add(MITHRIL_PLATED_DRILL_ENGINE.get())
                .add(MITHRIL_DRILL_SX_R226.get())
                .add(MITHRIL_DRILL_SX_R326.get())
                .add(TITANIUM_DRILL_DR_X355.get())
                .add(TITANIUM_DRILL_DR_X455.get())
                .add(TITANIUM_DRILL_DR_X555.get())
                .add(TITANIUM_DRILL_DR_X655.get())
                .add(CENTURY_THE_FISH.get())
                .add(ROOKIE_HOE.get());
    }
}
