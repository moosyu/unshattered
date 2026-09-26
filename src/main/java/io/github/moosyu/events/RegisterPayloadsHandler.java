package io.github.moosyu.events;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.PlayerFlagsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.gui.menus.ReforgeAnvilMenu;
import io.github.moosyu.gui.menus.StorageMenu;
import io.github.moosyu.gui.menus.TalismansMenu;
import io.github.moosyu.packets.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

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
                    }))
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

        registrar.playToServer(UpdateDialogueStatePacket.TYPE,
                UpdateDialogueStatePacket.STREAM_CODEC,
                (data, context) -> context.enqueueWork(() -> {
                    Player player = context.player();
                    player.getData(UnshatteredAttachments.PLAYER_STATE).setDialogueOpen(data.opened());
                    player.syncData(UnshatteredAttachments.PLAYER_STATE);
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

                    for (PassiveAbilityItem item : abilities.getStoredPassiveNonOngoingItems()) {
                        if (item.triggerTypes().contains(AbilityTriggerType.PLAYER_STARTED_SNEAKING) && item.abilityConditionsMet(abilityContext)) {
                            item.onAbilityTriggered(abilityContext);
                            item.onAbilityFinished(abilityContext);
                        }
                    }
                })
        );
    }
}
