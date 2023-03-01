package ru.croxi.quakeCraft.game;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.EnumTitleAction;
import net.minecraft.server.v1_8_R1.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import ru.croxi.quakeCraft.QuakeCraft;
import ru.croxi.quakeCraft.game.countdowns.GameCountdown;
import ru.croxi.quakeCraft.game.countdowns.LobbyCountdown;
import ru.croxi.quakeCraft.game.player.APlayer;
import ru.croxi.quakeCraft.scoreboards.Stats;
import ru.croxi.quakeCraft.utils.PacketSender;
import ru.croxi.quakeCraft.utils.StringModifier;

public class Game {

    private static QuakeCraft quakeCraft;
    private GameState gameState;
    private LobbyCountdown lobbyCountdown;
    private GameCountdown gameCountdown;
    private static Stats stats;

    public Game() {
        quakeCraft = QuakeCraft.getInstance();

        if(quakeCraft.getConfig().getBoolean("editMode") || quakeCraft.getSpawnpoints().areEmpty()) gameState = GameState.EDITING;
        else gameState = GameState.WAITING;

        lobbyCountdown = new LobbyCountdown();
        gameCountdown = new GameCountdown();
        stats = new Stats();

        lobbyCountdown.runTaskTimerAsynchronously(QuakeCraft.getInstance(), 0, 20);
    }

    public void start() {
        gameState = GameState.GAME;
        stats.sendAll();

        quakeCraft.getItemManager().giveItems();

        gameCountdown.runTaskTimer(quakeCraft, 0, 20);
    }

    public void stop() {
        gameState = GameState.STOPPED;
        gameCountdown.cancel();
        stats.sendAll();

        quakeCraft.getItemManager().giveItems();

        APlayer[] players = stats.getLeaders();
        ConfigurationSection section = quakeCraft.getConfig().getConfigurationSection("messages.gameOver");

        for(String string: section.getStringList("message")) {
            for(int i = 0; i < 3 && i < players.length; i++)
                string = StringModifier.getModifiedString(string, players[i].getBukkitPlayer())
                        .replace(String.valueOf("%" + (i + 1) + "%"), players[i].getBukkitPlayer().getName())
                        .replace("%kills" + (i + 1) + "%", String.valueOf(players[i].getKills()));

            Bukkit.broadcastMessage(string);
        }

        PacketSender.sendPacket(new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\" :\"" + StringModifier.getModifiedString(section.getString("messageOnScreen"), players[0].getBukkitPlayer().getPlayer()) + "\"}")));

        quakeCraft.getPlayerManager().unregisterAll();
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public LobbyCountdown getLobbyCountdown() {return lobbyCountdown;}
    Stats getStats() {
        return stats;
    }

}
