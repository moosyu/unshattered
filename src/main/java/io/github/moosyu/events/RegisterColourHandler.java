package io.github.moosyu.events;

import io.github.moosyu.blocks.UnshatteredBlocks;
import net.minecraft.client.color.block.BlockTintSource;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.jspecify.annotations.NonNull;

import java.util.List;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class RegisterColourHandler {
    @SubscribeEvent
    public static void registerBlockTints(RegisterColorHandlersEvent.BlockTintSources event) {
        event.register(List.of(new GrassTintSource()), UnshatteredBlocks.BREAKABLE_SUGARCANE_BLOCK.get());
    }

    public static class GrassTintSource implements BlockTintSource {
        @Override
        public int color(@NonNull BlockState state) {
            return GrassColor.getDefaultColor();
        }

        @Override
        public int colorInWorld(@NonNull BlockState state, @NonNull BlockAndTintGetter level, @NonNull BlockPos pos) {
            return BiomeColors.getAverageGrassColor(level, pos);
        }
    }}
