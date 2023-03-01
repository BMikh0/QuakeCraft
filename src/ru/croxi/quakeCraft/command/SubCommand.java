package ru.croxi.quakeCraft.command;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import ru.croxi.quakeCraft.QuakeCraft;
import ru.croxi.quakeCraft.utils.StringModifier;

import java.util.Arrays;

public abstract class SubCommand {

    private static QuakeCraft quakeCraft;
    private static FileConfiguration config;
    private String usage, description, permission;
    private String[] aliases;

    protected SubCommand(QuakeCraft quakeCraft, String usage, String description, String permission, String... aliases) {
        SubCommand.quakeCraft = quakeCraft;
        SubCommand.config = quakeCraft.getConfig();
        this.usage = usage;
        this.description = description;
        this.permission = permission;
        this.aliases = aliases;
    }

    boolean is(String arg) {
        return Arrays.asList(aliases).contains(arg);
    }

    protected abstract void onExecute(CommandSender sender, String... args);

    protected String getUsage() {
        return usage;
    }

    String getDescription() {
        return description;
    }

    String getPermission() {
        return permission;
    }

    static void notAllowed(CommandSender sender) {
        sender.sendMessage(StringModifier.getModifiedString(config.getString("messages.notAllowed")));
    }

    static void noPerm(CommandSender sender) {
        sender.sendMessage(StringModifier.getModifiedString(config.getString("messages.noPermission")));
    }

    protected QuakeCraft getQuakeCraft() {
        return quakeCraft;
    }

    protected FileConfiguration getConfig() {
        return config;
    }

}
