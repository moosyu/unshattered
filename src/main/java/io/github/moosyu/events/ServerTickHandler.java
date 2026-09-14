package io.github.moosyu.events;

import io.github.moosyu.data.regen.RegenSavedData;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.events.ItemFishedHandler.ACTIVE_LAUNCHES;

@EventBusSubscriber(modid = MODID)
public class ServerTickHandler {
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        RegenSavedData regenSavedData = server.overworld().getDataStorage().computeIfAbsent(RegenSavedData.ID);

        Map<GlobalPos, RegenSavedData.RegenState> regen = regenSavedData.regenQueue;
        for (Map.Entry<GlobalPos, RegenSavedData.RegenState> entry : new ArrayList<>(regen.entrySet())) {
            if (regenSavedData.tickBlock(entry.getKey())) {
                regenSavedData.regenerateBlock(entry.getKey(), server.getLevel(entry.getKey().dimension()));
            }
        }

        if (!ACTIVE_LAUNCHES.isEmpty()) {
            Iterator<Map.Entry<Entity, ItemFishedHandler.LaunchData>> it = ACTIVE_LAUNCHES.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<Entity, ItemFishedHandler.LaunchData> mapEntry = it.next();
                Entity entity = mapEntry.getKey();
                ItemFishedHandler.LaunchData data = mapEntry.getValue();

                if (!entity.isAlive()) {
                    it.remove();
                    continue;
                }

                long elapsed = entity.level().getGameTime() - data.startTick();

                if (elapsed >= data.durationTicks()) {
                    entity.setDeltaMovement(Vec3.ZERO);
                    entity.setPos(data.target().x, data.target().y, data.target().z);

                    if (entity instanceof Mob mob) {
                        mob.setNoAi(false);
                    }

                    it.remove();
                    continue;
                }

                double progress = (double) elapsed / data.durationTicks();
                entity.setPos(Mth.lerp(progress, data.start().x, data.target().x),
                        Mth.lerp(progress,
                                Mth.lerp(progress, data.start().y, data.peakY()),
                                Mth.lerp(progress, data.peakY(), data.target().y)
                        ),
                        Mth.lerp(progress, data.start().z, data.target().z));
                entity.setDeltaMovement(Vec3.ZERO);
            }
        }
    }
}
