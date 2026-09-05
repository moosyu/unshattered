package io.github.moosyu.gui.screens;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.joml.Vector2i;
import org.jspecify.annotations.NonNull;

public class SimpleScreen extends Screen {
    protected final Identifier texture;
    protected final int imageWidth;
    protected final int imageHeight;
    protected Vector2i backgroundTopLeft;

    protected SimpleScreen(Component title, int imageWidth, int imageHeight, String texturePath) {
        super(title);

        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.texture = UnshatteredUtils.getUnshatteredIdentifier(texturePath);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        this.backgroundTopLeft = new Vector2i((this.width - imageWidth) / 2, (this.height - imageHeight) / 2);
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        // same dimness as container menus
        this.extractTransparentBackground(graphics);

        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, backgroundTopLeft.x, backgroundTopLeft.y, 0, 0, imageWidth, imageHeight, 256, 256);
    }

    @Override
    public boolean keyPressed(@NonNull KeyEvent event) {
        if (Minecraft.getInstance().options.keyInventory.matches(event)) {
            this.onClose();
            return true;
        }

        return super.keyPressed(event);
    }
}