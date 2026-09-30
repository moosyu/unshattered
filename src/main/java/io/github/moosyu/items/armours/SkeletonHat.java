package io.github.moosyu.items.armours;

import io.github.moosyu.abilities.AbilityContext;
import io.github.moosyu.abilities.AbilityContextKey;
import io.github.moosyu.abilities.AbilityTriggerType;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.damage.DamageUtils;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemType;
import io.github.moosyu.abilities.PassiveAbilityItem;
import io.github.moosyu.items.UnshatteredArmourMaterials;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.phys.AABB;

import java.util.Optional;
import java.util.Set;

public class SkeletonHat extends Item implements PassiveAbilityItem {
    private static final Identifier ABILITY_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("skeleton_hat_explosive_arrows");
    public static ResourceKey<EquipmentAsset> SKELETON_HAT_KEY = ResourceKey.create(UnshatteredArmourMaterials.ROOT_ID, UnshatteredUtils.getUnshatteredIdentifier("skeleton_hat"));

    public SkeletonHat(Properties properties) {
        super(properties.component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemType.HELMET)
                .component(DataComponents.EQUIPPABLE,
                        Equippable.builder(EquipmentSlot.HEAD)
                                .setEquipSound(BuiltInRegistries.SOUND_EVENT.wrapAsHolder(SoundEvents.SKELETON_AMBIENT))
                                .setAsset(SKELETON_HAT_KEY)
                                .build()
                )
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 8)
                .component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(ABILITY_IDENTIFIER, 0, 0, 0, true))
                .attributes(ItemAttributeModifiers.builder()
                        .add(Attributes.MOVEMENT_SPEED,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("skeleton_hat_speed"),
                                        0.01,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HEAD
                        ).add(UnshatteredAttributeValues.MANA.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("skeleton_hat_mana"),
                                        3,
                                        AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.HEAD
                        ).build()
                )
        );
    }

    @Override
    public void onAbilityTriggered(AbilityContext context) {
        Optional<ServerPlayer> player = context.get(AbilityContextKey.PLAYER);
        Optional<LivingEntity> target = context.get(AbilityContextKey.TARGET);

        if (player.isPresent() && target.isPresent()) {
            Optional<AttributeInstance> finalDamageAttribute = UnshatteredUtils.getAttributeInstance(player.get(), UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder);
            finalDamageAttribute.ifPresent(attribute ->
                    attribute.addTransientModifier(new AttributeModifier(
                            ABILITY_IDENTIFIER,
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
            for (Entity entity : player.get().level().getEntities(null, boundingBox)) {
                if (entity instanceof LivingEntity livingEntity && !(livingEntity instanceof Player) && livingEntity != target.get()) {
                    DamageUtils.playerDealDamage(player.get(),
                            livingEntity,
                            player.get().getAttributeValue(UnshatteredAttributeValues.DAMAGE.holder),
                            false,
                            player.get().getMainHandItem(),
                            false
                    );

                    player.get().level().sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
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
                player.get().level().playSound(null,
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
    public void onAbilityFinished(AbilityContext context) {
        context.get(AbilityContextKey.PLAYER).flatMap(player -> UnshatteredUtils.getAttributeInstance(player,
                UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder)
        ).ifPresent(attribute -> attribute.removeModifier(ABILITY_IDENTIFIER));
    }

    @Override
    public boolean abilityConditionsMet(AbilityContext context) {
        return (context.get(AbilityContextKey.ITEM_TYPE).map(itemType -> itemType == ItemType.BOW
                || itemType == ItemType.SHORTBOW).orElse(false))
                && context.get(AbilityContextKey.PLAYER).map(player -> player.getRandom().nextInt(5) == 0).orElse(false);
    }

    @Override
    public Set<AbilityTriggerType> triggerTypes() {
        return Set.of(AbilityTriggerType.PLAYER_DEAL_DAMAGE);
    }
}
