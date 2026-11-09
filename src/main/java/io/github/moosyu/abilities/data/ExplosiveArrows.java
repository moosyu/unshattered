package io.github.moosyu.abilities.data;

import io.github.moosyu.abilities.*;
import io.github.moosyu.attributes.UnshatteredAttributeValue;
import io.github.moosyu.damage.DamageUtils;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

import java.util.Optional;

public class ExplosiveArrows extends Ability {
    public ExplosiveArrows() {
        super(new ItemAbility(), AbilityTriggerType.PLAYER_DEAL_DAMAGE);
    }

    @Override
    public boolean conditionsMet(AbilityContext context) {
        return (context.get(AbilityContextKey.ITEM_TYPE).map(itemType -> itemType == ItemType.BOW
                || itemType == ItemType.SHORTBOW).orElse(false)
        ) && context.get(AbilityContextKey.PLAYER).map(player -> player.getRandom().nextInt(5) == 0).orElse(false);
    }

    @Override
    public void onTrigger(AbilityContext context) {
        Optional<ServerPlayer> optionalServerPlayer = context.getServerPlayer();
        if (optionalServerPlayer.isEmpty()) {
            return;
        }

        ServerPlayer player = optionalServerPlayer.get();
        Optional<LivingEntity> target = context.get(AbilityContextKey.TARGET);

        if (target.isPresent()) {
            Optional<AttributeInstance> finalDamageAttribute = UnshatteredUtils.getAttributeInstance(player, UnshatteredAttributeValue.FINAL_DAMAGE_MODIFIER.holder);
            finalDamageAttribute.ifPresent(attribute ->
                    attribute.addTransientModifier(new AttributeModifier(
                            id(),
                            -(attribute.getValue() - 0.8),
                            AttributeModifier.Operation.ADD_VALUE
                    ))
            );

            AABB boundingBox = new AABB(target.get().getX() - 4,
                    target.get().getY(),
                    target.get().getZ() - 4,
                    target.get().getX() + 4,
                    target.get().getY() + 3,
                    target.get().getZ() + 4
            );

            int validEntities = 0;
            for (Entity entity : player.level().getEntities(null, boundingBox)) {
                if (entity instanceof LivingEntity livingEntity && !(livingEntity instanceof Player) && livingEntity != target.get()) {
                    DamageUtils.playerDealDamage(player,
                            livingEntity,
                            player.getAttributeValue(UnshatteredAttributeValue.DAMAGE.holder),
                            false,
                            player.getMainHandItem(),
                            false
                    );

                    player.level().sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                            livingEntity.getX(),
                            livingEntity.getY() + 0.2,
                            livingEntity.getZ(),
                            10,
                            0.15,
                            0.25,
                            0.15,
                            0.1
                    );

                    validEntities++;
                }
            }

            if (validEntities > 0) {
                player.level().playSound(null,
                        target.get().blockPosition(),
                        SoundEvents.SKELETON_HURT,
                        SoundSource.PLAYERS,
                        1.0f,
                        1.0f
                );
            }
        }
    }

    @Override
    public void onFinish(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).flatMap(player -> UnshatteredUtils.getAttributeInstance(player,
                UnshatteredAttributeValue.FINAL_DAMAGE_MODIFIER.holder)
        ).ifPresent(attribute -> attribute.removeModifier(id()));
    }
}
