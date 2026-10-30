package io.github.moosyu.data.datagen;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import org.jspecify.annotations.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import static io.github.moosyu.items.UnshatteredArmourMaterials.*;

// thanks for the code pookie
// https://github.com/Tutorials-By-Kaupenjoe/NeoForge-Course-26.X/blob/22-armor/src/main/java/net/kaupenjoe/mccourse/datagen/ModEquipmentAsset.java
public class UnshatteredEquipmentAssetProvider implements DataProvider {
    private final PackOutput.PathProvider pathProvider;

    public UnshatteredEquipmentAssetProvider(PackOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
    }

    private static void bootstrap(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
        output.accept(LEAFLET_KEY, EquipmentClientInfo.builder().addHumanoidLayers(UnshatteredUtils.getUnshatteredIdentifier("leaflet")).build());
        output.accept(GLOW_SQUID_BOOTS_KEY, EquipmentClientInfo.builder().addHumanoidLayers(UnshatteredUtils.getUnshatteredIdentifier("glow_squid_boots")).build());
        output.accept(SKELETON_HAT_KEY, EquipmentClientInfo.builder().addHumanoidLayers(UnshatteredUtils.getUnshatteredIdentifier("skeleton_hat")).build());
        output.accept(LAPIS_ARMOUR_KEY, EquipmentClientInfo.builder().addHumanoidLayers(UnshatteredUtils.getUnshatteredIdentifier("lapis")).build());
        output.accept(MINER_ARMOUR_KEY, EquipmentClientInfo.builder().addHumanoidLayers(UnshatteredUtils.getUnshatteredIdentifier("miner")).build());
    }

    @Override
    public @NonNull CompletableFuture<?> run(@NonNull CachedOutput cache) {
        Map<ResourceKey<EquipmentAsset>, EquipmentClientInfo> equipmentAssets = new HashMap<>();
        bootstrap((id, asset) -> {
            if (equipmentAssets.putIfAbsent(id, asset) != null) {
                throw new IllegalStateException("Tried to register equipment asset twice for id: " + id);
            }
        });
        return DataProvider.saveAll(cache, EquipmentClientInfo.CODEC, this.pathProvider::json, equipmentAssets);
    }

    @Override
    public @NonNull String getName() {
        return "Unshattered Equipment Definitions";
    }
}
