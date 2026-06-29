package io.github.moosyu.gui.screens;

import com.daqem.uilib.gui.AbstractScreen;
import com.daqem.uilib.gui.background.DarkenedBackground;
import com.daqem.uilib.gui.component.sprite.SpriteComponent;
import io.github.moosyu.gui.screens.components.StatsComponent;
import io.github.moosyu.gui.screens.components.TabSwitcherComponent;
import io.github.moosyu.gui.screens.states.TabSwitcherState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

import static io.github.moosyu.Unshattered.MODID;

public class ProfileScreen extends AbstractScreen {
    public ProfileScreen() {
        super(Component.literal("Stats"));
    }
    private static final int SCREEN_WIDTH = 700;
    private static final int SCREEN_HEIGHT = 300;

    @Override
    protected void init() {
        super.init();
        this.setBackground(new DarkenedBackground());

        final int CORNER_POS_X = (this.width - SCREEN_WIDTH) / 2;
        final int CORNER_POS_Y = (this.height - SCREEN_HEIGHT) / 2;

        this.addComponent(new SpriteComponent(CORNER_POS_X, CORNER_POS_Y, SCREEN_WIDTH, SCREEN_HEIGHT, Identifier.fromNamespaceAndPath(MODID, "profile_screen")));
        this.addComponent(new TabSwitcherComponent(CORNER_POS_X, CORNER_POS_Y, SCREEN_WIDTH, SCREEN_HEIGHT, new TabSwitcherState()));
        this.addComponent(new StatsComponent(CORNER_POS_X, 350, SCREEN_WIDTH, SCREEN_HEIGHT));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
