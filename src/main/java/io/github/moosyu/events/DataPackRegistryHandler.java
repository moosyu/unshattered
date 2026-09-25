package io.github.moosyu.events;

import io.github.moosyu.data.dialogue.DialogueTree;
import io.github.moosyu.data.fishing.FishingRewardTypes;
import io.github.moosyu.data.fishing.TriggerMiscReward;
import io.github.moosyu.data.quests.Quest;
import io.github.moosyu.data.regen.RegenPaths.RegenPath;
import io.github.moosyu.data.regions.Region;
import io.github.moosyu.data.regions.RegionBoundary;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class DataPackRegistryHandler {
    public static final ResourceKey<Registry<Region>> REGION_REGISTRY_KEY = ResourceKey.createRegistryKey(UnshatteredUtils.getUnshatteredIdentifier("regions"));
    public static final ResourceKey<Registry<RegionBoundary>> REGION_BOUNDARY_REGISTRY_KEY = ResourceKey.createRegistryKey(UnshatteredUtils.getUnshatteredIdentifier("region_boundaries"));
    public static final ResourceKey<Registry<DialogueTree>> DIALOGUE_TREE_REGISTRY_KEY = ResourceKey.createRegistryKey(UnshatteredUtils.getUnshatteredIdentifier("dialogue_trees"));
    public static final ResourceKey<Registry<Quest>> QUEST_REGISTRY_KEY = ResourceKey.createRegistryKey(UnshatteredUtils.getUnshatteredIdentifier("quests"));
    public static final ResourceKey<Registry<RegenPath>> REGEN_PATH_REGISTRY_KEY = ResourceKey.createRegistryKey(UnshatteredUtils.getUnshatteredIdentifier("regen_paths"));
    public static final ResourceKey<Registry<TriggerMiscReward>> FISHING_MISC_REWARD_KEY = ResourceKey.createRegistryKey(UnshatteredUtils.getUnshatteredIdentifier("fishing_misc_rewards"));

    @SubscribeEvent
    public static void registerDatapackRegistries(DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(
                REGION_REGISTRY_KEY,
                Region.CODEC,
                Region.CODEC
        );

        event.dataPackRegistry(
                REGION_BOUNDARY_REGISTRY_KEY,
                RegionBoundary.CODEC
        );

        event.dataPackRegistry(
                DIALOGUE_TREE_REGISTRY_KEY,
                DialogueTree.CODEC
        );

        event.dataPackRegistry(
                QUEST_REGISTRY_KEY,
                Quest.CODEC
        );

        event.dataPackRegistry(
                REGEN_PATH_REGISTRY_KEY,
                RegenPath.CODEC,
                RegenPath.CODEC
        );

        event.dataPackRegistry(
                FISHING_MISC_REWARD_KEY,
                FishingRewardTypes.CODEC
        );
    }
}
