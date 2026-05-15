package io.github.moosyu.events;

import io.github.moosyu.NNO;
import io.github.moosyu.registers.AttributesRegistry;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;


public class TreeSweepHandler {
    private static final int BREAK_COOLDOWN_MAX = 2;
    // not too sure efficiency-wise if this is better than a list, check that out later
    // just kind of assumed it was faster and wanted to use something new...
    // todo: fix this for multiplayer, queues seem to weirdly overlaps so in the final form of this mod that should be solved
    private static final Queue<BreakTask> TO_BREAK = new ArrayDeque<>();
    private record BreakTask(Level level, BlockPos pos, Player player) {}
    private static int breakCooldown = BREAK_COOLDOWN_MAX;
    private static final Random random = new Random();

    public static int trySweep(Level level, BlockPos startPos, Player player) {
        // remove the first block (it wont be removed at the start as the vanilla break for logs is disabled
        level.removeBlock(startPos, false);

        int sweep = (int) player.getAttributeValue(AttributesRegistry.SWEEP);
        if (sweep <= 0) {
            return 0;
        }

        Queue<BreakTask> result = breakConnectedLogs(level, startPos, player, sweep);
        TO_BREAK.addAll(result);

        return result.size() + 1;
    }

    private static void giveDrops(Player player) {

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

    //private static int calculateLogs(Player player) {
    //    var attribute = player.getAttribute(AttributesRegistry.FORAGING_FORTUNE);
    //    double fortune = attribute != null ? attribute.getValue() : 0.0;
    //    double multiplier = 1.0 + (fortune / 100.0);
    //    int guaranteedMultiplier = (int) multiplier;
    //    int finalMultiplier = guaranteedMultiplier;

        // Roll for extra drop
    //    if (random.nextDouble() < (multiplier - guaranteedMultiplier)) {
      //      finalMultiplier++;
        //}

      //  return finalMultiplier;
    //}

    @EventBusSubscriber(modid = NNO.MODID)
    public static class EventHandler {
        @SubscribeEvent
        private static void onServerTickEvent(ServerTickEvent.Post event) {
            if (TO_BREAK.isEmpty()) {
                breakCooldown = BREAK_COOLDOWN_MAX;
                // todo: figure out a way to pull this off, remember that Queue#poll deletes entries so they'll have to be stored somewhere bonus (maybe i shouldve just used a list)
                //giveDrops();
                return;
            }

            if (breakCooldown > 0) {
                breakCooldown--;
                return;
            }

            BreakTask current = TO_BREAK.poll();
            Level level = current.level();
            BlockPos pos = current.pos();

            level.removeBlock(pos, false);
            breakCooldown = BREAK_COOLDOWN_MAX;
        }
    }
}
