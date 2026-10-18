package io.github.moosyu.gui.components.tabs;

import io.github.moosyu.gui.screens.SkillsScreen;
import io.github.moosyu.gui.screens.StatsScreen;
import io.github.moosyu.packets.OpenCraftingPacket;
import io.github.moosyu.packets.OpenSackOfSacksPacket;
import io.github.moosyu.packets.OpenStoragePacket;
import io.github.moosyu.packets.OpenTalismanBagPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

import java.util.function.Supplier;

public class UnshatteredTabs {
    public record UnshatteredTab(String id, TabIcon icon, Component tooltip, Runnable onClick) {}

    public static final UnshatteredTab INVENTORY = new UnshatteredTab("inventory",
            TabIcon.item(Items.IRON_CHESTPLATE),
            Component.translatable("container.inventory"),
            () -> {
                Minecraft minecraft = Minecraft.getInstance();
                if (minecraft.player != null) {
                    closeContainerKeepScreen();
                    minecraft.setScreen(new InventoryScreen(minecraft.player));
                }
            });

    public static final UnshatteredTab CRAFTING = new UnshatteredTab("crafting",
            TabIcon.item(Items.CRAFTING_TABLE),
            Component.translatable("container.unshattered.crafting"),
            () -> {
                closeContainerKeepScreen();
                ClientPacketDistributor.sendToServer(new OpenCraftingPacket());
            });

    public static final UnshatteredTab STORAGE = new UnshatteredTab("storage",
            TabIcon.item(Items.CHEST),
            Component.translatable("container.unshattered.storage"),
            () -> {
                closeContainerKeepScreen();
                ClientPacketDistributor.sendToServer(new OpenStoragePacket());
            });

    public static final UnshatteredTab TALISMAN_BAG = new UnshatteredTab("talisman_bag",
            TabIcon.item(Items.ENDER_CHEST),
            Component.translatable("container.unshattered.talisman_bag"),
            () -> {
                closeContainerKeepScreen();
                ClientPacketDistributor.sendToServer(new OpenTalismanBagPacket());
            });

    public static final UnshatteredTab STATS = new UnshatteredTab("stats",
            TabIcon.item(Items.BOOK),
            Component.translatable("screen.unshattered.stats"),
            () -> openClientScreen(StatsScreen::new)
    );

    public static final UnshatteredTab SKILLS = new UnshatteredTab("skills",
            TabIcon.item(Items.ENCHANTED_BOOK),
            Component.translatable("screen.unshattered.skills"),
            () -> openClientScreen(SkillsScreen::new)
    );

    public static final UnshatteredTab PETS = new UnshatteredTab("pets",
            TabIcon.item(Items.BONE),
            Component.translatable("screen.unshattered.pets"),
            () -> {}
    );

    public static final UnshatteredTab SKILL_TREES = new UnshatteredTab("skill_trees",
            TabIcon.item(Items.OAK_SAPLING),
            Component.translatable("screen.unshattered.skill_trees"),
            () -> {}
    );

    public static final UnshatteredTab QUESTS = new UnshatteredTab("quests",
            TabIcon.item(Items.WRITTEN_BOOK),
            Component.translatable("screen.unshattered.quests"),
            () -> {}
    );

    public static final UnshatteredTab SACK_OF_SACKS = new UnshatteredTab("sack_of_sacks",
            TabIcon.item(Items.BUNDLE),
            Component.translatable("screen.unshattered.sack_of_sacks"),
            () -> {
                closeContainerKeepScreen();
                ClientPacketDistributor.sendToServer(new OpenSackOfSacksPacket());
            });

    public static final UnshatteredTab[] ALL = {INVENTORY, CRAFTING, STORAGE, TALISMAN_BAG, STATS, SKILLS, PETS, SKILL_TREES, QUESTS, SACK_OF_SACKS};

    /**
     * needs to be called after super.extractBackground
     */
    private static void openClientScreen(Supplier<Screen> screen) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null) {
            minecraft.setScreen(screen.get());
        }
    }

    public static void closeContainerKeepScreen() {
        Minecraft minecraft = Minecraft.getInstance();
        LocalPlayer player = minecraft.player;
        if (player == null) {
            return;
        }

        if (player.containerMenu != player.inventoryMenu) {
            AbstractContainerMenu oldMenu = player.containerMenu;
            // so the cursor doesnt reset
            player.connection.send(new ServerboundContainerClosePacket(oldMenu.containerId));
            // the inventory menu doesnt seem to update on the clientside so this (should) fix that
            player.inventoryMenu.setCarried(oldMenu.getCarried());
            player.containerMenu = player.inventoryMenu;
        }
    }}
