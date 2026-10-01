package io.github.moosyu.data.components;

import com.mojang.serialization.Codec;
import io.github.moosyu.items.ItemType;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import java.util.*;
import java.util.function.IntFunction;

public record ItemAttachments(Map<SlotType, ItemStack> attachments) {
    public enum SlotType implements StringRepresentable {
        HOOK(ItemType.HOOK),
        LINE(ItemType.LINE),
        SINKER(ItemType.SINKER),
        FUEL_TANK(ItemType.FUEL_TANK),
        DRILL_ENGINE(ItemType.DRILL_ENGINE),
        UPGRADE_MODULE(ItemType.UPGRADE_MODULE);

        public final ItemType correspondingType;

        SlotType(ItemType correspondingType) {
            this.correspondingType = correspondingType;
        }

        @Override
        public @NonNull String getSerializedName() {
            return name().toLowerCase(Locale.ROOT);
        }

        // idk what'll happen if something in inserted not at the end because im using ordinals so i can use 1 byte
        // instead of 4 bytes because im a bit of an efficiency goat
        private static final IntFunction<SlotType> BY_ID = ByIdMap.continuous(
                Enum::ordinal,
                values(),
                ByIdMap.OutOfBoundsStrategy.ZERO
        );

        public static Codec<SlotType> CODEC = StringRepresentable.fromEnum(SlotType::values);
        public static final StreamCodec<ByteBuf, SlotType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
    }

    public ItemAttachments {
        EnumMap<SlotType, ItemStack> copy = new EnumMap<>(SlotType.class);
        attachments.forEach((slot, stack) -> copy.put(slot, stack.copy()));
        attachments = Collections.unmodifiableMap(copy);
    }

    /**
     * for initialising the component with slots (genius?)
     */
    public static ItemAttachments withSlots(SlotType... slots) {
        Map<SlotType, ItemStack> map = new EnumMap<>(SlotType.class);
        for (SlotType slot : slots) map.put(slot, ItemStack.EMPTY);

        return new ItemAttachments(map);
    }

    public boolean hasSlot(SlotType slot) {
        return attachments.containsKey(slot);
    }

    public boolean has(SlotType slot) {
        return !get(slot).isEmpty();
    }

    public ItemStack get(SlotType slot) {
        ItemStack stack = attachments.get(slot);

        return stack == null ? ItemStack.EMPTY : stack.copy();
    }

    public boolean canAdd(SlotType slot, ItemStack itemStack) {
        return hasSlot(slot) && !has(slot) && !itemStack.isEmpty() && (itemStack.get(UnshatteredDataComponents.ITEM_TYPE.get()) == slot.correspondingType);
    }

    public ItemAttachments add(SlotType slot, ItemStack stack) {
        if (!canAdd(slot, stack)) return this;

        Map<SlotType, ItemStack> copy = new EnumMap<>(SlotType.class);
        copy.putAll(attachments);

        return new ItemAttachments(copy);
    }

    public ItemAttachments remove(SlotType slot) {
        if (!has(slot)) return this;

        Map<SlotType, ItemStack> copy = new EnumMap<>(SlotType.class);
        copy.putAll(attachments);

        return new ItemAttachments(copy);
    }

    public static final Codec<ItemAttachments> CODEC = Codec.unboundedMap(SlotType.CODEC, ItemStack.OPTIONAL_CODEC).xmap(ItemAttachments::new, ItemAttachments::attachments);

    public static final StreamCodec<RegistryFriendlyByteBuf, ItemAttachments> STREAM_CODEC =
            ByteBufCodecs.<RegistryFriendlyByteBuf, SlotType, ItemStack, Map<SlotType, ItemStack>>map(
                    _ -> new EnumMap<>(SlotType.class),
                    SlotType.STREAM_CODEC,
                    ItemStack.OPTIONAL_STREAM_CODEC
            ).map(ItemAttachments::new, ItemAttachments::attachments);
}
