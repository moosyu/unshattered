package io.github.moosyu.layers;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.jspecify.annotations.NonNull;

public class ItemNameLayer implements GuiLayer {
    @Override
    public void render(GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        graphics.text(minecraft.font, "Hi", 0, 0, 0xFFFFFFFF);
    }
}
