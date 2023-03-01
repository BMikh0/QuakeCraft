package ru.croxi.quakeCraft.game.countdowns;


import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.croxi.quakeCraft.QuakeCraft;
import ru.croxi.quakeCraft.utils.StringModifier;

import java.text.DecimalFormat;

public class GunCountdown {

    private static QuakeCraft quakeCraft;
    private static int maxTime;
    private static int step;
    private static DecimalFormat format = new DecimalFormat();
    private long time;

    public GunCountdown() {
        quakeCraft = QuakeCraft.getInstance();
        GunCountdown.maxTime = quakeCraft.getConfig().getInt("gunTime") * 1000;
        step = maxTime/100;

        format.setDecimalSeparatorAlwaysShown(false);
        format.setMaximumFractionDigits(1);
    }

    public boolean start(Player player) {
        if(maxTime == 0) return true;

        if(time > System.currentTimeMillis()) {
            player.sendMessage(StringModifier.getModifiedString(quakeCraft.getConfig().getString("messages.gunWaiting")).replace("%time%", format.format((time - System.currentTimeMillis())/1000f)));
            return false;
        }

        time = System.currentTimeMillis() + maxTime;

        new BukkitRunnable() {

            @Override
            public void run() {
                for(int i = 0; i<(maxTime/step); i++) {
                    float percent = (1 - ((float) (time - System.currentTimeMillis()))/maxTime) * 100;

                    player.setLevel((int) (percent) < 100 ? (int) (percent) : 100);
                    player.setExp(percent/100);

                    try {
                        Thread.sleep(step);
                    } catch (InterruptedException e) {e.printStackTrace();}
                }
            }

        }.runTaskAsynchronously(quakeCraft);

        return true;
    }

}
