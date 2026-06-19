package io.github.moosyu.entities;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
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
                            Identifier.fromNamespaceAndPath(MODID, "graveyard_zombie_villager")
                    ))
    );
}
