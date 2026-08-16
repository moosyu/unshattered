package io.github.moosyu.data.attachments;

import com.mojang.serialization.Codec;
import io.github.moosyu.collectables.CollectableEntries;
import io.github.moosyu.collectables.CollectableItemEntry;
import io.github.moosyu.collectables.CollectableLevel;
import io.github.moosyu.collectables.rewards.CollectableReward;
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
        CollectableItemEntry collectableItemEntry = CollectableEntries.getCollectableEntry(itemHolder);

        if (collectableItemEntry == null) return;

        int currentLevel = getLevel(player, collectableItemEntry);

        this.collectedItems.put(itemHolder, this.collectedItems.getOrDefault(itemHolder, 0) + itemStack.count());

        int newLevel = getLevel(player, collectableItemEntry);
        if (newLevel > currentLevel) {
            for (int i = 0; i < newLevel - currentLevel; i++) {
                for (CollectableReward collectableReward : collectableItemEntry.levels().get(currentLevel + i).rewards()) {
                    collectableReward.reward(player);
                }
            }
        }
    }

    public int getCount(Holder<Item> item) {
        return this.collectedItems.getOrDefault(item, 0);
    }

    public Map<Holder<Item>, Integer> getMap() {
        return this.collectedItems;
    }

    /**
     * @param player player having their level checked
     * @param entry collectable item being checked
     * @return the level the player is currently at for the specified collectable item
     */
    public int getLevel(Player player, CollectableItemEntry entry) {
        PlayerCollectionsAttachment collections = player.getData(UnshatteredAttachments.PLAYER_COLLECTIONS.get());
        int itemCount = collections.getCount(BuiltInRegistries.ITEM.wrapAsHolder(entry.item()));
        List<CollectableLevel> levels = entry.levels();

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
