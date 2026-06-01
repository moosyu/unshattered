package io.github.moosyu.events;

import io.github.moosyu.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.ModAttributes;
import io.github.moosyu.registers.AttributesRegistry;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.registers.AttachmentRegistry.PLAYER_STATE;

// triggers when the player starts the game or switches world
// however when the game starts this gives you the wrong value. idk why, maybe attributes arent properly loaded yet so you dont get modifiers.
// todo: fix whatever causes that
@EventBusSubscriber(modid = MODID)
public class PlayerJoinHandler {
    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            var stats = player.getData(PLAYER_STATE.get());
            final AttributeInstance healthAttribute = player.getAttribute(ModAttributes.HEALTH.holder);
            if (healthAttribute == null) return;
            final double MAX_HEALTH = healthAttribute.getValue();
            stats.setCurrentStat(PlayerStateAttachment.Stat.HEALTH, MAX_HEALTH);
            player.syncData(PLAYER_STATE);
        }
    }
}
