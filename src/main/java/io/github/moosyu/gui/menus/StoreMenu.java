package io.github.moosyu.gui.menus;

import io.github.moosyu.data.ShopItem;
import io.github.moosyu.data.attachments.PlayerCurrencyAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.packets.ClientsidePlayerSoundEffectPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.NonNull;

import java.util.List;

public class StoreMenu extends AbstractContainerMenu {
    private final List<ShopItem> shopItems;
    private final Slot sellSlot;

    public StoreMenu(int containerId, Inventory playerInventory, RegistryFriendlyByteBuf buf) {
        this(containerId, playerInventory, ShopItem.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buf), new SimpleContainer(1));
    }

    public StoreMenu(int containerId, Inventory playerInventory, List<ShopItem> shopItems, Container container) {
        super(UnshatteredMenus.STORE_MENU_TYPE.get(), containerId);
        this.shopItems = shopItems;

        addStandardInventorySlots(playerInventory, 8, 73);
        this.sellSlot = addSlot(new Slot(container, 0, 80, 41) {
            @Override
            public Identifier getNoItemIcon() {
                return UnshatteredUtils.getUnshatteredIdentifier("slots/sell_icon_outline");
            }

            // these were made false because i basically do what they do with the menu clicked override
            // probably couldve done it better but it started feeling really complicated
            @Override public boolean mayPlace(@NonNull ItemStack itemStack) {
                return false;
            }

            @Override public boolean mayPickup(@NonNull Player player) {
                return false;
            }
        });
    }

    @Override
    public void clicked(int slotIndex, int buttonNum, @NonNull ContainerInput containerInput, @NonNull Player player) {
        if (slotIndex >= 0
                && slotIndex < slots.size()
                && slots.get(slotIndex) == sellSlot
                && containerInput == ContainerInput.PICKUP
        ) {
            if (!player.level().isClientSide()) {
                ItemStack stored = sellSlot.getItem();

                if (!getCarried().isEmpty()) {
                    if (sellStack(getCarried(), player)) {
                        setCarried(ItemStack.EMPTY);
                    }
                // for buying back items
                } else if (!stored.isEmpty()) {
                    int price = UnshatteredUtils.getItemSellValue(stored) * stored.count();
                    if (UnshatteredUtils.canAffordCoins(price, player)) {
                        UnshatteredUtils.spendCoins(price, player);
                        setCarried(stored.copy());
                        sellSlot.set(ItemStack.EMPTY);
                    }
                }
            }
            return;
        }

        super.clicked(slotIndex, buttonNum, containerInput, player);
    }

    private boolean sellStack(ItemStack itemStack, Player player) {
        int price = UnshatteredUtils.getItemSellValue(itemStack);
        if (price <= 0) {
            return false;
        }

        PlayerCurrencyAttachment currency = player.getData(UnshatteredAttachments.PLAYER_CURRENCY.get());
        currency.addCoins(price * itemStack.count());
        player.syncData(UnshatteredAttachments.PLAYER_CURRENCY);
        PacketDistributor.sendToPlayer((ServerPlayer) player, new ClientsidePlayerSoundEffectPacket(
                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.NOTE_BLOCK_PLING.value()), 0.5f, 2.0f)
        );

        sellSlot.set(itemStack.copy());
        return true;
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        Slot slot = slots.get(slotIndex);
        if (player.level().isClientSide() || slot == sellSlot || !slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack original = slot.getItem().copy();
        if (!sellStack(slot.getItem(), player)) {
            return ItemStack.EMPTY;
        }

        slot.set(ItemStack.EMPTY);
        return original;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return true;
    }

    public List<ShopItem> getShopItems() {
        return shopItems;
    }
}
