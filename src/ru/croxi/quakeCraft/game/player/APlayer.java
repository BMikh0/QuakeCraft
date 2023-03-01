package ru.croxi.quakeCraft.game.player;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import ru.croxi.quakeCraft.QuakeCraft;
import ru.croxi.quakeCraft.game.Gun;
import ru.croxi.quakeCraft.utils.StringModifier;

public class APlayer {

    private Player bukkitPlayer;
    private Gun gun;
    private int kills;
    private int deaths;
    private boolean isProtected;

    APlayer(Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
        gun = new Gun();
        isProtected = false;
    }

    private void setKills(int kills) {
        this.kills = kills;
    }

    private void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getKills() {
        return kills;
    }

    public Player getBukkitPlayer() {
        return bukkitPlayer;
    }

    public void useGun() {
        gun.use(this);
    }

    public void kill(APlayer killer) {
        killer.setKills(killer.getKills() + 1);
        setDeaths(deaths + 1);

        Location location;

        QuakeCraft quakeCraft = QuakeCraft.getInstance();

        do location = quakeCraft.getSpawnpoints().getRandomSpawnpoint();
        while (location.equals(bukkitPlayer.getLocation()) && quakeCraft.getSpawnpoints().getLocations().size() != 1);

        bukkitPlayer.teleport(location);
        setProtection();

        Bukkit.broadcastMessage(StringModifier.getModifiedString(QuakeCraft.getInstance().getConfig().getString("messages.kill"), killer.getBukkitPlayer()).replace("%victim%", bukkitPlayer.getName()));
    }

    private void setProtection() {

        new BukkitRunnable() {

            private long functionalProtectionTime;
            private int protectionTime;
            private long invisibilityTime;
            private boolean isVanished;

            {
                isProtected = true;
                protectionTime = QuakeCraft.getInstance().getConfig().getInt("playerProtectionTime")*1000;
                functionalProtectionTime = System.currentTimeMillis() + protectionTime;
                invisibilityTime = 0;
                isVanished = false;
            }

            @Override
            public void run() {

                if(System.currentTimeMillis() > invisibilityTime) {
                    setVisibility();
                    invisibilityTime = System.currentTimeMillis() + protectionTime/12;
                }

                if(System.currentTimeMillis() > functionalProtectionTime) {
                    isProtected = false;

                    bukkitPlayer.removePotionEffect(PotionEffectType.INVISIBILITY);
                    cancel();
                }

            }

            private void setVisibility() {
                if(isVanished) {
                    bukkitPlayer.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 10, 1, false, false));
                    isVanished = false;
                } else {
                    bukkitPlayer.removePotionEffect(PotionEffectType.INVISIBILITY);
                    isVanished = true;
                }
            }

        }.runTaskTimer(QuakeCraft.getInstance(), 0, 1);
    }

    public boolean isProtected() {
        return isProtected;
    }

}
