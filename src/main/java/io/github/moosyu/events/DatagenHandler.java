package io.github.moosyu.events;

import io.github.moosyu.blocks.TalkingRockBlock;
import io.github.moosyu.data.dialogue.*;
import io.github.moosyu.data.quests.Quest;
import io.github.moosyu.data.quests.QuestTypes;
import io.github.moosyu.data.regions.*;
import io.github.moosyu.data.datagen.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class DatagenHandler {
    @SubscribeEvent
    public static void onGatherData(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new UnshatteredModelProvider(packOutput));
        generator.addProvider(true, new EquipmentAssets(packOutput));
        generator.addProvider(true, new UnshatteredBlockTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new UnshatteredItemTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new UnshatteredEntityTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new UnshatteredDataMapProvider(packOutput, lookupProvider));
        generator.addProvider(true, new UnshatteredSoundDefinitionsProvider(packOutput));

        event.createDatapackRegistryObjects(
                new RegistrySetBuilder().add(DataPackRegistryHandler.REGION_REGISTRY_KEY, bootstrap -> {
                    bootstrap.register(UnshatteredRegions.PLAINS_REGION, new Region(0xFF71AD1C, RegionTemperatureTypes.COMFORTABLE, false));
                    bootstrap.register(UnshatteredRegions.DEFAULT_REGION, new Region(0xFFFFFFFF, RegionTemperatureTypes.COLD, false));
                    bootstrap.register(UnshatteredRegions.FORGOTTEN_DWELLING, new Region(0xFFCCD9E3, RegionTemperatureTypes.COMFORTABLE, false));
                    bootstrap.register(UnshatteredRegions.HYTHE, new Region(0xFFB1FA96, RegionTemperatureTypes.COMFORTABLE, false));
                }).add(DataPackRegistryHandler.REGION_BOUNDARY_REGISTRY_KEY, bootstrap -> {
                    HolderGetter<Region> regions = bootstrap.lookup(DataPackRegistryHandler.REGION_REGISTRY_KEY);

                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(-2048, -2048), new Vector2i(2048, 2048), 0), UnshatteredRegions.DEFAULT_REGION, regions);
                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(-846, 688), new Vector2i(-1119, 825), 1), UnshatteredRegions.PLAINS_REGION, regions);
                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(-1035, 837), new Vector2i(-1070, 805), 2), UnshatteredRegions.FORGOTTEN_DWELLING, regions);
                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(-864, 707), new Vector2i(-937, 792), 2), UnshatteredRegions.HYTHE, regions);

                }).add(DataPackRegistryHandler.DIALOGUE_TREE_REGISTRY_KEY, bootstrap -> {
                    registerDialogueTree(bootstrap,
                            TalkingRockBlock.ROCK_DIALOGUE_TREE,
                            new DialogueTree(List.of(
                                    createDialogueOriginWithSelfFlag(
                                    0,
                                    new DialogueNode(Component.literal("hi, im a rock"),
                                            List.of(new DialogueChoice(Component.literal("interesting"),
                                                    new DialogueNode(Component.literal("im glad you think so :)"))),
                                                    new DialogueChoice(Component.literal("..."))
                                            )
                                    ),
                                    TalkingRockBlock.HI_MESSAGE_IDENTIFIER),
                                    createDialogueOriginWithSelfFlag(1,
                                            new DialogueNode(Component.literal("you've already spoken to me"),
                                                    List.of(new DialogueChoice(Component.literal("i know right"),
                                                            new GiveItemDialogueEvent(BuiltInRegistries.ITEM.wrapAsHolder(Items.DIAMOND), 1))
                                                    )
                                            ),
                                            List.of(TalkingRockBlock.HI_MESSAGE_IDENTIFIER),
                                            List.of(),
                                            List.of(),
                                            TalkingRockBlock.HI2_MESSAGE_IDENTIFIER
                                    ),
                                    createDialogueOrigin(2,
                                            new DialogueNode(Component.literal("find my pages"),
                                                    List.of(new DialogueChoice(Component.literal("i guess"), List.of(TalkingRockBlock.ROCKS_QUEST), new StartQuestDialogueEvent(TalkingRockBlock.ROCKS_QUEST)),
                                                            new DialogueChoice(Component.literal("no thanks")))
                                            ),
                                            List.of(TalkingRockBlock.HI2_MESSAGE_IDENTIFIER),
                                            List.of(TalkingRockBlock.ROCKS_QUEST)
                                    )
                            ))
                    );
                }).add(DataPackRegistryHandler.QUEST_REGISTRY_KEY, bootstrap -> {
                    registerQuest(bootstrap, TalkingRockBlock.ROCKS_QUEST, QuestTypes.NOVICE, new GiveItemDialogueEvent(BuiltInRegistries.ITEM.wrapAsHolder(Items.STONE), 1));
                })
        );
    }

    public static void registerRegionBoundary(BootstrapContext<RegionBoundary> bootstrap, BoundaryCoordinates boundaryCoordinates, ResourceKey<Region> region, HolderGetter<Region> regions) {
        Holder<Region> regionHolder = regions.getOrThrow(region);

        bootstrap.register(
                ResourceKey.create(DataPackRegistryHandler.REGION_BOUNDARY_REGISTRY_KEY, Identifier.fromNamespaceAndPath(MODID, region.identifier().getPath() + "_bounds")),
                new RegionBoundary(regionHolder, boundaryCoordinates)
        );
    }

    public static void registerDialogueTree(BootstrapContext<DialogueTree> bootstrap, Identifier dialogueTreeIdentifier, DialogueTree dialogueTree) {
        bootstrap.register(ResourceKey.create(DataPackRegistryHandler.DIALOGUE_TREE_REGISTRY_KEY, dialogueTreeIdentifier), dialogueTree);
    }


    public static DialogueTreeOrigin createDialogueOrigin(int priority, DialogueNode dialogueNode, List<Identifier> requiredFlags, List<Identifier> excludedFlags) {
        return new DialogueTreeOrigin(priority, dialogueNode, Optional.of(new DialogueFlagRequirements(requiredFlags, excludedFlags)), List.of());
    }

    /**
     * @return a dialogue tree origin with itself as a flag along with any extra flags to be tracked
     */
    public static DialogueTreeOrigin createDialogueOriginWithSelfFlag(int priority, DialogueNode dialogueNode, List<Identifier> requiredFlags, List<Identifier> excludedFlags, List<Identifier> extraSetFlags, Identifier treeOriginIdentifier) {
        List<Identifier> combinedSetFlags = new ArrayList<>(1 + extraSetFlags.size());
        combinedSetFlags.add(treeOriginIdentifier);
        combinedSetFlags.addAll(extraSetFlags);

        return new DialogueTreeOrigin(priority, dialogueNode, Optional.of(new DialogueFlagRequirements(requiredFlags, excludedFlags)), combinedSetFlags);
    }

    /**
     * @return a dialogue tree origin with itself as a flag to be tracked
     */
    public static DialogueTreeOrigin createDialogueOriginWithSelfFlag(int priority, DialogueNode dialogueNode, Identifier treeOriginIdentifier) {
        return new DialogueTreeOrigin(priority, dialogueNode, List.of(treeOriginIdentifier));
    }

    public static void registerQuest(BootstrapContext<Quest> bootstrap, Identifier questIdentifier, QuestTypes questTypes, DialogueTriggeredEvent questCompleteEvent) {
        bootstrap.register(ResourceKey.create(DataPackRegistryHandler.QUEST_REGISTRY_KEY, questIdentifier), new Quest(questTypes, Optional.of(questCompleteEvent)));
    }

    public static void registerQuest(BootstrapContext<Quest> bootstrap, Identifier questIdentifier, QuestTypes questTypes) {
        bootstrap.register(ResourceKey.create(DataPackRegistryHandler.QUEST_REGISTRY_KEY, questIdentifier), new Quest(questTypes));
    }
}
