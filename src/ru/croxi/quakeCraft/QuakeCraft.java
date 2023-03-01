package ru.croxi.quakeCraft;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import ru.croxi.quakeCraft.command.CommandManager;
import ru.croxi.quakeCraft.game.Game;
import ru.croxi.quakeCraft.game.GameState;
import ru.croxi.quakeCraft.game.player.PlayerManager;
import ru.croxi.quakeCraft.listeners.CancelledEvents;
import ru.croxi.quakeCraft.listeners.Interact;
import ru.croxi.quakeCraft.listeners.JoinQuit;
import ru.croxi.quakeCraft.utils.StringModifier;

public class QuakeCraft extends JavaPlugin {

    private static QuakeCraft instance;
    private Spawnpoints spawnpoints;
    private ItemManager itemManager;
    private PlayerManager playerManager;
    private Game game;

    @Override
    public void onEnable() {

        instance = this;

        loadConfigs();

        itemManager = new ItemManager();

        game = new Game();

        playerManager = new PlayerManager();

        registerListeners();

        new CommandManager();

        editModeMessage();
    }

    @Override
    public void onDisable() {
        Bukkit.getScheduler().cancelAllTasks();
    }

    public void editModeMessage(Player player) {
        if(getGame().getGameState() == GameState.EDITING || getSpawnpoints().areEmpty()) {
            if(player.hasPermission("quakecraft.editmode")) {
                if(getSpawnpoints().areEmpty()) player.sendMessage(StringModifier.getModifiedString(getConfig().getString("messages.locationsAreEmptyError")));
                else player.sendMessage(StringModifier.getModifiedString(getConfig().getString("messages.editModeEnabled")));
            } else player.kickPlayer(StringModifier.getModifiedString(getConfig().getString("messages.noPermissionToEditModeKickMessage")));
        }
    }

    private void editModeMessage() {
        for(Player player: Bukkit.getOnlinePlayers()) editModeMessage(player);
    }

    private void loadConfigs() {
        saveDefaultConfig();

        spawnpoints = new Spawnpoints();
    }

    private void registerListeners() {
        PluginManager pluginManager = Bukkit.getServer().getPluginManager();

        pluginManager.registerEvents(new JoinQuit(), this);
        pluginManager.registerEvents(new Interact(), this);
        pluginManager.registerEvents(new CancelledEvents(), this);
    }

    public static QuakeCraft getInstance() {
        return instance;
    }

    public Spawnpoints getSpawnpoints() {
        return spawnpoints;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public Game getGame() {
        return game;
    }
}
