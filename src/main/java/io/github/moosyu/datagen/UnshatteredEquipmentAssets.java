package io.github.moosyu.datagen;

import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.Registry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.materials.ArmorMaterials.LEAFLET_KEY;

// thanks for the code pookie
// https://github.com/Tutorials-By-Kaupenjoe/NeoForge-Course-26.X/blob/22-armor/src/main/java/net/kaupenjoe/mccourse/datagen/ModEquipmentAsset.java
public class UnshatteredEquipmentAssets implements DataProvider {
    private final PackOutput.PathProvider pathProvider;

    public UnshatteredEquipmentAssets(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
    }

    private static void bootstrap(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
        output.accept(LEAFLET_KEY, EquipmentClientInfo.builder().addHumanoidLayers(Identifier.fromNamespaceAndPath(MODID, "leaflet"), false).build());
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> equipmentAssets = new HashMap<>();
        bootstrap((id, asset) -> {
            if (equipmentAssets.putIfAbsent(id, asset) != null) {
                throw new IllegalStateException("Tried to register equipment asset twice for id: " + id);
            }
        });
        return DataProvider.saveAll(cache, EquipmentClientInfo.CODEC, this.pathProvider::json, equipmentAssets);
    }

    @Override
    public String getName() {
        return "Unshattered Equipment Definitions";
    }
}
