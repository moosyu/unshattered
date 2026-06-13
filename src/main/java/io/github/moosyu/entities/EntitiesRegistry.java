package io.github.moosyu.mobs;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.Unshattered.MODID;

public class MobsRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.createEntities(MODID);

    public static final ResourceKey<EntityType<?>> GRAVEYARD_ZOMBIE_VILLAGER = ResourceKey.create(Registries.ENTITY_TYPE, Identifier.fromNamespaceAndPath(MODID, "graveyard_zombie_villager"));
}
