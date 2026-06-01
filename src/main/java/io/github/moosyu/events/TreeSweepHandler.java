package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.ModAttributes;
import io.github.moosyu.registers.AttachmentRegistry;
import io.github.moosyu.sounds.UnshatteredSounds;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.registers.AttachmentRegistry.PLAYER_SKILLS;

// shouldnt need to check if serverside as this will being run from BlockBreakHandler which already had that check
@EventBusSubscriber(modid = MODID)
public class TreeSweepHandler {
    private static final int BREAK_COOLDOWN_MAX = 2;
    private static final List<TreeBreakInstance> ACTIVE_BREAKS = new ArrayList<>();
    private static final Random RANDOM = new Random();
    // todo: play a sound if the tree has been completely destroyed (creaking death)
    private record BreakTask(Level level, BlockPos pos, Player player, BlockState state) {}

    private static class TreeBreakInstance {
        private final List<BreakTask> tasks;
        private int listPos = 0;
        private int cooldown = BREAK_COOLDOWN_MAX;

        public TreeBreakInstance(List<BreakTask> tasks) {
            this.tasks = tasks;
        }

        private boolean tick() {
            if (listPos >= tasks.size()) {
                finish();
                return true;
            }

            if (cooldown > 0) {
                cooldown--;
                return false;
            }

            BreakTask current = tasks.get(listPos);
            Level level = current.level();
            BlockPos pos = current.pos();

            if (!level.isEmptyBlock(pos)) {
                level.removeBlock(pos, false);
            }

            listPos++;
            cooldown = BREAK_COOLDOWN_MAX;

            return false;
        }

        private void finish() {
            // unless something has gone horribly wrong the "player" value in tasks should be the same in every index
            Player player = tasks.getFirst().player();
            PlayerSkillsAttachment skills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());

            for (BreakTask current : tasks) {
                int logs = calculateLogs(current.player());
                current.player().getInventory().add(new ItemStack(current.state().getBlock(), logs));
            }

            skills.addExp(PlayerSkillsAttachment.Skill.FORAGING, tasks.size() * 6.0f);
            player.syncData(PLAYER_SKILLS);
            UnshatteredSounds.playerExperienceSound(player);
        }
    }

    private static int calculateLogs(Player player) {
        var attribute = player.getAttribute(ModAttributes.FORAGING_FORTUNE.holder);
        // making sure attribute isnt null, probably pointless but i also managed to fuck up registering it last time and it made the game crash
        double fortune = attribute != null ? attribute.getValue() : 0.0;
        double multiplier = 1.0 + (fortune / 100.0);
        int guaranteedMultiplier = (int) multiplier;
        int finalMultiplier = guaranteedMultiplier;

        // todo: fix something about this (not giving giving bonus drops aside from the guaranteed)
        if (RANDOM.nextDouble() < (multiplier - guaranteedMultiplier)) {
            finalMultiplier++;
        }

        return finalMultiplier;
    }

    public static void trySweep(Level level, BlockPos startPos, Player player) {
        PlayerSkillsAttachment skills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());
        BlockState startBlock = level.getBlockState(startPos);

        // removing the initial block (as the vanilla block break is canceled)
        level.removeBlock(startPos, false);

        int sweep = (int) player.getAttributeValue(ModAttributes.SWEEP.holder);
        if (sweep <= 0) {
            skills.addExp(PlayerSkillsAttachment.Skill.FORAGING, 6.0f);
            player.syncData(PLAYER_SKILLS);
            UnshatteredSounds.playerExperienceSound(player);
            player.getInventory().add(new ItemStack(startBlock.getBlock(), calculateLogs(player)));
            return;
        }

        Queue<BreakTask> result = breakConnectedLogs(level, startPos, player, sweep);
        result.add(new BreakTask(level, startPos, player, startBlock));
        ACTIVE_BREAKS.add(new TreeBreakInstance(new ArrayList<>(result)));
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

    @SubscribeEvent
    private static void onServerTickEvent(ServerTickEvent.Post event) {
        // so multiplayer doesnt get all wild and wacky with everyone having the same queue
        Iterator<TreeBreakInstance> iterator = ACTIVE_BREAKS.iterator();

        while (iterator.hasNext()) {
            TreeBreakInstance instance = iterator.next();
            boolean finished = instance.tick();

            if (finished) {
                iterator.remove();
            }
        }
    }
}