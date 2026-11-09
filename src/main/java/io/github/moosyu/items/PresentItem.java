package io.github.moosyu.items;

import io.github.moosyu.data.UnshatteredStatistics;
import io.github.moosyu.entities.decoration.present.Present;
import io.github.moosyu.entities.decoration.present.PresentTypes;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class PresentItem extends Item {
    private final Holder<PresentTypes.PresentType> presentType;

    public PresentItem(Properties properties, Holder<PresentTypes.PresentType> presentType) {
        super(properties);
        this.presentType = presentType;
    }

    @Override
    public @NonNull InteractionResult useOn(@NonNull UseOnContext context) {
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.FAIL;
        }

        ItemStack stack = context.getItemInHand();
        if (player.getCooldowns().isOnCooldown(stack)) {
            return InteractionResult.PASS;
        }

        player.getCooldowns().addCooldown(stack, Present.DURATION);
        stack.consume(1, player);

        Level level = context.getLevel();
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        if (player instanceof ServerPlayer serverPlayer) {
            Present present = new Present(level, EntityReference.of(player), presentType.value());
            Vec3 pos = context.getClickedPos().getCenter();
            present.setPos(pos.x, pos.y + 1, pos.z);
            level.addFreshEntity(present);
            serverPlayer.awardStat(UnshatteredStatistics.GIFTS_OPENED.get());
        }
        return InteractionResult.SUCCESS;
    }
}