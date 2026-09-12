package io.github.moosyu.gui.screens;

import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

public class SkillsScreen extends ScrollableListScreen {
    private final PlayerSkillsAttachment.Skill[] SKILLS = PlayerSkillsAttachment.Skill.values();
    private final int BAR_WIDTH = 182;
    private final int BAR_HEIGHT = 5;
    private final Player player;

    protected SkillsScreen(Component title) {
        super(title, 176, 166, "textures/gui/generic_scrollable.png", 12);

        this.player = Minecraft.getInstance().player;
    }

    @Override
    protected int getItemCount() {
        return SKILLS.length;
    }

    @Override
    protected void renderLine(GuiGraphicsExtractor graphics, int index, int lineY, int scissorTop, int scissorBottom) {
        PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS);

        graphics.text(font, SKILLS[index].getId(), backgroundTopLeft.x + 9, lineY, 0xFFFFFFFF);

        graphics.blit(RenderPipelines.GUI_TEXTURED,
                Identifier.withDefaultNamespace("textures/gui/sprites/hud/experience_bar_background.png"),
                backgroundTopLeft.x,
                lineY,
                0,
                0,
                BAR_WIDTH,
                BAR_HEIGHT,
                BAR_WIDTH,
                BAR_HEIGHT
        );

        graphics.blit(RenderPipelines.GUI_TEXTURED,
                Identifier.withDefaultNamespace("textures/gui/sprites/hud/experience_bar_progress.png"),
                backgroundTopLeft.x,
                lineY,
                0,
                0,
                BAR_WIDTH * (int) (1 - skills.getPercentageToNextLevel(skills.getExp(SKILLS[index]))),
                BAR_HEIGHT,
                BAR_WIDTH,
                BAR_HEIGHT
        );
    }
}
