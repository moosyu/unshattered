package io.github.moosyu.gui.screens.states;

import net.minecraft.resources.Identifier;

public class TabSwitcherState {
    private Tabs selectedTab;

    public enum Tabs {
        SKILLS(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        STATS(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        QUESTS(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        CRAFTING(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        COLLECTIONS(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        PETS(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        STORAGE(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        WARDROBE(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        BANK(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        WARP(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        BAGS(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png"));

        private final Identifier iconTexture;

        Tabs(Identifier iconTexture) {
            this.iconTexture = iconTexture;
        }
    }

    public TabSwitcherState() {
        this.selectedTab = Tabs.STATS;
    }

    public Tabs getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(Tabs selectedTab) {
        this.selectedTab = selectedTab;
    }
}
