package io.github.moosyu.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.registers.AttachmentRegistry;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public class LevelCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("levels").then(Commands.argument("skill", StringArgumentType.word())
                .suggests((context, builder) -> {
                    builder.suggest("mining");
                    builder.suggest("foraging");
                    builder.suggest("fishing");
                    builder.suggest("combat");
                    builder.suggest("farming");
                    return builder.buildFuture();
                }).executes(context -> {
                    ServerPlayer player = context.getSource().getPlayer();
                    if (player == null) {
                        return 0;
                    }
                    String skillName = StringArgumentType.getString(context, "skill");
                    PlayerSkillsAttachment skills = player.getData(AttachmentRegistry.PLAYER_SKILLS.get());

                    int level = switch (skillName.toLowerCase()) {
                        case "mining" -> skills.getLevel(skills.getMiningExp());
                        case "foraging" -> skills.getLevel(skills.getForagingExp());
                        case "fishing" -> skills.getLevel(skills.getFishingExp());
                        case "combat" -> skills.getLevel(skills.getCombatExp());
                        case "farming" -> skills.getLevel(skills.getFarmingExp());
                        default -> -1;
                    };

                    if (level == -1) {
                        context.getSource().sendFailure(net.minecraft.network.chat.Component.literal("Unknown skill!"));
                        return 0;
                    }

                    context.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal(skillName.substring(0, 1).toUpperCase() + skillName.substring(1) + " level: " + level), false);
                    return 1;
                })));
    }
}