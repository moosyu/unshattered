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

import java.util.ArrayList;
import java.util.List;

public class SkillsScreen extends SimpleScreen {
    private final PlayerSkillsAttachment.Skill[] SKILLS = PlayerSkillsAttachment.Skill.values();
    private final Player player;

    protected SkillsScreen() {
        super(Component.translatable("screen.unshattered.skills"), 176, 166, "textures/gui/empty_screen.png");

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
            String skillExpText;
            int xPos = backgroundTopLeft.x + X_OFFSET;
            int yPos = (20 * i) + backgroundTopLeft.y + Y_OFFSET;
            float nextLevelExp = skills.getNextLevelExpRequirement(currentLevel);
            float currentLevelExp = skills.getCurrentLevelExp(currentSkill, currentLevel);

            if (skills.isMaxLevel(currentLevel)) {
                skillExpText = String.format("%,d", (int) skills.getExp(currentSkill));
            } else {
                skillExpText = String.format("%,d", (int) (skills.getNextLevelExpRequirement(currentLevel) - skills.getCurrentLevelExp(currentSkill, currentLevel)));
            }

            graphics.text(font,
                    Component.translatable("skills.name.unshattered."
                            + currentSkill.getId()).append(" " + UnshatteredUtils.convertTextToRomanNumeral(currentLevel)
                            + " ("
                            + skillExpText
                            + ")"
                    ),
                    xPos,
                    yPos,
                    0xFFFFFFFF
            );

            graphics.blit(RenderPipelines.GUI_TEXTURED,
                    UnshatteredUtils.getUnshatteredIdentifier("textures/gui/sprites/widgets/skill_exp_bar_empty.png"),
                    xPos,
                    yPos + 12,
                    0,
                    0,
                    BAR_WIDTH,
                    BAR_HEIGHT,
                    BAR_WIDTH,
                    BAR_HEIGHT
            );

            graphics.blit(RenderPipelines.GUI_TEXTURED,
                    UnshatteredUtils.getUnshatteredIdentifier("textures/gui/sprites/widgets/skill_exp_bar_filled.png"),
                    xPos,
                    yPos + 12,
                    0,
                    0,
                    (int) (BAR_WIDTH * skills.getPercentageToNextLevel(skills.getExp(currentSkill))),
                    BAR_HEIGHT,
                    BAR_WIDTH,
                    BAR_HEIGHT
            );

            if (mouseX >= xPos
                    && mouseX < xPos + BAR_WIDTH
                    && mouseY >= yPos
                    && mouseY < yPos + 12 + BAR_HEIGHT
            ) {

                if (skills.isMaxLevel(currentLevel)) {
                    skillExpText = String.format("%,d", (int) skills.getExp(currentSkill));
                } else {
                    skillExpText = String.format("%,d", (int) (skills.getNextLevelExpRequirement(currentLevel) - skills.getCurrentLevelExp(currentSkill, currentLevel)));
                }
                List<Component> tooltip = new ArrayList<>();

                tooltip.add(Component.translatable("skills.name.unshattered." + currentSkill.getId()).withColor(UnshatteredUtils.GREEN));
                tooltip.add(Component.translatable("skills.description.unshattered." + currentSkill.getId()).withColor(UnshatteredUtils.GRAY));
                tooltip.add(Component.empty());
                tooltip.add(Component.translatable("screen.unshattered.skills.progress").withColor(UnshatteredUtils.GRAY));

                graphics.setComponentTooltipForNextFrame(font, tooltip, mouseX, mouseY);
            }
        }
    }
}
