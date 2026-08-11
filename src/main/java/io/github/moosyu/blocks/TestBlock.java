package io.github.moosyu.blocks;

import io.github.moosyu.data.dialogue.DialogueInteractable;
import io.github.moosyu.packets.OpenDialoguePacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.network.PacketDistributor;

public class TestBlock extends Block implements DialogueInteractable {
    public TestBlock(Properties properties) {
        super(properties);
    }

    @Override
    public Component getDialogueHaverName() {
        return null;
    }

    @Override
    public void onDialogueTriggered(Player player) {
        if (!player.level().isClientSide()) {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new OpenDialoguePacket());
        }
    }

    @Override
    public boolean dialogueConditionsMet(Player player) {
        return true;
    }
}
