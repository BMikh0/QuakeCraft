package ru.croxi.quakeCraft.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import ru.croxi.quakeCraft.QuakeCraft;
import ru.croxi.quakeCraft.command.subCommands.*;
import ru.croxi.quakeCraft.utils.StringModifier;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements CommandExecutor {

    protected static QuakeCraft quakeCraft;
    private static List<SubCommand> subCommands = new ArrayList<>();;

    public CommandManager() {
        quakeCraft = QuakeCraft.getInstance();

        quakeCraft.getServer().getPluginCommand("quake").setExecutor(this);
        registerSubCommands();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("quakecraft.help")) {
            SubCommand.noPerm(sender);

            return true;
        }

        if(!(sender instanceof Player) && !(sender instanceof ConsoleCommandSender)) {
            SubCommand.notAllowed(sender);
            return true;
        }

        if(args.length == 0) {
            showHelp(sender);
            return true;
        }

        for(SubCommand subCommand: subCommands) {

            if(!sender.hasPermission(subCommand.getPermission())) {
                SubCommand.noPerm(sender);

                return true;
            }

            if(subCommand.is(args[0])) {

                subCommand.onExecute(sender, args);

                return true;
            }
        }

        showHelp(sender);

        return true;
    }

    private void showHelp(CommandSender sender) {
        ConfigurationSection cfgSection = quakeCraft.getConfig().getConfigurationSection("help");
        String commandFormat = cfgSection.getString("commandFormat");

        for(String str: cfgSection.getStringList("message")) {
            if(str.contains("%commands%")) {
                for (SubCommand subCommand: subCommands) {
                    if(sender.hasPermission(subCommand.getPermission()))
                        sender.sendMessage(StringModifier.getModifiedString(commandFormat).replace("%command%", subCommand.getUsage()).replace("%description%", subCommand.getDescription()));
                }
            } else sender.sendMessage(StringModifier.getModifiedString(str));
        }
    }

    private void registerSubCommands() {
        subCommands.add(new Start(quakeCraft));
        subCommands.add(new Spawnpoint(quakeCraft));
    }

}
