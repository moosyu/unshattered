package io.github.moosyu.data.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public record GiveItemDialogueEvent(Holder<Item> item, int count) implements DialogueTriggeredEvent {
    @Override
    public void trigger(ServerPlayer player) {
        player.getInventory().add(new ItemStack(item, count));
    }

    @Override
    public MapCodec<? extends DialogueTriggeredEvent> codec() {
        return CODEC;
    }

    public static final MapCodec<GiveItemDialogueEvent> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    Item.CODEC.fieldOf("item").forGetter(GiveItemDialogueEvent::item),
                    Codec.INT.fieldOf("count").forGetter(GiveItemDialogueEvent::count)
            ).apply(instance, GiveItemDialogueEvent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, GiveItemDialogueEvent> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.holderRegistry(Registries.ITEM), GiveItemDialogueEvent::item,
            ByteBufCodecs.INT, GiveItemDialogueEvent::count,
            GiveItemDialogueEvent::new
    );
}
