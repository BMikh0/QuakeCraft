package ru.croxi.quakeCraft.listeners;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.EnumTitleAction;
import net.minecraft.server.v1_8_R1.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import ru.croxi.quakeCraft.QuakeCraft;
import ru.croxi.quakeCraft.game.GameState;
import ru.croxi.quakeCraft.game.player.PlayerManager;
import ru.croxi.quakeCraft.utils.PacketSender;
import ru.croxi.quakeCraft.utils.StringModifier;

public class JoinQuit implements Listener {

    private static QuakeCraft quakeCraft;
    private static FileConfiguration config;
    private static PlayerManager playerManager;

    public JoinQuit() {
        quakeCraft = QuakeCraft.getInstance();
        config = quakeCraft.getConfig();
        playerManager = quakeCraft.getPlayerManager();
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onJoin(PlayerJoinEvent event) {
        GameState gameState = quakeCraft.getGame().getGameState();
        Player player = event.getPlayer();

        if(Bukkit.getOnlinePlayers().size() >= config.getInt("maxPlayers")+1) {
            player.kickPlayer(StringModifier.getModifiedString(config.getString("messages.tooManyPlayers")));
            return;
        }

        if(gameState == GameState.GAME || gameState == GameState.STOPPED) {
            player.kickPlayer(StringModifier.getModifiedString(quakeCraft.getConfig().getString("messages.gameIsOnKickMessage")));
            return;
        } else if(gameState == GameState.WAITING) Bukkit.broadcastMessage(StringModifier.getModifiedString(config.getString("messages.playerJoin").replace("%count%", String.valueOf(Bukkit.getOnlinePlayers().size())), event.getPlayer()));

        quakeCraft.editModeMessage(player);
        playerManager.register(player);
    }


    @EventHandler
    @SuppressWarnings("unused")
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        GameState gameState = quakeCraft.getGame().getGameState();

        if(gameState == GameState.WAITING) {
            playerManager.unregister(player);

            Bukkit.broadcastMessage(StringModifier.getModifiedString(config.getString("messages.playerQuit").replace("%count%", String.valueOf(Bukkit.getOnlinePlayers().size()-1)), event.getPlayer()));

            if(Bukkit.getOnlinePlayers().size() == config.getInt("recommendedPlayers") || Bukkit.getOnlinePlayers().size() == config.getInt("minPlayers")) {
                Bukkit.broadcastMessage(StringModifier.getModifiedString(config.getString("messages.notEnoughPlayers")));
                PacketSender.sendPacket(new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\" :\"" + StringModifier.getModifiedString(config.getString("messages.gameCancellation")) + "\"}")));

                for(Player player2: Bukkit.getOnlinePlayers()) {
                    player2.playSound(player2.getLocation(), Sound.NOTE_BASS, 2, 1);
                    player2.playSound(player2.getLocation(), Sound.WOOD_CLICK, 2, 1);
                }

                quakeCraft.getGame().getLobbyCountdown().setTime(config.getInt("lobbyTime1"));
            }
        } else if(gameState == GameState.STOPPED) playerManager.unregister(player);
        else if(gameState == GameState.GAME && Bukkit.getOnlinePlayers().size() == 2) quakeCraft.getGame().stop();

    }

}
