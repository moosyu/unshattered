package io.github.moosyu.events;

import io.github.moosyu.commands.LevelCommand;
import io.github.moosyu.commands.SkillCommand;
import io.github.moosyu.commands.StatCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static io.github.moosyu.Unshattered.MODID;

// Perhaps DeferredRegister should be involved here, I'll look into it though. <-- I looked into it and there doesn't seem like command registries, sorry registry fans.
public class CommandHandler {
    @EventBusSubscriber(modid = MODID)
    public static class EventHandler {
        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            SkillCommand.register(event.getDispatcher());
            LevelCommand.register(event.getDispatcher());
            StatCommand.register(event.getDispatcher());
        }
    }
}
