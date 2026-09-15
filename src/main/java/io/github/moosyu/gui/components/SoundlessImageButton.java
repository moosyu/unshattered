package io.github.moosyu.gui.components;

import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class SoundlessImageButton extends ImageButton {
    public SoundlessImageButton(int x, int y, int width, int height, WidgetSprites sprites, OnPress onPress) {
        super(x, y, width, height, sprites, onPress);
    }

    public SoundlessImageButton(int width, int height, WidgetSprites sprites, OnPress onPress, Component message) {
        super(width, height, sprites, onPress, message);
    }

    public SoundlessImageButton(int x, int y, int width, int height, WidgetSprites sprites, OnPress onPress, Component message) {
        super(x, y, width, height, sprites, onPress, message);
    }

    @Override
    public void playDownSound(@NonNull SoundManager soundManager) {}
}
