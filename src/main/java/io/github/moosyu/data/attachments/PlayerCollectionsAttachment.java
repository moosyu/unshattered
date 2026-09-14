package io.github.moosyu.data.attachments;

import com.mojang.serialization.Codec;
import io.github.moosyu.Unshattered;
import io.github.moosyu.collectables.CollectableItemEntry;
import io.github.moosyu.collectables.CollectableLevel;
import io.github.moosyu.collectables.rewards.CollectableReward;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.datagen.UnshatteredDataMapProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record PlayerCollectionsAttachment(Map<Holder<Item>, Integer> collectedItems) {
    public PlayerCollectionsAttachment() {
        this(new HashMap<>());
    }

    public PlayerCollectionsAttachment(Map<Holder<Item>, Integer> collectedItems) {
        this.collectedItems = new HashMap<>(collectedItems);
    }

    public void addPickedUpItem(ItemStack itemStack, Player player) {
        Holder<Item> itemHolder = itemStack.typeHolder();
        CollectableItemEntry collectableData = itemHolder.getData(UnshatteredDataMaps.COLLECTABLE_DATA);

        if (collectableData == null) return;

        int currentLevel = getLevel(player, itemHolder);

        collectedItems.put(itemHolder, collectedItems.getOrDefault(itemHolder, 0) + itemStack.count());

        int newLevel = getLevel(player, itemHolder);
        if (newLevel > currentLevel) {
            for (int i = 0; i < newLevel - currentLevel; i++) {
                for (CollectableReward collectableReward : collectableData.levels().get(currentLevel + i).rewards()) {
                    collectableReward.reward(player);
                }
            }
        }
    }

    public int getCount(Holder<Item> item) {
        return collectedItems.getOrDefault(item, 0);
    }

    public Map<Holder<Item>, Integer> getMap() {
        return collectedItems;
    }

    /**
     * @param player player having their level checked
     * @param itemHolder collectable item being checked
     * @return the level the player is currently at for the specified collectable item
     */
    public int getLevel(Player player, Holder<Item> itemHolder) {
        PlayerCollectionsAttachment collections = player.getData(UnshatteredAttachments.PLAYER_COLLECTIONS.get());
        CollectableItemEntry itemEntry = itemHolder.getData(UnshatteredDataMaps.COLLECTABLE_DATA);
        int itemCount = collections.getCount(itemHolder);

        if (itemEntry == null) {
            Unshattered.LOGGER.error("missing item entry for {}! returned 0.", itemHolder.getRegisteredName());
            return 0;
        }

        List<CollectableLevel> levels = itemEntry.levels();

        int currentLevel = 0;
        float totalItemsRequiredForNextLevel = 0;
        for (int i = 0; i < levels.size(); i++) {
            totalItemsRequiredForNextLevel += levels.get(i).itemRequirement();

            if (itemCount >= totalItemsRequiredForNextLevel) {
                currentLevel = i + 1;
            } else {
                break;
            }
        }

        return currentLevel;
    }

    public void checkCollectionLevelUp() {

    }

    public static final Codec<PlayerCollectionsAttachment> CODEC = Codec.unboundedMap(BuiltInRegistries.ITEM.holderByNameCodec(), Codec.INT).xmap(PlayerCollectionsAttachment::new, PlayerCollectionsAttachment::collectedItems);

    private static final StreamCodec<RegistryFriendlyByteBuf, Map<Holder<Item>, Integer>> MAP_STREAM_CODEC =
            ByteBufCodecs.map(
                    HashMap::new,
                    ByteBufCodecs.holderRegistry(Registries.ITEM),
                    ByteBufCodecs.INT
            );

    public static final StreamCodec<RegistryFriendlyByteBuf, PlayerCollectionsAttachment> STREAM_CODEC = MAP_STREAM_CODEC.map(PlayerCollectionsAttachment::new, PlayerCollectionsAttachment::getMap);
}
