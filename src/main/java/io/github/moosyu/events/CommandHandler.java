package io.github.moosyu.events;

import io.github.moosyu.commands.SkillCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

public class CommandHandler {
    @EventBusSubscriber(modid = "nno")
    public static class EventHandler {
        @SubscribeEvent
        public static void onRegisterCommands(RegisterCommandsEvent event) {
            SkillCommand.register(event.getDispatcher());
        }
    }
}
