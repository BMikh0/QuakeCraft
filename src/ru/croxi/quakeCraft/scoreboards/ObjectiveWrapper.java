package ru.croxi.quakeCraft.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

public class ObjectiveWrapper {

    private Objective objective;
    private List<String> strings;

    ObjectiveWrapper(Objective objective) {
        this.objective = objective;
        strings = new ArrayList<>();
    }

    void addString(String string) {
        strings.add(string);
        objective.getScore(string).setScore(strings.size());
    }

    void clear() {
        Scoreboard scoreboard = objective.getScoreboard();
        for(String string: strings) scoreboard.resetScores(string);

        strings.clear();
    }

    void sendAll() {
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        for(Player player: Bukkit.getOnlinePlayers()) player.setScoreboard(objective.getScoreboard());
    }

}
