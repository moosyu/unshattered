package io.github.moosyu.events;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.attachments.*;
import io.github.moosyu.gui.menus.ReforgeAnvilMenu;
import io.github.moosyu.gui.menus.StorageMenu;
import io.github.moosyu.gui.menus.TalismansMenu;
import io.github.moosyu.packets.*;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jspecify.annotations.NonNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class RegisterPayloadsHandler {
    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(ZombieSwordEffectsPacket.TYPE, ZombieSwordEffectsPacket.STREAM_CODEC);
        registrar.playToClient(ExpSoundEffectPacket.TYPE, ExpSoundEffectPacket.STREAM_CODEC);
        registrar.playToClient(DeathSoundEffectPacket.TYPE, DeathSoundEffectPacket.STREAM_CODEC);
        registrar.playToClient(DamageNumberPacket.TYPE, DamageNumberPacket.STREAM_CODEC);
        registrar.playToClient(OpenDialoguePacket.TYPE, OpenDialoguePacket.STREAM_CODEC);
        registrar.playToClient(FerocityEffectPacket.TYPE, FerocityEffectPacket.STREAM_CODEC);
        registrar.playToClient(BlockBreakSyncPacket.TYPE, BlockBreakSyncPacket.STREAM_CODEC);
        registrar.playToClient(WeakHitSoundEffectPacket.TYPE, WeakHitSoundEffectPacket.STREAM_CODEC);
        registrar.playToClient(ClientsidePlayerSoundEffectPacket.TYPE, ClientsidePlayerSoundEffectPacket.STREAM_CODEC);

        registrar.playToServer(OpenTalismanBagPacket.TYPE,
                OpenTalismanBagPacket.STREAM_CODEC,
                (_, context) -> context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer serverPlayer) {
                        serverPlayer.openMenu(new SimpleMenuProvider(
                                (containerId,
                                 inventory,
                                 _) -> new TalismansMenu(containerId,
                                        inventory,
                                        serverPlayer.getData(UnshatteredAttachments.PLAYER_TALISMAN_STORAGE)
                                ),
                                Component.translatable("container.unshattered.talisman_bag")
                        ));
                    }
                })
        );

        registrar.playToServer(OpenCraftingPacket.TYPE,
                OpenCraftingPacket.STREAM_CODEC,
                (_, context) -> context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer serverPlayer) {
                        serverPlayer.openMenu(new SimpleMenuProvider(
                                (containerId, inventory, _) -> new CraftingMenu(containerId, inventory, ContainerLevelAccess.create(serverPlayer.level(), serverPlayer.blockPosition())) {
                                    @Override
                                    public boolean stillValid(@NonNull Player player) {
                                return true;
                            }
                            },
                                Component.translatable("container.unshattered.crafting")
                        ));
                    }
                })
        );

        registrar.playToServer(OpenStoragePacket.TYPE,
                OpenStoragePacket.STREAM_CODEC,
                (_, context) -> context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer serverPlayer) {
                        serverPlayer.openMenu(new SimpleMenuProvider(
                                (containerId,
                                 inventory,
                                 _) -> new StorageMenu(containerId,
                                        inventory,
                                        serverPlayer.getData(UnshatteredAttachments.PLAYER_BANK_STORAGE)
                                ),
                                Component.translatable("container.unshattered.storage")
                        ));
                    }
                })
        );

        registrar.playToServer(OpenReforgeAnvilPacket.TYPE,
                OpenReforgeAnvilPacket.STREAM_CODEC,
                (_, context) -> context.enqueueWork(() -> context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer serverPlayer) {
                        serverPlayer.openMenu(new SimpleMenuProvider(
                                (containerId,
                                 inventory,
                                 _) -> new ReforgeAnvilMenu(containerId, inventory, ContainerLevelAccess.create(serverPlayer.level(), serverPlayer.blockPosition())),
                                Component.translatable("container.unshattered.reforge_anvil")
                            ));
                        }
                    })
                )
        );

        registrar.playToServer(ResetFlagQueuePacket.TYPE,
                ResetFlagQueuePacket.STREAM_CODEC,
                (data, context) -> context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer serverPlayer) {
                        PlayerFlagsAttachment playerFlagsAttachment = serverPlayer.getData(UnshatteredAttachments.PLAYER_FLAGS);
                        if (data.addToPlayerFlags()) {
                            playerFlagsAttachment.addQueuedFlags();
                            serverPlayer.syncData(UnshatteredAttachments.PLAYER_FLAGS);
                        } else {
                            playerFlagsAttachment.clearFlagQueue();
                        }
                    }
                })
        );

        registrar.playToServer(QueueNewFlagsPacket.TYPE,
                QueueNewFlagsPacket.STREAM_CODEC,
                (data, context) -> context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer serverPlayer) {
                        serverPlayer.getData(UnshatteredAttachments.PLAYER_FLAGS.get()).addFlagsToQueue(data.flags());
                    }
                })
        );

        registrar.playToServer(TriggerEventPacket.TYPE,
                TriggerEventPacket.STREAM_CODEC,
                (data, context) -> context.enqueueWork(() -> {
                    if (context.player() instanceof ServerPlayer serverPlayer) {
                        data.dialogueTriggeredEvent().trigger(serverPlayer);
                    }
                })
        );

        registrar.playToServer(UpdateStorageSearchResultsPacket.TYPE,
                UpdateStorageSearchResultsPacket.STREAM_CODEC,
                (data, context) -> context.enqueueWork(() -> {
                    if (context.player().containerMenu instanceof StorageMenu storageMenu) {
                        storageMenu.handleSearch(data.input());
                    }
                })
        );

        registrar.playToServer(UpdateStoragePagePacket.TYPE,
                UpdateStoragePagePacket.STREAM_CODEC,
                (data, context) -> context.enqueueWork(() -> {
                    if (context.player().containerMenu instanceof StorageMenu storageMenu) {
                        storageMenu.updatePage(data.increment());
                    }
                })
        );

        registrar.playToServer(PlayerStartedSneakingPacket.TYPE,
                PlayerStartedSneakingPacket.STREAM_CODEC,
                (_, context) -> context.enqueueWork(() -> {
                    PlayerAbilityEffectsAttachment abilities = context.player().getData(UnshatteredAttachments.PLAYER_ABILITIES);
                    AbilityContext abilityContext = new AbilityContext().add(AbilityContextKey.PLAYER, (ServerPlayer) context.player());

                    for (ItemStack item : abilities.getStoredNonOngoingItems()) {
                        if (item.getItem() instanceof PassiveAbilityItem passiveAbilityItem && passiveAbilityItem.triggerTypes().contains(AbilityTriggerType.PLAYER_STARTED_SNEAKING) && passiveAbilityItem.abilityConditionsMet(abilityContext)) {
                            passiveAbilityItem.onAbilityTriggered(abilityContext);
                            passiveAbilityItem.onAbilityFinished(abilityContext);
                        }
                    }
                })
        );

        registrar.playToServer(AttemptPurchasePacket.TYPE,
                AttemptPurchasePacket.STREAM_CODEC,
                (data, context) -> context.enqueueWork(() -> {
                    ServerPlayer serverPlayer = (ServerPlayer) context.player();
                    int soldItemCount = data.soldItemStack().count();

                    if (data.price().isPresent() && !UnshatteredUtils.canAffordCoins(data.price().get() * soldItemCount, context.player())) {
                        context.player().sendSystemMessage(Component.translatable("screen.unshattered.vendor.text.purchase_failed_coins")
                                .withColor(UnshatteredUtils.RED)
                        );

                        PacketDistributor.sendToPlayer(serverPlayer, new ClientsidePlayerSoundEffectPacket(
                                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.VILLAGER_NO), 0.5f)
                        );


                        return;
                    }

                    if (data.itemTradeRequirements().isPresent() && !UnshatteredUtils.canTradeItems(data.itemTradeRequirements().get(), context.player(), soldItemCount)) {
                        context.player().sendSystemMessage(Component.translatable("screen.unshattered.vendor.text.purchase_failed_trade")
                                .withColor(UnshatteredUtils.RED)
                        );

                        PacketDistributor.sendToPlayer(serverPlayer, new ClientsidePlayerSoundEffectPacket(
                                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.VILLAGER_NO), 0.5f)
                        );

                        return;
                    }

                    data.price().ifPresent(price -> UnshatteredUtils.spendCoins(price * soldItemCount, context.player()));

                    data.itemTradeRequirements().ifPresent(requirements -> UnshatteredUtils.tradeItems(requirements, context.player(), soldItemCount));

                    PacketDistributor.sendToPlayer(serverPlayer, new ClientsidePlayerSoundEffectPacket(
                            BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.NOTE_BLOCK_PLING.value()),
                                    1.0f,
                                    2.0f
                            )
                    );

                    UnshatteredUtils.givePlayerHarvestedItemStack(context.player(), data.soldItemStack());
                })
        );

        registrar.playToServer(AttemptForgeItemPacket.TYPE,
                AttemptForgeItemPacket.STREAM_CODEC,
                (data, context) -> context.enqueueWork(() -> {
                    PlayerForgeSlotsAttachment forgeSlotsAttachment = context.player().getData(UnshatteredAttachments.PLAYER_FORGE_SLOTS.get());

                    if (forgeSlotsAttachment.availableSlots() >= data.forgeSlotIndex() + 1
                            && UnshatteredUtils.canForgeItem(context.player(), data.forgeRecipe())
                    ) {
                        List<PlayerForgeSlotsAttachment.ForgeSlot> newSlots = new ArrayList<>(forgeSlotsAttachment.slots());
                        newSlots.set(data.forgeSlotIndex(), new PlayerForgeSlotsAttachment.ForgeSlot(Instant.now().getEpochSecond(),
                                data.forgeRecipe().durationSeconds(),
                                Optional.of(new ItemStack(data.forgeRecipe().result().item()))
                        ));

                        context.player().setData(UnshatteredAttachments.PLAYER_FORGE_SLOTS.get(), new PlayerForgeSlotsAttachment(forgeSlotsAttachment.availableSlots(), newSlots));
                        context.player().syncData(UnshatteredAttachments.PLAYER_FORGE_SLOTS);
                        data.forgeRecipe().consume(context.player().getInventory());
                    }
                })
        );

        registrar.playToServer(AttemptClaimForgedItemPacket.TYPE,
                AttemptClaimForgedItemPacket.STREAM_CODEC,
                (data, context) -> context.enqueueWork(() -> {
                    PlayerForgeSlotsAttachment forgeSlotsAttachment = context.player().getData(UnshatteredAttachments.PLAYER_FORGE_SLOTS.get());

                    if (data.forgeSlotIndex() < 0 || data.forgeSlotIndex() >= forgeSlotsAttachment.slots().size()) {
                        return;
                    }

                    PlayerForgeSlotsAttachment.ForgeSlot slot = forgeSlotsAttachment.slots().get(data.forgeSlotIndex());

                    if (slot.itemStack().isEmpty() || (slot.startTime() + slot.forgingDuration()) > Instant.now().getEpochSecond()) {
                        return;
                    }

                    ItemStack claimedItem = slot.itemStack().get().copy();
                    List<PlayerForgeSlotsAttachment.ForgeSlot> newSlots = new ArrayList<>(forgeSlotsAttachment.slots());

                    newSlots.set(data.forgeSlotIndex(), new PlayerForgeSlotsAttachment.ForgeSlot(0, 0, Optional.empty()));
                    context.player().setData(UnshatteredAttachments.PLAYER_FORGE_SLOTS.get(), new PlayerForgeSlotsAttachment(forgeSlotsAttachment.availableSlots(), newSlots));
                    context.player().syncData(UnshatteredAttachments.PLAYER_FORGE_SLOTS);

                    PacketDistributor.sendToPlayer((ServerPlayer) context.player(),
                            new ClientsidePlayerSoundEffectPacket(
                                    BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.NOTE_BLOCK_PLING.value()),
                                    1.0f,
                                    2.0f
                            )
                    );

                    context.player().getData(UnshatteredAttachments.PLAYER_SKILLS.get()).addExp(PlayerSkillsAttachment.Skill.CARPENTRY, (float) (UnshatteredUtils.getItemSellValue(data.claimedItem()) * 0.1), context.player());
                    UnshatteredUtils.givePlayerHarvestedItemStack(context.player(), claimedItem);
                })
        );
    }
}
