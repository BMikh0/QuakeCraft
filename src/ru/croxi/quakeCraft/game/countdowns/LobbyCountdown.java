package ru.croxi.quakeCraft.game.countdowns;

import net.minecraft.server.v1_8_R1.ChatSerializer;
import net.minecraft.server.v1_8_R1.EnumTitleAction;
import net.minecraft.server.v1_8_R1.PacketPlayOutTitle;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.croxi.quakeCraft.QuakeCraft;
import ru.croxi.quakeCraft.game.GameState;
import ru.croxi.quakeCraft.utils.PacketSender;
import ru.croxi.quakeCraft.utils.StringModifier;

import java.util.Collection;

public class LobbyCountdown extends BukkitRunnable {

    private static QuakeCraft quakeCraft;
    private static FileConfiguration config;
    private int currentTime;

    public LobbyCountdown() {
        quakeCraft = QuakeCraft.getInstance();
        config = quakeCraft.getConfig();
        currentTime = config.getInt("lobbyTime1");
    }

    @Override
    public void run() {
        int time1 = config.getInt("lobbyTime1");
        int time2 = config.getInt("lobbyTime2");
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();

        if(quakeCraft.getGame().getGameState() == GameState.WAITING) {

            for(Player player: Bukkit.getOnlinePlayers()) {
                if(currentTime <= time2) {
                    PacketSender.sendPacket(new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\" :\"" +StringModifier.getModifiedString(config.getString("messages.lobbyTimeOnScreen"), currentTime) +"\"}")), player);
                    player.setExp(((float) currentTime)/time2);
                } else player.setExp(1);

                player.setLevel(currentTime);
            }

            if(players.size() >= config.getInt("minPlayers")) {
                String msg = StringModifier.getModifiedString(config.getString("messages.lobbyTime"), currentTime);

                if(players.size() >= config.getInt("recommendedPlayers") && currentTime > time2) currentTime = time2;

                if((currentTime == time1 || currentTime == time1/2 || currentTime <= time2) && currentTime > 0) {
                    for(Player player: players) {
                        player.sendMessage(StringModifier.getModifiedString(msg));
                        player.playSound(player.getLocation(), Sound.NOTE_PLING, 1, currentTime / (time2 + 1f) + 0.5f);
                    }
                }

                currentTime--;
            }

            if(currentTime < 0) startGame();
        } else currentTime = time1;
    }

    public void startGame() {
        PacketSender.sendPacket(new PacketPlayOutTitle(EnumTitleAction.TITLE, ChatSerializer.a("{\"text\" :\"" +StringModifier.getModifiedString(config.getString("messages.gameStartedOnScreen")) +"\"}")));
        Bukkit.broadcastMessage(StringModifier.getModifiedString(StringModifier.getModifiedString(config.getString("messages.gameStarted"))));

        for(Player player: Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), Sound.NOTE_PLING, 0.7f, 0.5f);
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 0.7f, 1.4f);
            player.setLevel(100);
            player.setExp(1);
        }

        quakeCraft.getGame().start();
    }

    public void setTime(int time) {this.currentTime = time;}

}
