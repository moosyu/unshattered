package io.github.moosyu.data.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public record GiveItemDialogueEvent(Holder<Item> item, int count) implements DialogueTriggeredEvent {
    @Override
    public void trigger(ServerPlayer player) {
        player.getInventory().add(new ItemStack(item, count));
    }

    public static final Codec<GiveItemDialogueEvent> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Item.CODEC.fieldOf("item").forGetter(GiveItemDialogueEvent::item),
                    Codec.INT.fieldOf("count").forGetter(GiveItemDialogueEvent::count)
            ).apply(instance, GiveItemDialogueEvent::new)
    );
}
