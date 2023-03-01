package ru.croxi.quakeCraft.game;

import net.minecraft.server.v1_8_R1.EnumParticle;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftFirework;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;
import ru.croxi.quakeCraft.QuakeCraft;
import ru.croxi.quakeCraft.game.countdowns.GunCountdown;
import ru.croxi.quakeCraft.game.player.APlayer;
import ru.croxi.quakeCraft.utils.MathUtil;
import ru.croxi.quakeCraft.utils.Particle;

public class Gun {

    private static QuakeCraft quakeCraft;
    private GunCountdown gunCountdown;

    public Gun() {
        quakeCraft = QuakeCraft.getInstance();
        gunCountdown = new GunCountdown();
    }

    public void use(APlayer player) {
        Player bukkitPlayer = player.getBukkitPlayer();

        if(quakeCraft.getGame().getGameState() == GameState.GAME) {
            if(gunCountdown.start(bukkitPlayer)) {
                Location location = bukkitPlayer.getEyeLocation();
                Vector direction = location.getDirection().normalize().multiply(0.2);

                for(Player player2: Bukkit.getOnlinePlayers()) {
                    player2.playSound(location, Sound.BLAZE_HIT, 0.55f, 0);
                    player2.playSound(location, Sound.ZOMBIE_WOOD, 0.5f, 0);
                }

                for(int length = 0; length<250; length++) {
                    location.add(direction);

                    Particle.display(EnumParticle.REDSTONE, location, 0, 0, 0, 0, 1);

                    if(location.getBlock().getType().isSolid()) {
                        instantFirework(location);
                        return;
                    }

                    for(Player target: Bukkit.getOnlinePlayers()) {
                        APlayer aTarget = quakeCraft.getPlayerManager().getPlayer(target);

                        if(!bukkitPlayer.getName().equals(target.getName()) && !aTarget.isProtected() && (location.distance(target.getLocation().add(0, 0.5, 0)) < 2.3)) {
                            aTarget.kill(player);
                            quakeCraft.getGame().getStats().sendAll();

                            instantFirework(location);
                            return;
                        }
                    }
                }
            }
        }
    }

    private static void instantFirework(Location location) {
        Firework firework = location.getWorld().spawn(location, Firework.class);
        FireworkMeta fireworkMeta = firework.getFireworkMeta();
        fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.fromRGB(MathUtil.getRandomInt(255), MathUtil.getRandomInt(255), MathUtil.getRandomInt(255))).withTrail().build());
        firework.setFireworkMeta(fireworkMeta);
        ((CraftFirework)firework).getHandle().expectedLifespan = 1;
    }

}