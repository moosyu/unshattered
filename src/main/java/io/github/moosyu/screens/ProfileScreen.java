package io.github.moosyu.screens;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.helpers.RomanNumeralHelper;
import io.github.moosyu.registers.AttachmentRegistry;
import io.github.moosyu.registers.AttributesRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import static io.github.moosyu.registers.TextureRegister.EMPTY_SCREEN;

public class ProfileScreen extends Screen {
    private static final int SCREEN_WIDTH = 176;
    private static final int SCREEN_HEIGHT = 222;
    private static final int BOX_WIDTH = 65;
    private static final String PAGE_NAME = "Profile";
    private int centerPosX;

    public ProfileScreen() {super(Component.literal(PAGE_NAME));}

    @Override
    protected void init() {
        super.init();

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        int cornerPosX = (this.width - SCREEN_WIDTH) / 2;
        int cornerPosY = (this.height - SCREEN_HEIGHT) / 2;
        centerPosX = this.width / 2;
        int centerPosY = this.height / 2;
        final float TEXT_SPACING = (float) (SCREEN_HEIGHT - 40) / (PlayerSkillsAttachment.Skill.values().length * 2);
        final int STAT_X_OFFSET = 5;
        final int STAT_Y_OFFSET = 20;
        // how far the boxes are offset from the side of the menu
        final int BOX_X_OFFSET = 15;

        // todo: loop through all the attributes
        this.addRenderableWidget(new StatWidget("Health", AttributesRegistry.HEALTH, cornerPosX - STAT_X_OFFSET, cornerPosY + (int) ((cornerPosY + 10) / 1.5f) + STAT_Y_OFFSET, player));

        for (int i = 0; i < PlayerSkillsAttachment.Skill.values().length; i++) {
            boolean even = i % 2 == 0;
            int iconX = (even ? cornerPosX + BOX_X_OFFSET : cornerPosX + SCREEN_WIDTH - BOX_WIDTH - BOX_X_OFFSET);
            int textY = (even ? (int) (centerPosY + 15 + i * TEXT_SPACING) : (int)(centerPosY + 15 + (i - 1) * TEXT_SPACING));
            int iconY = textY - 4;
            PlayerSkillsAttachment.Skill currentSkill = PlayerSkillsAttachment.Skill.values()[i];

            this.addRenderableWidget(new SkillWidget(currentSkill, iconX, iconY, player, new ItemStack(currentSkill.getIcon())));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {super.render(graphics, mouseX, mouseY, partialTick);}

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // putting this all here to layer the dimmed background bit and the actual empty screen properly
        // idk why this happens i mustve done something wrong at some point
        this.renderTransparentBackground(graphics);
        int cornerPosX = (this.width - SCREEN_WIDTH) / 2;
        int cornerPosY = (this.height - SCREEN_HEIGHT) / 2;

        graphics.blit(EMPTY_SCREEN, cornerPosX, cornerPosY, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);

        // title
        float scale = 1.5f;
        graphics.pose().pushPose();
        graphics.pose().scale(scale, scale, scale);
        graphics.drawString(font, PAGE_NAME, (centerPosX / scale) - (font.width(PAGE_NAME) / 2f), (cornerPosY + 10) / scale, 0x000, false);
        graphics.pose().popPose();
    }

    // so it wont try to save and what not
    @Override
    public boolean isPauseScreen() {return false;}

    private static class SkillWidget extends AbstractWidget {
        private final PlayerSkillsAttachment.Skill skill;
        private final Player player;
        private final ItemStack displayIcon;

        public SkillWidget(PlayerSkillsAttachment.Skill skill, int x, int y, Player player, ItemStack displayIcon) {
            super(x, y, BOX_WIDTH, 16, Component.literal(skill.name()));
            this.skill = skill;
            this.player = player;
            this.displayIcon = displayIcon;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            PlayerSkillsAttachment playerData = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());
            int textX = this.getX() + 20;
            int textY = this.getY() + 4;
            float scale = 0.75f;

            graphics.fill(this.getX() - 3, this.getY() - 3, this.getX() + BOX_WIDTH, this.getY() + 19, 0xFFFFFFFF);
            graphics.fill(this.getX() - 2, this.getY() - 2, this.getX() + BOX_WIDTH - 1, this.getY() + 18, 0xFF2B2B2B);
            String levelText = skill.getName() + " " + RomanNumeralHelper.toRoman(playerData.getLevel(playerData.getExp(skill)));
            graphics.pose().pushPose();
            graphics.pose().scale(scale, scale, scale);
            // idrk why i need to add the + 2 but it's acting odd
            graphics.drawString(Minecraft.getInstance().font, levelText, textX / scale, (textY / scale) + 2, 0xFF53F953, true);
            graphics.pose().popPose();
            graphics.renderItem(displayIcon, this.getX(), this.getY());
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.isValidClickButton(button) && this.clicked(mouseX, mouseY)) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                return true;
            }
            return super.mouseClicked(mouseX, mouseY, button);
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}
    }

    // this is a widget as i intend for some hover interaction at some point
    private static class StatWidget extends AbstractWidget {
        private final Player player;
        private final Holder<Attribute> attribute;
        private final String attributeName;

        public StatWidget(String attributeName, Holder<Attribute> attribute, int x, int y, Player player) {
            super(x, y, BOX_WIDTH, 16, Component.literal(attributeName));
            this.player = player;
            this.attribute = attribute;
            this.attributeName = attributeName;
        }

        @Override
        protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
            final double attributeValue = player.getAttributeValue(attribute);
            int textX = this.getX() + 20;
            int textY = this.getY() + 4;
            float scale = 0.75f;

            graphics.pose().pushPose();
            graphics.pose().scale(scale, scale, scale);
            graphics.drawString(Minecraft.getInstance().font, "❤ " + attributeName + ":", textX / scale, textY / scale, 0xFFFF5555, true);
            graphics.drawString(Minecraft.getInstance().font, String.format("%.0f", attributeValue), (textX / scale) + Minecraft.getInstance().font.width("❤ " + attributeName + ": ") + 2, textY / scale, 0xFFFFFFFF, true);
            graphics.pose().popPose();
        }

        @Override
        protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {}
    }
}