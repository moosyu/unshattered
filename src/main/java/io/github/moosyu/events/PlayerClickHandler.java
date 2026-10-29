package io.github.moosyu.events;

import io.github.moosyu.data.NpcData;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.dialogue.DialogueInteractable;
import io.github.moosyu.entities.npcs.NPCEntity;
import io.github.moosyu.packets.OpenReforgeAnvilPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.TriState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class PlayerClickHandler {
    public static final Identifier ACTIVE_RIGHT_CLICK = Identifier.fromNamespaceAndPath(MODID, "active_right_click");
    public static final int INTERACTION_COOLDOWN = 5;

    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
        BlockPos pos = event.getPos();
        BlockState interactedBlockState = event.getLevel().getBlockState(pos);
        Player player = event.getEntity();
        // was thinking about a set or something for this but supposedly thatd be slower. i cant see how but whatever
        if (interactedBlockState.is(Blocks.FURNACE)
                || interactedBlockState.is(Blocks.BLAST_FURNACE)
                || interactedBlockState.is(BlockTags.COPPER_CHESTS)
                || interactedBlockState.is(Blocks.ENDER_CHEST)
                || interactedBlockState.is(BlockTags.SHULKER_BOXES)
                || interactedBlockState.is(Blocks.BARREL)
                || interactedBlockState.is(BlockTags.BEDS)
                || interactedBlockState.is(BlockTags.ALL_SIGNS)
                || interactedBlockState.is(Blocks.RESPAWN_ANCHOR)
                || interactedBlockState.is(Blocks.LECTERN)
                || interactedBlockState.is(BlockTags.WOODEN_SHELVES)
                || interactedBlockState.is(Blocks.REDSTONE_TORCH)
                || interactedBlockState.is(Blocks.SMOKER)
                || interactedBlockState.is(Blocks.BLAST_FURNACE)
                || interactedBlockState.is(Blocks.LOOM)
                || interactedBlockState.is(Blocks.GRINDSTONE)
                || interactedBlockState.is(Blocks.SMITHING_TABLE)
                || interactedBlockState.is(Blocks.FLETCHING_TABLE)
                || interactedBlockState.is(Blocks.CARTOGRAPHY_TABLE)
                || interactedBlockState.is(Blocks.STONECUTTER)
                || interactedBlockState.is(Blocks.BREWING_STAND)
                || interactedBlockState.is(Blocks.ENCHANTING_TABLE)
        ) {
            event.setCanceled(true);
            return;
        }

        if (interactedBlockState.is(Blocks.ANVIL)) {
            event.setCanceled(true);
            player.swing(InteractionHand.MAIN_HAND);
            event.setCancellationResult(InteractionResult.SUCCESS);
            ClientPacketDistributor.sendToServer(new OpenReforgeAnvilPacket());
            return;
        }

        // disables block placement
        if (event.getItemStack().getItem() instanceof BlockItem && !player.isCreative()) {
            event.setUseItem(TriState.FALSE);
            return;
        }

        Block interactedBlock = interactedBlockState.getBlock();
        // i *think* this was firing on both hands for some reason so i added the event#getHand gate
        if (interactedBlock instanceof DialogueInteractable dialogueBlock && event.getHand() == InteractionHand.MAIN_HAND) {
            event.setCancellationResult(InteractionResult.SUCCESS);
            PlayerAbilityEffectsAttachment playerAbilityEffectsAttachment = player.getData(UnshatteredAttachments.PLAYER_ABILITIES);
            if (playerAbilityEffectsAttachment.hasActiveEffect(ACTIVE_RIGHT_CLICK) && !event.getLevel().isClientSide()) {
                player.sendSystemMessage(Component.translatable("misc.messages.unshattered.interact_cooldown").withColor(UnshatteredUtils.RED));
                return;
            }

            Integer npcNameColour = BuiltInRegistries.BLOCK.wrapAsHolder(interactedBlock).getData(UnshatteredDataMaps.BLOCK_NPC_DATA);
            dialogueBlock.onDialogueTriggered(player, npcNameColour == null ? 0xFFFFFFFF : npcNameColour);
            playerAbilityEffectsAttachment.addActiveEffect(ACTIVE_RIGHT_CLICK, INTERACTION_COOLDOWN, player.level(), null);
        }
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Level level = event.getLevel();
        Player player = event.getEntity();

        if (event.getTarget().is(EntityType.ARMOR_STAND) && !player.isCreative()) {
            event.setCanceled(true);
        }

        if (event.getTarget() instanceof NPCEntity npcEntity) {
            if (player.isCreative()) {
                npcEntity.discard();
            } else if (event.getHand() == InteractionHand.MAIN_HAND
                    && !player.isCreative()
                    && npcEntity instanceof DialogueInteractable dialogueInteractable
            ) {
                PlayerAbilityEffectsAttachment playerAbilityEffectsAttachment = player.getData(UnshatteredAttachments.PLAYER_ABILITIES);
                if (playerAbilityEffectsAttachment.hasActiveEffect(ACTIVE_RIGHT_CLICK) && !level.isClientSide()) {
                    player.sendSystemMessage(Component.translatable("misc.messages.unshattered.interact_cooldown").withColor(UnshatteredUtils.RED));
                    return;
                }

                NpcData npcData = BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(npcEntity.getType()).getData(UnshatteredDataMaps.ENTITY_NPC_DATA);
                dialogueInteractable.onDialogueTriggered(player, npcData == null ? 0xFFFFFFFF : npcData.npcColour());
                playerAbilityEffectsAttachment.addActiveEffect(ACTIVE_RIGHT_CLICK, INTERACTION_COOLDOWN, level, null);
            }
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        if (event.getAction() == PlayerInteractEvent.LeftClickBlock.Action.ABORT
                && !player.level().isClientSide()
                && player.getData(UnshatteredAttachments.PLAYER_STATE).isFailedMessageFired()
        ) {
            System.out.println("fire reset");
            player.getData(UnshatteredAttachments.PLAYER_STATE).setFailedMessageFired(false);
            player.syncData(UnshatteredAttachments.PLAYER_STATE);
        }
    }
}