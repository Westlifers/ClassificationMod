package top.yougi.classification.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import top.yougi.classification.capability.LevelCapabilityProvider;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ShowDetailOfClassCommand {
    public ShowDetailOfClassCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("class")
                .then(Commands.literal("show")
                        .then(Commands
                                .argument("ClassName", StringArgumentType.word())
                                .suggests((ctx, builder) -> {
                                    ServerLevel level = ctx.getSource().getLevel();
                                    level.getCapability(LevelCapabilityProvider.LEVEL_CAPABILITY).ifPresent(cap -> {
                                        for (Map.Entry<String, List<String>> entry: cap.getClassMap().entrySet()) {
                                            builder.suggest(entry.getKey());
                                        }
                                    });
                                    return builder.buildFuture();
                                })
                                .executes((command -> {
                                    ServerLevel level = command.getSource().getLevel();
                                    String className = StringArgumentType.getString(command, "ClassName");
                                    level.getCapability(LevelCapabilityProvider.LEVEL_CAPABILITY).ifPresent(cap -> {
                                        StringBuilder rep = new StringBuilder();
                                        rep.append("\n").append(className).append("包含的具体物品有：");
                                        Map<String, List<String>> map = cap.getClassMap();
                                        for (Map.Entry<String, List<String>> entry: map.entrySet()) {
                                            if (Objects.equals(entry.getKey(), className)) {
                                                for (String item: entry.getValue()) {
                                                    rep.append("\n").append(item);
                                                }
                                            }
                                        }
                                        command.getSource().sendSystemMessage(Component.literal(rep.toString()));
                                    });
                                    return 1;
                                }))
                        )

                )
        );
    }

}
