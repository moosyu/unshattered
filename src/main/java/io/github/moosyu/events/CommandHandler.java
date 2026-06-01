package io.github.moosyu.events;

import io.github.moosyu.commands.LevelCommand;
import io.github.moosyu.commands.SkillCommand;
import io.github.moosyu.commands.StatCommand;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class CommandHandler {
    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        SkillCommand.register(event.getDispatcher());
        LevelCommand.register(event.getDispatcher());
        StatCommand.register(event.getDispatcher());
    }
}