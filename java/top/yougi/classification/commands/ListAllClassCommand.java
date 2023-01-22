package top.yougi.classification.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import top.yougi.classification.capability.LevelCapabilityProvider;

import java.util.List;
import java.util.Map;

public class ListAllClassCommand {
    public ListAllClassCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("class")
                .then(Commands.literal("list")
                        .executes((command -> {
                            return ListAllClass(command.getSource());
                        }))
                ));
    }

    private int ListAllClass(CommandSourceStack source) throws CommandSyntaxException {
        ServerLevel level = source.getLevel();
        StringBuilder rep = new StringBuilder();
        rep.append("\n当前世界所有的分类名如下：");
        level.getCapability(LevelCapabilityProvider.LEVEL_CAPABILITY).ifPresent(
            cap -> {
                Map<String, List<String>> map = cap.getClassMap();
                for (Map.Entry<String, List<String>> entry: map.entrySet()) {
                    rep.append("\n").append(entry.getKey());
                }
                source.sendSystemMessage(Component.literal(rep.toString()));
            }
        );

        return 1;
    }
}
