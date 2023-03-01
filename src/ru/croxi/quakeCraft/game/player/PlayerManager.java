package ru.croxi.quakeCraft.game.player;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import ru.croxi.quakeCraft.QuakeCraft;
import ru.croxi.quakeCraft.game.GameState;

import java.util.ArrayList;
import java.util.List;

public class PlayerManager {

    private List<APlayer> players;

    public PlayerManager() {
        players = new ArrayList<>();

        if(QuakeCraft.getInstance().getGame().getGameState() != GameState.EDITING) registerAll();
    }

    public void register(Player bukkitPlayer) {
        if(QuakeCraft.getInstance().getGame().getGameState() != GameState.EDITING) {
            PlayerInventory inventory = bukkitPlayer.getInventory();

            inventory.clear();
            inventory.setArmorContents(null);

            bukkitPlayer.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 1, false, false));

            bukkitPlayer.setGameMode(GameMode.ADVENTURE);

            bukkitPlayer.setHealth(20);
            bukkitPlayer.setFoodLevel(20);

            bukkitPlayer.setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());

            players.add(new APlayer(bukkitPlayer));
        }
    }

    public APlayer getPlayer(Player bukkitPlayer) {
        for(APlayer player: players) {
            if(player.getBukkitPlayer().getUniqueId().equals(bukkitPlayer.getUniqueId())) return player;
        }

        return null;
    }

    public void unregister(Player bukkitPlayer) {
        for(APlayer player: players) {
            if(player.getBukkitPlayer().getUniqueId().equals(bukkitPlayer.getUniqueId())) {
                players.remove(player);
                return;
            }
        }
    }

    public void registerAll() {
        for (Player player : Bukkit.getOnlinePlayers()) register(player);
    }

    public void unregisterAll() {
        players.clear();
    }

    public List<APlayer> getPlayers() {
        return players;
    }

}
