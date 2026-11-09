package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.Ability;
import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.attributes.UnshatteredAttributeValue;
import io.github.moosyu.damage.DamageUtils;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class Cleave extends Ability {
    // players currently in the middle of a cleave so its known when a cleave is active
    private static final Set<UUID> CLEAVING = new HashSet<>();

    private final float radius;
    private final float cleaveDamageFraction;

    public Cleave(float radius, float cleaveDamageFraction) {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_DEAL_DAMAGE);

        this.radius = radius;
        this.cleaveDamageFraction = cleaveDamageFraction;
    }

    @Override
    public void onTrigger(AbilityContext context) {
        if (context.get(AbilityContextKey.PLAYER).isEmpty() || context.get(AbilityContextKey.TARGET).isEmpty()) {
            return;
        }

        Optional<ServerPlayer> optionalServerPlayer = context.getServerPlayer();
        if (optionalServerPlayer.isEmpty()) {
            return;
        }

        ServerPlayer player = optionalServerPlayer.get();
        LivingEntity target = context.get(AbilityContextKey.TARGET).get();
        Level level = player.level();

        if (level.isClientSide() || !CLEAVING.add(player.getUUID())) {
            return;
        }

        Optional<AttributeInstance> optionalFinalDamageAttribute = UnshatteredUtils.getAttributeInstance(player, UnshatteredAttributeValue.FINAL_DAMAGE_MODIFIER.holder);

        try {
            AABB boundingBox = new AABB(
                    target.getX() - radius,
                    target.getY() - radius,
                    target.getZ() - radius,
                    target.getX() + 1 + radius,
                    target.getY() + 1 + radius,
                    target.getZ() + 1 + radius
            );

            optionalFinalDamageAttribute.ifPresent(attribute -> {
                attribute.removeModifier(id());
                attribute.addTransientModifier(new AttributeModifier(
                        id(),
                        -(attribute.getValue() - (1 - cleaveDamageFraction)),
                        AttributeModifier.Operation.ADD_VALUE
                ));
            });

            // this causes knockback to cleaved enemies which isn't really what i wanted, but we'll see if i ever fix it
            for (Entity entity : level.getEntities(null, boundingBox)) {
                if (entity instanceof LivingEntity livingEntity && !(livingEntity instanceof Player) && livingEntity != target) {
                    DamageUtils.playerDealDamage(player,
                            livingEntity,
                            player.getAttributeValue(UnshatteredAttributeValue.DAMAGE.holder),
                            false,
                            player.getMainHandItem(),
                            false
                    );
                }
            }
        } finally {
            optionalFinalDamageAttribute.ifPresent(attribute -> attribute.removeModifier(id()));
            CLEAVING.remove(player.getUUID());
        }
    }

    @Override
    public void onFinish(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).flatMap(
                player -> UnshatteredUtils.getAttributeInstance(player,
                        UnshatteredAttributeValue.FINAL_DAMAGE_MODIFIER.holder)
        ).ifPresent(attribute -> attribute.removeModifier(id()));
    }
}