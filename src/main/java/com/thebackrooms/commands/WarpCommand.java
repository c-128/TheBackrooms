package com.thebackrooms.commands;

import com.thebackrooms.levels.Level;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.Argument;
import net.minestom.server.command.builder.arguments.ArgumentType;
import net.minestom.server.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WarpCommand extends Command {

    public WarpCommand() {
        super("warp");

        setCondition((sender, commandString) -> sender.isPlayer());

        // TODO: Make code look better (level_0)
        var levelArgument = ArgumentType.Word("level").from("level_0");

        addSyntax((sender, context) -> {
            Player player = sender.asPlayer();
            String id = context.get(levelArgument);
            if (!player.getTag(Level.TAG_VISITED_LEVELS).contains(id.hashCode())) {
                player.sendMessage("You must visit the level first.");
                return;
            }
            Level level = Level.LEVELS.get(id);
            if (player.hasTag(Level.TAG_LEVEL)) {
                Level visitedLevel = Level.LEVELS.get(player.getTag(Level.TAG_LEVEL));
                visitedLevel.removePlayer(player);
            }

            level.addPlayer(player);
            player.sendMessage("You warped to level " + id);
        }, levelArgument);
    }
}
