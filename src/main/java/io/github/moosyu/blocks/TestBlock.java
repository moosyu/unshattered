package io.github.moosyu.blocks;

import io.github.moosyu.data.dialogue.DialogueInteractable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;

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
        System.out.println("hi");
    }

    @Override
    public boolean dialogueConditionsMet(Player player) {
        return true;
    }
}
