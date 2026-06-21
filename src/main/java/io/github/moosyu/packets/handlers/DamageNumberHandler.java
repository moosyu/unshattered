package io.github.moosyu.packets.handlers;

import io.github.moosyu.packets.DamageNumberPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;

// who said maths isnt useful
public class DamageNumberHandler {
    public static void handleData(final DamageNumberPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            System.out.println("damage dealt: " + data.number());
            Player player = Minecraft.getInstance().player;
            if (player == null) return;

            final double TARGET_Y = data.getY() + 1.2d;
            final double OFFSET_DISTANCE = 0.8;
            double dirX = player.getX() - data.getX();
            double dirY = player.getEyeY() - TARGET_Y;
            double dirZ = player.getZ() - data.getZ();
            final double DISTANCE = Math.sqrt(dirX * dirX + dirY * dirY + dirZ * dirZ);

            if (DISTANCE > 0.001) {
                dirX /= DISTANCE;
                dirY /= DISTANCE;
                dirZ /= DISTANCE;
            }

            // decided against doing this with particles as it seemed like i was getting diminishing returns, instead going to try something with RenderLevelStageEvent
            //Minecraft.getInstance().particleEngine.add(new DamageNumberParticle((ClientLevel) player.level(), data.getX() + (dirX * OFFSET_DISTANCE), TARGET_Y + (dirY * OFFSET_DISTANCE), data.getZ() + (dirZ * OFFSET_DISTANCE), Component.literal("hi")));
        });
    }
}
