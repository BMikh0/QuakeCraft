package ru.croxi.quakeCraft.game.countdowns;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import ru.croxi.quakeCraft.QuakeCraft;
import ru.croxi.quakeCraft.game.Game;
import ru.croxi.quakeCraft.game.GameState;
import ru.croxi.quakeCraft.utils.StringModifier;

public class GameCountdown extends BukkitRunnable {

    private static QuakeCraft quakeCraft;
    private int currentTime;

    public GameCountdown() {
        quakeCraft = QuakeCraft.getInstance();

        currentTime = quakeCraft.getConfig().getInt("gameTime");
    }

    @Override
    public void run() {
        Game game = quakeCraft.getGame();

        if(game.getGameState() == GameState.GAME) {
            currentTime--;

            if(currentTime <= 0) game.stop();
            else if(currentTime < quakeCraft.getConfig().getInt("gameTime2")) Bukkit.broadcastMessage(StringModifier.getModifiedString(quakeCraft.getConfig().getString("messages.gameTime"), currentTime));
        }
    }
}
