package io.github.moosyu.events;

import io.github.moosyu.data.dialogue.DialogueChoice;
import io.github.moosyu.data.dialogue.DialogueNode;
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

import java.util.List;
import java.util.Set;
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

        event.createDatapackRegistryObjects(
                new RegistrySetBuilder().add(DataPackRegistryHandler.REGION_REGISTRY_KEY, bootstrap -> {
                    bootstrap.register(UnshatteredRegions.PLAINS_REGION, new Region(0xFF71AD1C, RegionTemperatureTypes.COMFORTABLE, false));
                    bootstrap.register(UnshatteredRegions.DEFAULT_REGION, new Region(0xFFFFFFFF, RegionTemperatureTypes.COLD, false));
                }).add(DataPackRegistryHandler.REGION_BOUNDARY_REGISTRY_KEY, bootstrap -> {
                    HolderGetter<Region> regions = bootstrap.lookup(DataPackRegistryHandler.REGION_REGISTRY_KEY);

                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(-2048, -2048), new Vector2i(2048, 2048), 0), UnshatteredRegions.DEFAULT_REGION, regions);
                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(0, 0), new Vector2i(400, 400), 1), UnshatteredRegions.PLAINS_REGION, regions);
                }).add(DataPackRegistryHandler.DIALOGUE_NODE_REGISTRY_KEY, bootstrap -> {
                    registerDialogueNode(bootstrap, "rock", "hi", Component.literal("hi, im a rock!"), List.of());
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

    public static void registerDialogueNode(BootstrapContext<DialogueNode> bootstrap, String dialogueInitiatorName, String dialogueTreeName, String dialogueID, Component dialogueText, List<DialogueChoice> dialogueChoices) {
        bootstrap.register(
                ResourceKey.create(DataPackRegistryHandler.DIALOGUE_NODE_REGISTRY_KEY, Identifier.fromNamespaceAndPath(MODID, dialogueInitiatorName + "/" + dialogueTreeName + dialogueID)),
                new DialogueNode(dialogueText, dialogueChoices)
        );
    }

    public static void registerDialogueNode(BootstrapContext<DialogueNode> bootstrap, String dialogueInitiatorName, String dialogueID, Component dialogueText, List<DialogueChoice> dialogueChoices) {
        bootstrap.register(
                ResourceKey.create(DataPackRegistryHandler.DIALOGUE_NODE_REGISTRY_KEY, Identifier.fromNamespaceAndPath(MODID, dialogueInitiatorName + "/" + dialogueID)),
                new DialogueNode(dialogueText, dialogueChoices)
        );
    }

}
