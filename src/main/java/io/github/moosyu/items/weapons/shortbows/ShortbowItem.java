package io.github.moosyu.items.weapons.shortbows;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.function.Predicate;

public class ShortbowItem extends ProjectileWeaponItem {
    public ShortbowItem(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, Player player, @NonNull InteractionHand hand) {
        ItemStack weapon = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(weapon)) {
            return InteractionResult.FAIL;
        }

        ItemStack ammo = player.getProjectile(weapon);
        if (ammo.isEmpty()) {
            if (!player.hasInfiniteMaterials()) {
                return InteractionResult.FAIL;
            }
            ammo = new ItemStack(Items.ARROW);
        }

        // so the client doesnt try to re-equip the bow
        if (level.isClientSide()) {
            return InteractionResult.PASS;
        }

        List<ItemStack> projectiles = draw(weapon, ammo, player);
        if (projectiles.isEmpty()) {
            return InteractionResult.FAIL;
        }


        if (level instanceof ServerLevel serverLevel) {
            shoot(serverLevel, player, hand, weapon, projectiles, 3.0f, 1.0f, false, null);
        }

        level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0f, 1.0f / (level.getRandom().nextFloat() * 0.4f + 1.2f) + 0.5f);
        // for whatever reason cooldowns that arent multiples of four get oddly out of sync sometimes and fire like twice in one tick.
        player.getCooldowns().addCooldown(weapon, 8);
        player.awardStat(Stats.ITEM_USED.get(this));
        
        return InteractionResult.CONSUME;
    }

    // yeah idk what to do about this, its abstract so i kind of have to override it though
    @SuppressWarnings("deprecation")
    @Override
    public @NonNull Predicate<ItemStack> getAllSupportedProjectiles() {
        return ARROW_ONLY;
    }


    @Override
    public int getDefaultProjectileRange() {
        return 15;
    }

    @Override
    protected void shootProjectile(@NonNull LivingEntity shooter, Projectile projectileEntity, int index, float power, float uncertainty, float angle, LivingEntity targetOverrride) {
        projectileEntity.shootFromRotation(shooter, shooter.getXRot(), shooter.getYRot() + angle, 0.0f, power, uncertainty);
    }
}