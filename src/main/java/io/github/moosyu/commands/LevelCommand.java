package io.github.moosyu.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import io.github.moosyu.attachments.PlayerSkillsAttachment;
import io.github.moosyu.registers.AttachmentRegistry;
import net.minecraft.commands.Commands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
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
                        case "mining" -> skills.getLevel(skills.getExp(PlayerSkillsAttachment.Skill.MINING));
                        case "foraging" -> skills.getLevel(skills.getExp(PlayerSkillsAttachment.Skill.FORAGING));
                        case "fishing" -> skills.getLevel(skills.getExp(PlayerSkillsAttachment.Skill.FISHING));
                        case "combat" -> skills.getLevel(skills.getExp(PlayerSkillsAttachment.Skill.COMBAT));
                        case "farming" -> skills.getLevel(skills.getExp(PlayerSkillsAttachment.Skill.FARMING));
                        default -> -1;
                    };

                    if (level == -1) {
                        context.getSource().sendFailure(Component.translatable("commands.unshattered.skills.levels.get.failure"));
                        return 0;
                    }

                    context.getSource().sendSuccess(() -> Component.literal(skillName.substring(0, 1).toUpperCase() + skillName.substring(1) + " " + Component.translatable("commands.unshattered.skills.skill") + ":"  + level), false);
                    return 1;
                })));
    }
}