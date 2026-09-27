package io.github.moosyu.items.weapons.shortbows;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import net.minecraft.server.level.ServerLevel;
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
        super(properties.component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.SHORTBOW));
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, Player player, @NonNull InteractionHand hand) {
        ItemStack weapon = player.getItemInHand(hand);

        if (player.getCooldowns().isOnCooldown(weapon)) {
            return InteractionResult.FAIL;
        }

        ItemStack ammo = getHeldProjectile(player, getAllSupportedProjectiles());
        boolean infinite = player.hasInfiniteMaterials();

        if (ammo.isEmpty()) {
            if (!infinite) {
                return InteractionResult.FAIL;
            }
            ammo = new ItemStack(Items.ARROW);
        }

        List<ItemStack> projectiles = draw(weapon, ammo, player);
        if (projectiles.isEmpty()) {
            return InteractionResult.FAIL;
        }

        if (level instanceof ServerLevel serverLevel) {
            shoot(serverLevel, player, hand, weapon, projectiles, 3.0f, 1.0f, false, null);
            player.getCooldowns().addCooldown(weapon, 2);
        }

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
