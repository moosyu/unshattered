package io.github.moosyu.events;

import io.github.moosyu.NNO;
import io.github.moosyu.registers.AttributesRegistry;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;

public class TreeSweepHandler {
    private static final int BREAK_COOLDOWN_MAX = 2;
    // not too sure efficiency-wise if this is better than a list, check that out later
    // just kind of assumed it was faster and wanted to use something new...
    // todo: fix this for multiplayer, queues seem to weirdly overlaps so in the final form of this mod that should be solved
    private static final Queue<BreakTask> TO_BREAK = new ArrayDeque<>();
    private record BreakTask(Level level, BlockPos pos, Player player) {}
    private static int breakCooldown = BREAK_COOLDOWN_MAX;

    public static int trySweep(Level level, BlockPos startPos, Player player) {
        int sweep = (int) player.getAttributeValue(AttributesRegistry.SWEEP);
        if (sweep <= 0) {
            return 0;
        }

        Queue<BreakTask> result = breakConnectedLogs(level, startPos, player, sweep);
        TO_BREAK.addAll(result);

        return result.size();
    }
    private static Queue<BreakTask> breakConnectedLogs(Level level, BlockPos startPos, Player player, int sweep) {
        Queue<BreakTask> toBreak = new ArrayDeque<>();
        Queue<Long> queue = new LinkedList<>();
        LongOpenHashSet visited = new LongOpenHashSet();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        queue.add(startPos.asLong());
        visited.add(startPos.asLong());

        while (!queue.isEmpty()) {
            long currentLong = queue.poll();
            BlockPos currentPos = BlockPos.of(currentLong);

            // this 3d business is messed up, looping through 26 is pretty slow so that's why im getting all wacky and wild
            // by using mutablePos and LongOpenHashSet (longs are primatives so faster). still not fast but not the worst thing ever
            // also future me don't change this to 0 -> < 2, i know it looks weird but it needs to be this way for MutableBlockPos#set
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && y == 0 && z == 0) continue;

                        mutablePos.set(currentPos.getX() + x,currentPos.getY() + y,currentPos.getZ() + z);
                        long neighborLong = mutablePos.asLong();

                        if (!visited.contains(neighborLong)) {
                            visited.add(neighborLong);
                            BlockState state = level.getBlockState(mutablePos);
                            if (state.is(BlockTags.LOGS)) {
                                if (toBreak.size() >= sweep || toBreak.size() >= 35)
                                    return toBreak;
                                queue.add(neighborLong);
                                toBreak.add(new BreakTask(level, mutablePos.immutable(), player));
                            }
                        }
                    }
                }
            }
        }
        return toBreak;
    }

    @EventBusSubscriber(modid = NNO.MODID)
    public static class EventHandler {
        @SubscribeEvent
        private static void onServerTickEvent(ServerTickEvent.Post event) {
            if (TO_BREAK.isEmpty()) {
                breakCooldown = BREAK_COOLDOWN_MAX;
                return;
            }

            if (breakCooldown > 0) {
                breakCooldown--;
                return;
            }

            BreakTask current = TO_BREAK.poll();

            Level level = current.level();
            BlockPos pos = current.pos();
            Player player = current.player();

            // all this bullshit so that the extra broken logs wont have particles...
            BlockState state = level.getBlockState(pos);
            level.playSound(null, pos, state.getSoundType(level, pos, player).getBreakSound(), SoundSource.BLOCKS, 1.0F, 1.0F);
            Block.dropResources(state, level, pos, level.getBlockEntity(pos), player, player.getMainHandItem());
            level.removeBlock(pos, false);
            breakCooldown = BREAK_COOLDOWN_MAX;
        }
    }
}
