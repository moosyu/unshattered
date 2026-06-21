package io.github.moosyu.screens;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.AttributeTypes;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.attachments.UnshatteredAttachments;
import io.github.moosyu.util.TextHelpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.layers.UnshatteredGuiLayers.*;

public class ProfileScreen extends Screen {
    private enum Tabs {
        SKILLS(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        STATS(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        QUESTS(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        CRAFTING(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        COLLECTIONS(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        PETS(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        STORAGE(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        WARDROBE(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        BANK(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        WARP(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png")),
        BAGS(Identifier.fromNamespaceAndPath("minecraft", "textures/block/stone.png"));

        private final Identifier iconTexture;

        Tabs(Identifier iconTexture) {
            this.iconTexture = iconTexture;
        }
    }

    private static final int SCREEN_WIDTH = 350;
    private static final int SCREEN_HEIGHT = 150;
    private static final String PAGE_NAME = "Profile";
    private Tabs currentTab = Tabs.SKILLS;

    public ProfileScreen() {super(Component.literal(PAGE_NAME));}

    @Override
    protected void init() {
        super.init();

        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        this.clearWidgets();

        final int CORNER_POS_X = (this.width - SCREEN_WIDTH) / 2;
        final int CORNER_POS_Y = (this.height - SCREEN_HEIGHT) / 2;

        for (int i = 0; i < Tabs.values().length; i++) {
            final Tabs CURRENT_TAB_POSITION = Tabs.values()[i];
            final boolean top = i <= 6;
            this.addRenderableWidget(new TabButton(top ? CORNER_POS_X + 16 + (i * 48) : CORNER_POS_X + 16 + ((i - 7) * 48), top ? CORNER_POS_Y - 32 : CORNER_POS_Y + SCREEN_HEIGHT, 32, 32, (CURRENT_TAB_POSITION == currentTab ? TAB_OPENED : TAB_CLOSED), () -> {
                currentTab = CURRENT_TAB_POSITION;
                this.init();
            }, !top, CURRENT_TAB_POSITION.iconTexture, currentTab == CURRENT_TAB_POSITION));
        }

        // for anything that requires it
        int uniqueIndex = 0;

        // todo: center these horizontally and vertically
        if (currentTab == Tabs.SKILLS) {
            for (int i = 0; i < PlayerSkillsAttachment.Skill.values().length; i++) {
                boolean even = i % 2 == 0;
                int posX = (even ? CORNER_POS_X : CORNER_POS_X + SCREEN_WIDTH - 176);
                int posY = (even ? (CORNER_POS_Y + i * 24) : (CORNER_POS_Y + (i - 1) * 24));
                PlayerSkillsAttachment.Skill currentSkill = PlayerSkillsAttachment.Skill.values()[i];
                this.addRenderableWidget(new SkillWidget(currentSkill, posX, posY, player, currentSkill.getIcon().getDefaultInstance()));
            }
        } else if (currentTab == Tabs.STATS) {
            for (UnshatteredAttributeValues currentStat : UnshatteredAttributeValues.values()) {
                int posX = (uniqueIndex > 9 ? CORNER_POS_X + SCREEN_WIDTH - 176 : CORNER_POS_X);
                int posY = (uniqueIndex > 9 ? CORNER_POS_Y + ((uniqueIndex - 10) * 14) : CORNER_POS_Y + (uniqueIndex * 14));
                if (currentStat.type == AttributeTypes.INVISIBLE) continue;
                this.addRenderableWidget(new StatWidget(currentStat, posX, posY, player));
                uniqueIndex++;
            }
            this.addRenderableWidget(new VanillaStatWidget((uniqueIndex > 9 ? CORNER_POS_X + SCREEN_WIDTH - 176 : CORNER_POS_X), (uniqueIndex > 9 ? CORNER_POS_Y + ((uniqueIndex - 10) * 14) : CORNER_POS_Y + (uniqueIndex * 14)), "✦", "attribute.name.unshattered.speed", (int) (player.getAttributeValue(Attributes.MOVEMENT_SPEED) * 1000), 0xFFFFFFFF));
        }
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        // putting this all here to layer the dimmed background bit and the actual empty screen properly
        // idk why this happens i mustve done something wrong at some point
        this.extractTransparentBackground(graphics);
        final int CORNER_POS_X = (this.width - SCREEN_WIDTH) / 2;
        final int CORNER_POS_Y = (this.height - SCREEN_HEIGHT) / 2;

        graphics.blit(RenderPipelines.GUI_TEXTURED, PROFILE_SCREEN, CORNER_POS_X, CORNER_POS_Y, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, SCREEN_WIDTH, SCREEN_HEIGHT);
        // some debugging stuff
        //final int RIGHT_POS = CORNER_POS_Y + SCREEN_WIDTH;
        //final int BOTTOM_POS = CORNER_POS_Y + SCREEN_HEIGHT;
        //final int COLOR = 0xFFFF0000;

        //graphics.fill(CORNER_POS_X, CORNER_POS_X, RIGHT_POS, CORNER_POS_X + 1, COLOR);
        //graphics.fill(CORNER_POS_X, BOTTOM_POS - 1, RIGHT_POS, BOTTOM_POS, COLOR);
        //graphics.fill(CORNER_POS_X, CORNER_POS_X, CORNER_POS_X + 1, BOTTOM_POS, COLOR);
        //graphics.fill(RIGHT_POS - 1, CORNER_POS_X, RIGHT_POS, BOTTOM_POS, COLOR);

    }

    // so it wont try to save and what not
    @Override
    public boolean isPauseScreen() {return false;}

    private static class SkillWidget extends AbstractWidget {
        private final PlayerSkillsAttachment.Skill skill;
        private final Player player;
        private final ItemStack displayIcon;

        public SkillWidget(PlayerSkillsAttachment.Skill skill, int x, int y, Player player, ItemStack displayIcon) {
            super(x, y, 144, 32, Component.literal(skill.name()));
            this.skill = skill;
            this.player = player;
            this.displayIcon = displayIcon;
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int i, int i1, float v) {
            PlayerSkillsAttachment playerData = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());

            // debug area for clicks
            // graphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x44FF0000);

            String levelText = skill.getName() + " " + TextHelpers.convertTextToRomanNumeral(playerData.getLevel(playerData.getExp(skill)));
            graphics.text(Minecraft.getInstance().font, levelText, this.getX() + 36, this.getY() + 4, 0xFF53F953, true);
            graphics.pose().pushMatrix();
            graphics.pose().scale(2f, 2f);
            graphics.item(displayIcon, this.getX() / 2, this.getY() / 2);
            graphics.pose().popMatrix();
            graphics.blit(RenderPipelines.GUI_TEXTURED, SKILL_BAR, this.getX() + 32, this.getY() + 24, 0, 7, 112, 8, 112, 15);
            graphics.blit(RenderPipelines.GUI_TEXTURED, SKILL_BAR, this.getX() + 32, this.getY() + 24, 0, 0, (int) (112 * playerData.getPercentageToNextLevel(playerData.getExp(skill))), 8, 112, 15);
        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            if (this.isValidClickButton(event.buttonInfo())) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                System.out.println(this.skill.getName());
                return true;
            }
            return false;
        }

        @Override
        protected void updateWidgetNarration(@NonNull NarrationElementOutput narrationElementOutput) {}
    }

    // this is a widget as i intend for some hover interaction at some point
    private static class StatWidget extends AbstractWidget {
        private final Player player;
        private final UnshatteredAttributeValues currentAttribute;

        public StatWidget(UnshatteredAttributeValues currentAttribute, int x, int y, Player player) {
            super(x, y, 32, 16, Component.literal(String.valueOf(Component.translatable(currentAttribute.getTranslationKey()))));
            this.player = player;
            this.currentAttribute = currentAttribute;
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            final double attributeValue = player.getAttributeValue(currentAttribute.holder);
            int textX = this.getX() + 20;
            int textY = this.getY() + 4;

            String statsTitle = currentAttribute.symbol + " " + Component.translatable(currentAttribute.getTranslationKey()).getString() + ":";
            graphics.text(Minecraft.getInstance().font, statsTitle, textX, textY, currentAttribute.color, false);
            graphics.text(Minecraft.getInstance().font, String.format("%.0f", attributeValue), textX + Minecraft.getInstance().font.width(statsTitle) + 2, textY, 0xFFDEDFE0, false);
        }

        @Override
        protected void updateWidgetNarration(@NonNull NarrationElementOutput narrationElementOutput) {}
    }

    private static class VanillaStatWidget extends AbstractWidget {
        private final String symbol;
        private final String key;
        private final int value;
        private final int color;
        public VanillaStatWidget(int x, int y, String symbol, String key, int value, int color) {
            super(x, y, 32, 16, Component.literal(key));
            this.symbol = symbol;
            this.value = value;
            this.key = key;
            this.color = color;
        }

        @Override
        protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
            int textX = this.getX() + 20;
            int textY = this.getY() + 4;

            String statsTitle = symbol + " " + Component.translatable(key).getString() + ":";
            graphics.text(Minecraft.getInstance().font, statsTitle, textX, textY, color, false);
            graphics.text(Minecraft.getInstance().font, String.valueOf(value), textX + Minecraft.getInstance().font.width(statsTitle) + 2, textY, 0xFFDEDFE0, false);
        }

        @Override
        protected void updateWidgetNarration(@NonNull NarrationElementOutput narrationElementOutput) {}
    }

    private static class TabButton extends AbstractWidget {
        private final Identifier texture, iconTexture;
        private final Runnable onPress;
        private final boolean flipped, active;

        public TabButton(int x, int y, int width, int height, Identifier texture, Runnable onPress, boolean flipped, Identifier iconTexture, boolean active) {
            super(x, y, width, height, Component.empty());
            this.texture = texture;
            this.onPress = onPress;
            this.flipped = flipped;
            this.iconTexture = iconTexture;
            this.active = active;
        }

        @Override
        protected void extractWidgetRenderState(@NonNull GuiGraphicsExtractor graphics, int i, int i1, float v) {
            if (flipped) {
                graphics.pose().pushMatrix();
                graphics.pose().translate(this.getX() + width / 2f, this.getY() + height / 2f);
                graphics.pose().rotate((float) Math.PI);
                graphics.pose().translate(-(this.getX() + width / 2f), -(this.getY() + height / 2f));
                graphics.blit(RenderPipelines.GUI_TEXTURED, texture, this.getX(), this.getY(), 0, 0, width, height, width, height);
                graphics.pose().popMatrix();

                // icons
                graphics.blit(RenderPipelines.GUI_TEXTURED, iconTexture, this.getX(), active ? (this.getY()) - 1 : (this.getY()), 0, active ? 0 : 5, 16, active ? 16 : 11, 16, 16);
            } else {
                graphics.blit(RenderPipelines.GUI_TEXTURED, texture, this.getX(), this.getY(), 0, 0, width, height, width, height);

                // icons
                graphics.blit(RenderPipelines.GUI_TEXTURED, iconTexture, this.getX(), active ? (this.getY()) + 1 : (this.getY()) + 5, 0, 0, 16, active ? 16 : 11, 16, 16);
            }

        }

        @Override
        public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
            if (this.isValidClickButton(event.buttonInfo())) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                onPress.run();
                return true;
            }
            return false;
        }

        @Override
        protected void updateWidgetNarration(@NonNull NarrationElementOutput narrationElementOutput) {}
    }
}