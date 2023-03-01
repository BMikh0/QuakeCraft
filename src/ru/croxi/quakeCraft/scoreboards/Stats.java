package ru.croxi.quakeCraft.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import ru.croxi.quakeCraft.QuakeCraft;
import ru.croxi.quakeCraft.game.player.APlayer;
import ru.croxi.quakeCraft.utils.StringModifier;

import java.util.List;

public class Stats {

    private static ConfigurationSection section;
    private static Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    private static ObjectiveWrapper objectiveWrapper;

    public Stats() {
        section = QuakeCraft.getInstance().getConfig().getConfigurationSection("objective");
        objectiveWrapper = new ObjectiveWrapper(scoreboard.registerNewObjective(StringModifier.getModifiedString(section.getString("name")), "stats"));
    }

    public void sendAll() {
        List<String> strings = section.getStringList("content");
        APlayer[] sortedPlayers = getLeaders();

        objectiveWrapper.clear();

        for(int i = strings.size()-1; i >= 0; i--) {
            String string = strings.get(i);

            if(string.contains("%players%")) {
                for(int j = sortedPlayers.length-1; j >= 0; j--) {
                    APlayer player = sortedPlayers[j];

                    objectiveWrapper.addString(StringModifier.getModifiedString(section.getString("placeFormat"), player.getBukkitPlayer())
                            .replace("%place%", String.valueOf(j+1))
                            .replace("%kills%", String.valueOf(player.getKills())));
                }
            } else objectiveWrapper.addString(StringModifier.getModifiedString(string));
        }

        objectiveWrapper.sendAll();
    }

    public APlayer[] getLeaders() {
        APlayer[] players = QuakeCraft.getInstance().getPlayerManager().getPlayers().toArray(new APlayer[0]);

        for(int i = 0; i < players.length; i++) {
            for (int j = 1; j < players.length-i; j++) {
                if (players[j-1].getKills() < players[j].getKills()) {
                    APlayer temp = players[j-1];
                    players[j-1] = players[j];
                    players[j] = temp;
                }
            }
        }

        return players;
    }

}