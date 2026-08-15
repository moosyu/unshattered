package io.github.moosyu.events;

import io.github.moosyu.blocks.TestBlock;
import io.github.moosyu.data.dialogue.*;
import io.github.moosyu.data.regions.*;
import io.github.moosyu.datagen.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.data.dialogue.DialogueEvents.ROCK_COMPLETE_EVENT;

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

        event.createDatapackRegistryObjects(
                new RegistrySetBuilder().add(DataPackRegistryHandler.REGION_REGISTRY_KEY, bootstrap -> {
                    bootstrap.register(UnshatteredRegions.PLAINS_REGION, new Region(0xFF71AD1C, RegionTemperatureTypes.COMFORTABLE, false));
                    bootstrap.register(UnshatteredRegions.DEFAULT_REGION, new Region(0xFFFFFFFF, RegionTemperatureTypes.COLD, false));
                }).add(DataPackRegistryHandler.REGION_BOUNDARY_REGISTRY_KEY, bootstrap -> {
                    HolderGetter<Region> regions = bootstrap.lookup(DataPackRegistryHandler.REGION_REGISTRY_KEY);

                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(-2048, -2048), new Vector2i(2048, 2048), 0), UnshatteredRegions.DEFAULT_REGION, regions);
                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(0, 0), new Vector2i(400, 400), 1), UnshatteredRegions.PLAINS_REGION, regions);
                }).add(DataPackRegistryHandler.DIALOGUE_TREE_REGISTRY_KEY, bootstrap -> {
                    registerDialogueTree(bootstrap,
                            TestBlock.ROCK_DIALOGUE_TREE,
                            new DialogueTree(List.of(
                                    createDialogueOriginWithSelfFlag(
                                    0,
                                    new DialogueNode(Component.literal("hi, im a rock"),
                                            List.of(new DialogueChoice(Component.literal("interesting"),
                                                    new DialogueNode(Component.literal("im glad you think so :)"))),
                                                    new DialogueChoice(Component.literal("..."))
                                            )
                                    ),
                                    TestBlock.HI_MESSAGE_IDENTIFIER),
                                    createDialogueOriginWithSelfFlag(1,
                                            new DialogueNode(Component.literal("you've already spoken to me"),
                                                    List.of(new DialogueChoice(Component.literal("i know right"), ROCK_COMPLETE_EVENT))
                                            ),
                                            List.of(TestBlock.HI_MESSAGE_IDENTIFIER),
                                            List.of(),
                                            List.of(),
                                            TestBlock.HI2_MESSAGE_IDENTIFIER
                                    )
                            ))
                    );
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
}
