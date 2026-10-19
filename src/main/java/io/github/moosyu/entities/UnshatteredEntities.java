package io.github.moosyu.entities;

import io.github.moosyu.entities.mobs.*;
import io.github.moosyu.entities.npcs.*;
import io.github.moosyu.entities.projectiles.HomingBee;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.createEntities(MODID);

    public static final Supplier<EntityType<GraveyardZombieVillager>> GRAVEYARD_ZOMBIE_VILLAGER = ENTITY_TYPES.register(
            "graveyard_zombie_villager", () -> EntityType.Builder.of(GraveyardZombieVillager::new, MobCategory.MONSTER)
                    .sized(1.0f, 1.0f)
                    // A multiplicative factor (scalar) used by mobs that spawn in varying sizes.
                    // In vanilla, these are only slimes and magma cubes, both of which use 4.0f.
                    .spawnDimensionsScale(4.0f)
                    // The eye height, in blocks from the bottom of the size. Defaults to height * 0.85.
                    // This must be called after #sized to have an effect.
                    .eyeHeight(0.5f)
                    // Prevents the entity from being saved to disk.
                    .noSave()
                    // Disables a rule in the spawn handler that limits the distance at which entities can spawn.
                    // This means that no matter the distance to the player, this entity can spawn.
                    // Vanilla enables this for pillagers and shulkers.
                    .canSpawnFarFromPlayer()
                    // The range in which the entity is kept loaded by the client, in chunks.
                    // Vanilla values for this vary, but it's often something around 8 or 10. Defaults to 5.
                    // Be aware that if this is greater than the client's chunk view distance,
                    // then that chunk view distance is effectively used here instead.
                    .clientTrackingRange(8)
                    // How often update packets are sent for this entity, in once every x ticks. This is set to higher values
                    // for entities that have predictable movement patterns, for example projectiles. Defaults to 3.
                    .updateInterval(10)
                    // Build the entity type using a resource key. The second parameter should be the same as the entity id.
                    .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,
                            UnshatteredUtils.getUnshatteredIdentifier("graveyard_zombie_villager"))
                    )
    );

    public static final Supplier<EntityType<HomingBee>> HOMING_BEE = ENTITY_TYPES.register(
            "homing_bee",
            () -> EntityType.Builder.<HomingBee>of(HomingBee::new, MobCategory.MISC)
                    .sized(0.7f, 0.6f)
                    .eyeHeight(0.3f)
                    .updateInterval(1)
                    .noSave()
                    .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,
                            UnshatteredUtils.getUnshatteredIdentifier("homing_bee"))
                    )
    );

    public static final Supplier<EntityType<JotraelineGreatforgeNPC>> JOTRAELINE_GREATFORGE = ENTITY_TYPES.register(
            "jotraeline_greatforge",
            () -> EntityType.Builder.of(JotraelineGreatforgeNPC::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.8f)
                    .eyeHeight(1.62f)
                    .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,
                            JotraelineGreatforgeNPC.JOTRAELINE_GREATFORGE_IDENTIFIER)
                    )
    );

    public static final Supplier<EntityType<WoolWeaverNPC>> WOOL_WEAVER = ENTITY_TYPES.register(
            "wool_weaver",
            () -> EntityType.Builder.of(WoolWeaverNPC::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.8f)
                    .eyeHeight(1.62f)
                    .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,
                            WoolWeaverNPC.WOOL_WEAVER_IDENTIFIER)
                    )
    );

    public static final Supplier<EntityType<ForgerNPC>> FORGER = ENTITY_TYPES.register(
            "forger",
            () -> EntityType.Builder.of(ForgerNPC::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.8f)
                    .eyeHeight(1.62f)
                    .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,
                            ForgerNPC.FORGER_IDENTIFIER)
                    )
    );

    public static final Supplier<EntityType<BubuNPC>> BUBU = ENTITY_TYPES.register(
            "bubu",
            () -> EntityType.Builder.of(BubuNPC::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.8f)
                    .eyeHeight(1.62f)
                    .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,
                            BubuNPC.BUBU_IDENTIFIER)
                    )
    );

    public static final Supplier<EntityType<JennyNPC>> JENNY = ENTITY_TYPES.register(
            "jenny",
            () -> EntityType.Builder.of(JennyNPC::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.8f)
                    .eyeHeight(1.62f)
                    .build(ResourceKey.create(
                            Registries.ENTITY_TYPE,
                            JennyNPC.JENNY_IDENTIFIER)
                    )
    );

    public static final Supplier<EntityType<SneakyCreeper>> SNEAKY_CREEPER = ENTITY_TYPES.register(
            "sneaky_creeper",
            () -> EntityType.Builder.of(SneakyCreeper::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.7f)
                    .clientTrackingRange(8)
                    .notInPeaceful()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, UnshatteredUtils.getUnshatteredIdentifier("sneaky_creeper")))
    );

    public static final Supplier<EntityType<LapisZombie>> LAPIS_ZOMBIE = ENTITY_TYPES.register(
            "lapis_zombie",
            () -> EntityType.Builder.of(LapisZombie::new, MobCategory.MONSTER)
                    .sized(0.6f, 1.95f)
                    .eyeHeight(1.74f)
                    .passengerAttachments(2.0125f)
                    .ridingOffset(-0.7f)
                    .clientTrackingRange(8)
                    .notInPeaceful()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, UnshatteredUtils.getUnshatteredIdentifier("lapis_zombie")))
    );

    public static final Supplier<EntityType<SlimeChild>> SLIME_CHILD = ENTITY_TYPES.register(
            "slime_child",
            () -> EntityType.Builder.of(SlimeChild::new, MobCategory.MONSTER)
                    .sized(1.04f, 1.04f)
                    .eyeHeight(0.65f)
                    .clientTrackingRange(10)
                    .notInPeaceful()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, UnshatteredUtils.getUnshatteredIdentifier("slime_child")))
    );

    public static final Supplier<EntityType<EmeraldSlime>> EMERALD_SLIME = ENTITY_TYPES.register(
            "emerald_slime",
            () -> EntityType.Builder.of(EmeraldSlime::new, MobCategory.MONSTER)
                    .sized(1.56f, 1.56f)
                    .eyeHeight(0.975f)
                    .clientTrackingRange(10)
                    .notInPeaceful()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, UnshatteredUtils.getUnshatteredIdentifier("emerald_slime")))
    );

    public static final Supplier<EntityType<MassiveEmeraldSlime>> MASSIVE_EMERALD_SLIME = ENTITY_TYPES.register(
            "massive_emerald_slime",
            () -> EntityType.Builder.of(MassiveEmeraldSlime::new, MobCategory.MONSTER)
                    .sized(4.16f, 4.16f)
                    .eyeHeight(2.6f)
                    .clientTrackingRange(10)
                    .notInPeaceful()
                    .build(ResourceKey.create(Registries.ENTITY_TYPE, UnshatteredUtils.getUnshatteredIdentifier("massive_emerald_slime")))
    );
}
