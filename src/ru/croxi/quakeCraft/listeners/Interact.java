package ru.croxi.quakeCraft.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import ru.croxi.quakeCraft.QuakeCraft;

public class Interact implements Listener {

    private static QuakeCraft quakeCraft;

    public Interact() {
        quakeCraft = QuakeCraft.getInstance();
    }

    @EventHandler
    @SuppressWarnings("unused")
    public void onClick(PlayerInteractEvent event) {
        if(event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();

            if(player.getItemInHand().equals(quakeCraft.getItemManager().getItem("gun"))) {
                event.setCancelled(true);
                quakeCraft.getPlayerManager().getPlayer(player).useGun();
            }
        }
    }

}
