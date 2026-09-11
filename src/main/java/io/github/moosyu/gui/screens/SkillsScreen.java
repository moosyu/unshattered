package io.github.moosyu.gui.screens;

import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

public class SkillsScreen extends ScrollableListScreen {
    final Player player;

    protected SkillsScreen(Component title) {
        super(title, 176, 166, "textures/gui/generic_scrollable.png", 12);

        this.player = Minecraft.getInstance().player;
    }

    @Override
    protected int getItemCount() {
        return PlayerSkillsAttachment.Skill.values().length;
    }

    @Override
    protected void renderLine(GuiGraphicsExtractor graphics, int index, int lineY, int scissorTop, int scissorBottom) {

    }
}
