package io.github.moosyu.items;

import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.registers.AttachmentRegistry;
import io.github.moosyu.sounds.ModSounds;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import java.util.*;

public class TreecapitatorItem extends AxeItem {
    // change later when sweep is added
    private static final int MAX_LOGS = 35;

    public TreecapitatorItem(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (entity instanceof Player player && state.is(BlockTags.LOGS)) {
            if (!level.isClientSide) {
                int brokenLogs = breakConnectedLogs(level, pos, entity).size();
                // +1 because the original log gets broken normally
                int totalLogs = brokenLogs + 1;
                PlayerSkillsAttachment skills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());
                skills.addForagingExp(totalLogs * 6.0f);
                ModSounds.playerExperienceSound(player);
            }
        }
        return super.mineBlock(stack, level, state, pos, entity);
    }

    private List<BlockPos> breakConnectedLogs(Level level, BlockPos startPos, LivingEntity player) {
        Queue<Long> queue = new LinkedList<>();
        LongOpenHashSet visited = new LongOpenHashSet();
        List<BlockPos> toBreak = new ArrayList<>();

        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        queue.add(startPos.asLong());
        visited.add(startPos.asLong());

        while (!queue.isEmpty() && toBreak.size() < MAX_LOGS) {
            long currentLong = queue.poll();
            BlockPos currentPos = BlockPos.of(currentLong);

            // this 3d business is messed up, looping through 26 is pretty slow so that's why im getting all wacky and wild
            // by using mutablePos and LongOpenHashSet (longs are primatives so faster). still not fast but not the worst thing ever
            // also future me don't change this to 0 -> < 2, i know it looks weird but it needs to be this way for MutableBlockPos#move
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && y == 0 && z == 0) continue;

                        mutablePos.set(currentPos).move(x, y, z);
                        long neighborLong = mutablePos.asLong();

                        if (!visited.contains(neighborLong)) {
                            visited.add(neighborLong);
                            BlockState state = level.getBlockState(mutablePos);
                            if (state.is(BlockTags.LOGS)) {
                                queue.add(neighborLong);
                                toBreak.add(mutablePos.immutable());
                            }
                        }
                    }
                }
            }
        }

        // to add: some sort of tick delay (maybe just 1 tick or so) between logs breaking like the real thing
        // id imagine this is done with ServerTickEvent so yeah idk figure that out
        for (BlockPos pos : toBreak) {
            level.destroyBlock(pos, true, player);
        }

        return toBreak;
    }
}