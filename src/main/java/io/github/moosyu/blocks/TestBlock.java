package io.github.moosyu.blocks;

import io.github.moosyu.data.dialogue.DialogueInteractable;
import io.github.moosyu.data.dialogue.DialogueNode;
import io.github.moosyu.data.dialogue.DialogueTreeOrigin;
import io.github.moosyu.events.DataPackRegistryHandler;
import io.github.moosyu.packets.OpenDialoguePacket;
import io.github.moosyu.util.DialogueUtil;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class TestBlock extends Block implements DialogueInteractable {
    public static final Component name = Component.literal("Rock");
    public static final Identifier HI_MESSAGE_IDENTIFIER = DialogueUtil.createDialogueNodeIdentifier(name.getString().toLowerCase(), "hi");
    public static final Identifier HI_2_MESSAGE_IDENTIFIER = DialogueUtil.createDialogueNodeIdentifier(name.getString().toLowerCase(), "hi2");

    public TestBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Component getInteractableName() {
        return name;
    }

    @Override
    public List<DialogueTreeOrigin> getDialogueTreeOrigins(RegistryAccess registryAccess) {
        return List.of(new DialogueTreeOrigin(List.of(), 0, registryAccess.lookupOrThrow(DataPackRegistryHandler.DIALOGUE_NODE_REGISTRY_KEY).getValue(HI_MESSAGE_IDENTIFIER), List.of("initial")), new DialogueTreeOrigin(List.of("initial"), 1, registryAccess.lookupOrThrow(DataPackRegistryHandler.DIALOGUE_NODE_REGISTRY_KEY).getValue(HI_2_MESSAGE_IDENTIFIER), List.of()));
    }
}
