package io.github.moosyu.data.dialogue;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static io.github.moosyu.Unshattered.MODID;

public final class DialogueEventTypes {
    private static final Map<String, MapCodec<? extends DialogueTriggeredEvent>> DIALOGUE_TRIGGERED_EVENT_CODECS = new HashMap<>();
    private static final Map<String, StreamCodec<? super RegistryFriendlyByteBuf, ? extends DialogueTriggeredEvent>> DIALOGUE_TRIGGERED_EVENT_STREAM_CODECS = new HashMap<>();

    static {
        register("give_item", GiveItemDialogueEvent.CODEC, GiveItemDialogueEvent.STREAM_CODEC);
        register("start_quest", StartQuestDialogueEvent.CODEC, StartQuestDialogueEvent.STREAM_CODEC);
    }

    private static <T extends DialogueTriggeredEvent> void register(String id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec) {
        String fullId = MODID + ":" + id;
        DIALOGUE_TRIGGERED_EVENT_CODECS.put(fullId, codec);
        DIALOGUE_TRIGGERED_EVENT_STREAM_CODECS.put(fullId, streamCodec);
    }

    private static String idFor(MapCodec<?> codec) {
        return DIALOGUE_TRIGGERED_EVENT_CODECS.entrySet().stream()
                .filter(entry -> entry.getValue() == codec)
                .findFirst()
                .map(Map.Entry::getKey)
                .orElseThrow(() -> new IllegalStateException("unregistered dialogue event codec"));
    }

    private static final Codec<MapCodec<? extends DialogueTriggeredEvent>> TYPE_CODEC = Codec.STRING.comapFlatMap(
            id -> Optional.ofNullable(DIALOGUE_TRIGGERED_EVENT_CODECS.get(id))
                    .map(DataResult::success)
                    .orElseGet(() -> DataResult.error(() -> "unknown dialogue event type: " + id)),
            DialogueEventTypes::idFor
    );

    public static final Codec<DialogueTriggeredEvent> CODEC = TYPE_CODEC.dispatch("type", DialogueTriggeredEvent::codec, Function.identity());

    public static final StreamCodec<RegistryFriendlyByteBuf, DialogueTriggeredEvent> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.<RegistryFriendlyByteBuf>cast().dispatch(
            event -> idFor(event.codec()),
            DIALOGUE_TRIGGERED_EVENT_STREAM_CODECS::get
    );
}