package io.github.moosyu.events;

import io.github.moosyu.attachments.AttachmentRegistry;
import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributes;
import io.github.moosyu.data.components.DataComponentRegistry;
import io.github.moosyu.data.components.SkillRequirement;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.level.ExplosionEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class ExplosionHandler {
    @SubscribeEvent
    public static void onExplosion(ExplosionEvent.Detonate event) {
        event.getAffectedBlocks().clear();
    }
}