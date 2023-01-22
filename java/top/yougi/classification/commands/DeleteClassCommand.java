package top.yougi.classification.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import top.yougi.classification.capability.LevelCapabilityProvider;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DeleteClassCommand {
    public DeleteClassCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("class")
                .then(Commands.literal("delete")
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
                                        Map<String, List<String>> map = cap.getClassMap();
                                        if (map.remove(className) != null) {
                                            cap.setClassMap(map);
                                            rep.append("\n删除分类").append(className).append("成功！");
                                            command.getSource().sendSystemMessage(Component.literal(rep.toString()));
                                        } else {
                                            rep.append("\n分类").append(className).append("不存在！请检查输入");
                                            command.getSource().sendSystemMessage(Component.literal(rep.toString()));
                                        }
                                    });
                                    return 1;
                                }))
                        )

                )
        );
    }
}
