package ru.croxi.quakeCraft.command.subCommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import ru.croxi.quakeCraft.QuakeCraft;
import ru.croxi.quakeCraft.command.SubCommand;
import ru.croxi.quakeCraft.game.Game;
import ru.croxi.quakeCraft.game.GameState;
import ru.croxi.quakeCraft.utils.StringModifier;

public class Start extends SubCommand {

    public Start(QuakeCraft quakeCraft) {
        super(quakeCraft,"start", StringModifier.getModifiedString(quakeCraft.getConfig().getString("commandDescription.start")), "quakecraft.start", "start");
    }

    @Override
    protected void onExecute(CommandSender sender, String... args) {
        Game game = getQuakeCraft().getGame();

        if(game.getGameState() == GameState.EDITING) {
            if(getQuakeCraft().getSpawnpoints().getLocations().size() == 0) {
                sender.sendMessage(StringModifier.getModifiedString(getConfig().getString("messages.locationsAreEmptyCommandError")));
                return;
            }

            game.setGameState(GameState.WAITING);
            getQuakeCraft().getPlayerManager().registerAll();

            return;
        }

        if(Bukkit.getOnlinePlayers().size() < getConfig().getInt("minPlayers")) {
            sender.sendMessage(StringModifier.getModifiedString(getConfig().getString("messages.notEnoughPlayers")));
            return;
        }

        if(game.getGameState() == GameState.GAME) {
            sender.sendMessage(StringModifier.getModifiedString(getConfig().getString("messages.gameIsOnCommandError")));
            return;
        }

        if(game.getGameState() == GameState.STOPPED) {
            sender.sendMessage(StringModifier.getModifiedString(getConfig().getString("messages.gameOverCommandError")));
            return;
        }

        game.getLobbyCountdown().startGame();
    }
}
