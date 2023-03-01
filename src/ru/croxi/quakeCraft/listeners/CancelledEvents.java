package ru.croxi.quakeCraft.listeners;

import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import ru.croxi.quakeCraft.QuakeCraft;
import ru.croxi.quakeCraft.game.GameState;

public class CancelledEvents implements Listener {

    private GameState gameState;
    private FileConfiguration config;

    public CancelledEvents() {
        QuakeCraft quakeCraft = QuakeCraft.getInstance();

        gameState = quakeCraft.getGame().getGameState();
        config = quakeCraft.getConfig();
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void cancel(PlayerPickupItemEvent event) {
        if(!config.getBoolean("canPlayersPickUpItems") && event.getPlayer().getGameMode() == GameMode.ADVENTURE && gameState != GameState.EDITING) event.setCancelled(true);
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void cancel(PlayerDropItemEvent event) {
        if(!config.getBoolean("canPlayersDropItems") && event.getPlayer().getGameMode() == GameMode.ADVENTURE && gameState != GameState.EDITING) event.setCancelled(true);
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void cancel(EntityDamageEvent event) {
        if(gameState != GameState.EDITING) event.setCancelled(true);
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void cancel(FoodLevelChangeEvent event) {
        if(gameState != GameState.EDITING) event.setCancelled(true);
    }

}
