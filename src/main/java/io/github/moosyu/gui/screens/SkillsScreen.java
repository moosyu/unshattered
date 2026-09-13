package io.github.moosyu.gui.screens;

import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NonNull;

public class SkillsScreen extends SimpleScreen {
    private final PlayerSkillsAttachment.Skill[] SKILLS = PlayerSkillsAttachment.Skill.values();
    private final Player player;

    protected SkillsScreen(Component title) {
        super(title, 176, 166, "textures/gui/empty_screen.png");

        this.player = Minecraft.getInstance().player;
    }

    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        final int BAR_WIDTH = 154;
        final int BAR_HEIGHT = 5;
        final int X_OFFSET = 11;
        final int Y_OFFSET = 13;

        PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS);

        for (int i = 0; i < SKILLS.length; i++) {
            PlayerSkillsAttachment.Skill currentSkill = SKILLS[i];
            int currentLevel = skills.getLevel(skills.getExp(currentSkill));

            graphics.text(font,
                    Component.translatable("skills.name.unshattered."
                            + currentSkill.getId()).append(" " + UnshatteredUtils.convertTextToRomanNumeral(currentLevel)
                            + " ("
                            + (int) skills.getCurrentLevelExp(currentSkill, currentLevel)
                            + "/"
                            + (int) skills.getNextLevelExpRequirement(currentLevel)
                            + ")"
                    ),
                    backgroundTopLeft.x + X_OFFSET,
                    (20 * i) + backgroundTopLeft.y + Y_OFFSET,
                    0xFFFFFFFF
            );

            graphics.blit(RenderPipelines.GUI_TEXTURED,
                    UnshatteredUtils.getUnshatteredIdentifier("textures/gui/sprites/widgets/skill_exp_bar_empty.png"),
                    backgroundTopLeft.x + X_OFFSET,
                    (20 * i) + 12 + backgroundTopLeft.y + Y_OFFSET,
                    0,
                    0,
                    BAR_WIDTH,
                    BAR_HEIGHT,
                    BAR_WIDTH,
                    BAR_HEIGHT
            );

            graphics.blit(RenderPipelines.GUI_TEXTURED,
                    UnshatteredUtils.getUnshatteredIdentifier("textures/gui/sprites/widgets/skill_exp_bar_filled.png"),
                    backgroundTopLeft.x + X_OFFSET,
                    (20 * i) + 12 + backgroundTopLeft.y + Y_OFFSET,
                    0,
                    0,
                    (int) (BAR_WIDTH * skills.getPercentageToNextLevel(skills.getExp(currentSkill))),
                    BAR_HEIGHT,
                    BAR_WIDTH,
                    BAR_HEIGHT
            );
        }
    }
}
