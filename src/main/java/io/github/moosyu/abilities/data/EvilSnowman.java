package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.ActiveAbility;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.sounds.UnshatteredSounds;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class EvilSnowman extends ActiveAbility {
    public EvilSnowman() {
        super(new ItemAbility(25, 3, 0));
    }

    @Override
    protected void onActivate(AbilityContext context, boolean refreshing) {
        context.getServerPlayer().ifPresent(player -> {
            HitResult hitResult = player.pick(64, 1.0f, false);

            if (hitResult.getType() != HitResult.Type.MISS) {
                Level level = player.level();
                Vec3 location = hitResult.getLocation();
                BlockPos base = BlockPos.containing(location);

                for (int offset = 5; offset >= 3; offset--) {
                    BlockPos candidate = base.above(offset);

                    if (!level.isInWorldBounds(candidate)) {
                        continue;
                    }

                    BlockState state = level.getBlockState(candidate);
                    // no collision shape if there is no block
                    if (state.getCollisionShape(level, candidate).isEmpty()) {
                        // placeholder, gonna be an icicle soon
                        level.setBlockAndUpdate(candidate, Blocks.BEDROCK.defaultBlockState());
                        level.playSound(null, candidate.getX(), candidate.getY(), candidate.getZ(), UnshatteredSounds.ICICLE_PLACE, SoundSource.PLAYERS, 1.0f, 1.0f);
                        return;
                    }
                }
            }

            player.sendSystemMessage(Component.translatable("combat.messages.unshattered.frosty_arm_failed").withColor(UnshatteredUtils.RED));
        });
    }
}
