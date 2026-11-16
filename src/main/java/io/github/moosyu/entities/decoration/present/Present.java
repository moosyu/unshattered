package io.github.moosyu.entities.decoration.present;

import io.github.moosyu.abilities.*;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.drops.*;
import io.github.moosyu.entities.UnshatteredEntities;
import io.github.moosyu.util.UnshatteredUtils;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class Present extends Entity {
    // in ticks
    public static final int DURATION = 100;
    public static final float MAX_HEIGHT = 2.5f;
    // mightve overshot idk just didnt wanna overlap with vanilla
    // assuming https://minecraft.wiki/w/Java_Edition_protocol/Entity_statuses#Projectile is real this should be a good gap though i reckon
    public static final byte FINALE_EVENT_ID = 100;
    public static final EntityDataAccessor<String> TEXTURE_PATH = SynchedEntityData.defineId(Present.class, EntityDataSerializers.STRING);
    private static final int[] SCALE = {0, 2, 4, 5, 7, 9, 11, 12, 14, 16, 17, 19, 21, 23, 24};

    @Nullable public final EntityReference<Entity> owner;
    private final PresentTypes.@Nullable PresentType presentType;

    public Present(EntityType<? extends Entity> type, Level level) {
        super(type, level);

        owner = null;
        presentType = null;
    }

    public Present(Level level, @Nullable EntityReference<Entity> owner, PresentTypes.PresentType presentType) {
        super(UnshatteredEntities.PRESENT.get(), level);

        this.owner = owner;
        this.presentType = presentType;
        entityData.set(TEXTURE_PATH, presentType.texture().getPath());
    }

    private int noteCooldown = 0;

    public static float getLiftHeight(float progress) {
        // so itll ease out, (1-x)^2
        float eased = 1 - Mth.square(1 - progress);
        return eased * MAX_HEIGHT;
    }

    @Override
    public void tick() {
        super.tick();
        float progress = Math.min(tickCount / (float) DURATION, 1f);

        if (level().isClientSide()) {
            spawnParticles(progress);
        } else {
            if (--noteCooldown > 0) {
                return;
            }

            noteCooldown = Math.round(Mth.lerp(progress, 8f, 2f));

            int semitone = SCALE[Math.min((int) (progress * SCALE.length), SCALE.length - 1)];
            float pitch = (float) Math.pow(2.0, (semitone - 12) / 12.0);
            double y = getLiftedY(progress);

            level().playSound(null, getX(), y, getZ(), SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.NEUTRAL, 1.0f, pitch);
            level().playSound(null, getX(), y, getZ(), SoundEvents.NOTE_BLOCK_BELL.value(), SoundSource.NEUTRAL, 0.6f, pitch);

            if (tickCount >= DURATION) {
                level().playSound(null, getX(), getLiftedY(1.0f), getZ(), SoundEvents.NOTE_BLOCK_PLING.value(), SoundSource.NEUTRAL, 1.5f, 2.0f);
                level().broadcastEntityEvent(this, FINALE_EVENT_ID);

                if (owner != null && presentType != null) {
                    if (level().getEntity(owner.getUUID()) instanceof ServerPlayer player) {
                        AbilityContext context = new AbilityContext().add(AbilityContextKey.PLAYER, player).add(AbilityContextKey.PRESENT_TYPE, presentType);

                        List<WeightedDropData> presentData = PresentTypes.REGISTRY.wrapAsHolder(presentType).getData(UnshatteredDataMaps.PRESENT_DATA);
                        AbilityResults abilityResults = AbilityScope.fire(player, AbilityTriggerType.PLAYER_FINISH_OPENING_PRESENT, context);

                        if (presentData != null && !presentData.isEmpty() && !abilityResults.get(AbilityResult.CANCEL_EVENT)) {
                            int totalWeight = 0;
                            for (WeightedDropData data : presentData) {
                                totalWeight += data.weight();
                            }

                            if (totalWeight > 0) {
                                RandomSource random = player.getRandom();
                                int roll = random.nextInt(totalWeight);
                                int cumulative = 0;

                                for (WeightedDropData data : presentData) {
                                    cumulative += data.weight();
                                    if (roll < cumulative) {
                                        // multiplying the weight so the odds look less wack. honestly at this point it'd make more sense to hardcode them but we're well past that point
                                        DropType dropType = DropType.getDropType( (float) (data.weight() * 10) / totalWeight);
                                        Component dropTypeComponent = Component.empty().append(Component.literal(Component.translatable("drop_type.message.unshattered." + dropType.key).getString().toUpperCase(Locale.ROOT)).withStyle(ChatFormatting.BOLD).withColor(dropType.colour));

                                        switch (data.reward()) {
                                            case ItemReward itemReward -> {
                                                Item item = itemReward.itemRange().item();
                                                String count = itemReward.formatAmount(data.reward().grant(player, player.getRandom(), null, 1.0f));

                                                player.sendSystemMessage(Component.empty()
                                                        .append(dropTypeComponent)
                                                        .append(Component.literal(" "))
                                                        .append(Component.translatable(item.getDescriptionId()).withColor(UnshatteredUtils.getItemRarity(item.getDefaultInstance()).getColour()))
                                                        .append(Objects.equals(count, "1") ? Component.empty() : Component.literal(" x" + count).withColor(UnshatteredUtils.DARK_GRAY))
                                                );
                                            }
                                            case CoinReward coinReward -> {
                                                // unfortunately leads to the rewards being displayed after things like level up messages, what can you do i guess
                                                float modifiedPresentCoinsPercent = abilityResults.get(AbilityResult.ADD_PRESENT_COINS_PERCENT);
                                                float rewardAmount = data.reward().grant(player, player.getRandom(), null, modifiedPresentCoinsPercent);

                                                player.sendSystemMessage(Component.empty()
                                                        .append(dropTypeComponent)
                                                        .append(Component.literal(" +").withColor(UnshatteredUtils.GOLD))
                                                        .append(Component.literal(modifiedPresentCoinsPercent == 1.0f
                                                                ? coinReward.formatAmount(rewardAmount)
                                                                : coinReward.formatAmount(rewardAmount) + " (+" + coinReward.formatAmount((rewardAmount * modifiedPresentCoinsPercent) - rewardAmount) + ")").withColor(UnshatteredUtils.GOLD)
                                                        )
                                                        .append(Component.literal(" "))
                                                        .append(Component.translatable("misc.unshattered.coins").withColor(UnshatteredUtils.GOLD))
                                                        .append(Component.literal("!").withColor(UnshatteredUtils.YELLOW))
                                                );
                                            }
                                            case ExperienceReward experienceReward -> {
                                                float modifiedPresentExperiencePercent = abilityResults.get(AbilityResult.ADD_PRESENT_EXPERIENCE_PERCENT);
                                                float rewardAmount = data.reward().grant(player, player.getRandom(), null, modifiedPresentExperiencePercent);

                                                player.sendSystemMessage(Component.empty()
                                                        .append(dropTypeComponent)
                                                        .append(Component.literal(" +").withColor(UnshatteredUtils.DARK_AQUA))
                                                        .append(Component.literal(modifiedPresentExperiencePercent == 1.0f
                                                                ? experienceReward.formatAmount(rewardAmount)
                                                                : experienceReward.formatAmount(rewardAmount) + " (+" + experienceReward.formatAmount((rewardAmount * modifiedPresentExperiencePercent) - rewardAmount) + ")").withColor(UnshatteredUtils.DARK_AQUA)
                                                        )
                                                        .append(Component.literal(" "))
                                                        .append(Component.translatable(experienceReward.skill().getTranslationKey()).withColor(UnshatteredUtils.DARK_AQUA))
                                                        .append(Component.literal(" "))
                                                        .append(Component.translatable("misc.unshattered.experience").withColor(UnshatteredUtils.DARK_AQUA))
                                                        .append(Component.literal("!").withColor(UnshatteredUtils.DARK_AQUA))
                                                );
                                            }
                                            default -> {}
                                        }

                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                discard();
            }
        }
    }

    private double getLiftedY(float progress) {
        return getY() + getLiftHeight(progress) + 0.4;
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == FINALE_EVENT_ID) {
            level().createFireworks(getX(),
                    getLiftedY(1.0f),
                    getZ(),
                    0,
                    0,
                    0,
                    List.of(new FireworkExplosion(FireworkExplosion.Shape.LARGE_BALL,
                                    IntList.of(0xE53935, 0xFFD54F),
                                    IntList.of(0xFFFFFF),
                                    true,
                                    true
                            ), new FireworkExplosion(FireworkExplosion.Shape.STAR,
                                    IntList.of(0x43A047),
                                    IntList.of(),
                                    false,
                                    true
                            )
                    )
            );
        } else {
            super.handleEntityEvent(id);
        }
    }

    private void spawnParticles(float progress) {
        RandomSource random = level().getRandom();
        // so the amount increases as time passes
        int count = 1 + (int) (progress * 4);
        for (int i = 0; i < count; i++) {
            level().addParticle(ParticleTypes.END_ROD,
                    getX() + (random.nextDouble() - 0.5) * 0.6, getLiftedY(progress), getZ() + (random.nextDouble() - 0.5) * 0.6,
                    (random.nextDouble() - 0.5) * 0.05, -0.02, (random.nextDouble() - 0.5) * 0.05);
        }
    }

    @Override
    public boolean hurtServer(@NonNull ServerLevel level, @NonNull DamageSource damageSource, float amount) {
        return false;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NonNull Builder builder) {
        builder.define(TEXTURE_PATH, "");
    }

    @Override
    protected void readAdditionalSaveData(@NonNull ValueInput valueInput) {}

    @Override
    protected void addAdditionalSaveData(@NonNull ValueOutput valueOutput) {}
}
