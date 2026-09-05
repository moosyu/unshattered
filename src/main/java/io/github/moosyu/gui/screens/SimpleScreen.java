package io.github.moosyu.gui.screens;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import org.joml.Vector2i;
import org.jspecify.annotations.NonNull;

public class SimpleScreen extends Screen {
    protected final Identifier texture;
    protected final int imageWidth;
    protected final int imageHeight;
    protected Vector2i backgroundTopLeft;
    protected double scrollOffset = 0;
    protected int contentHeight = 0;
    protected int viewportHeight;

    protected SimpleScreen(Component title, int imageWidth, int imageHeight, String texturePath) {
        super(title);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.texture = UnshatteredUtils.getUnshatteredIdentifier(texturePath);
        this.viewportHeight = imageHeight - 24;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (contentHeight > viewportHeight) {
            scrollOffset -= scrollY * 12;
            scrollOffset = Mth.clamp(scrollOffset, 0, contentHeight - viewportHeight);

            return true;
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void extractBackground(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);

        this.backgroundTopLeft = new Vector2i((this.width - imageWidth) / 2, (this.height - imageHeight) / 2);

        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, backgroundTopLeft.x, backgroundTopLeft.y, 0, 0, imageWidth, imageHeight, 256, 256);
    }
}