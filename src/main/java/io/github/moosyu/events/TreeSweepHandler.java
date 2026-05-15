package io.github.moosyu.events;

import io.github.moosyu.NNO;
import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.registers.AttachmentRegistry;
import io.github.moosyu.registers.AttributesRegistry;
import io.github.moosyu.sounds.ModSounds;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BlockTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;


public class TreeSweepHandler {
    private static final int BREAK_COOLDOWN_MAX = 2;
    // not too sure efficiency-wise if this is better than a list, check that out later
    // just kind of assumed it was faster and wanted to use something new...
    // todo: fix this for multiplayer, queues seem to weirdly overlaps so in the final form of this mod that should be solved
    private static final List<BreakTask> TO_BREAK = new ArrayList<>();
    private record BreakTask(Level level, BlockPos pos, Player player, BlockState state) {}
    private static int breakCooldown = BREAK_COOLDOWN_MAX;
    private static final Random random = new Random();
    private static int listPos = 0;
    private static PlayerSkillsAttachment skills;

    public static void trySweep(Level level, BlockPos startPos, Player player) {
        skills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());
        // so when it's deleted the player doesnt just get given air
        BlockState startBlock = level.getBlockState(startPos);
        level.removeBlock(startPos, false);

        int sweep = (int) player.getAttributeValue(AttributesRegistry.SWEEP);
        if (sweep <= 0) {
            skills.addForagingExp(6.0f);
            ModSounds.playerExperienceSound(player);
            return;
        }
        // todo: disable when there are already logs being broken
        Queue<BreakTask> result = breakConnectedLogs(level, startPos, player, sweep);
        result.add(new BreakTask(level, startPos, player, startBlock));
        TO_BREAK.addAll(result);
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
                                toBreak.add(new BreakTask(level, mutablePos.immutable(), player, state));
                            }
                        }
                    }
                }
            }
        }
        return toBreak;
    }

    // todo: fix this always beeing zero?
    private static int calculateLogs(Player player) {
        var attribute = player.getAttribute(AttributesRegistry.FORAGING_FORTUNE);
        double fortune = attribute != null ? attribute.getValue() : 0.0;
        double multiplier = 1.0 + (fortune / 100.0);
        int guaranteedMultiplier = (int) multiplier;
        int finalMultiplier = guaranteedMultiplier;

        // roll for extra drop
        if (random.nextDouble() < (multiplier - guaranteedMultiplier)) {
            finalMultiplier++;
        }

        return finalMultiplier;
    }

    @EventBusSubscriber(modid = NNO.MODID)
    public static class EventHandler {
        @SubscribeEvent
        private static void onServerTickEvent(ServerTickEvent.Post event) {
            if (!TO_BREAK.isEmpty() && listPos == TO_BREAK.size()) {
                breakCooldown = BREAK_COOLDOWN_MAX;
                for (BreakTask current : TO_BREAK) {
                    int calculatedLogs = calculateLogs(current.player());
                    current.player().getInventory().add(new ItemStack(current.state().getBlock(), calculatedLogs));
                }
                skills.addForagingExp(TO_BREAK.size() * 6.0f);
                // my spider senses are telling me something terrible with happen with this in multiplayer but idrk rn
                ModSounds.playerExperienceSound(TO_BREAK.getLast().player());
                listPos = 0;
                TO_BREAK.clear();
                return;
            }

            if (breakCooldown > 0) {
                breakCooldown--;
                return;
            }

            if (TO_BREAK.isEmpty()) {
                return;
            }

            BreakTask current = TO_BREAK.get(listPos);
            Level level = current.level();
            BlockPos pos = current.pos();

            // as the first block (added at the end) will certainly be an empty block
            if (!level.isEmptyBlock(pos)) {
                level.removeBlock(pos, false);
            }
            listPos++;
            breakCooldown = BREAK_COOLDOWN_MAX;
        }
    }
}
