package ru.croxi.quakeCraft.command.subCommands;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import ru.croxi.quakeCraft.QuakeCraft;
import ru.croxi.quakeCraft.Spawnpoints;
import ru.croxi.quakeCraft.command.SubCommand;
import ru.croxi.quakeCraft.game.GameState;
import ru.croxi.quakeCraft.utils.StringModifier;

public class Spawnpoint extends SubCommand {

    public Spawnpoint(QuakeCraft quakeCraft) {
        super(quakeCraft, "spawnpoint <create;remove <номер>>", quakeCraft.getConfig().getString("commandDescription.spawnpoint"), "quakecraft.editMode", "spawnpoint");
    }

    @Override
    protected void onExecute(CommandSender sender, String... args) {
        Spawnpoints spawnpoints = getQuakeCraft().getSpawnpoints();
        ConfigurationSection messages = getConfig().getConfigurationSection("messages");

        if(getQuakeCraft().getGame().getGameState() != GameState.EDITING) {
            sender.sendMessage(StringModifier.getModifiedString(messages.getString("editModeError")));
            return;
        }

        if(args.length >= 2) {
            switch (args[1]) {
                case "create":
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(StringModifier.getModifiedString(messages.getString("onlyForPlayers")));
                        return;
                    }

                    int id = spawnpoints.create(((Player) sender).getLocation());

                    sender.sendMessage(id == -1 ? StringModifier.getModifiedString("Нельзя создавать две одинаковые точки!") : StringModifier.getModifiedString((messages.getString("spawnpointCreated")).replace("%id%", String.valueOf(id))));

                    break;
                case "remove":
                    if (args.length == 3) {
                        if (args[2].matches("[0-9]+"))
                            sender.sendMessage(spawnpoints.remove(Integer.valueOf(args[2])) ? StringModifier.getModifiedString(messages.getString("spawnpointRemoved")) : StringModifier.getModifiedString(messages.getString("spawnpointDoesNotExist")).replace("%id%", String.valueOf(args[2])));
                        else
                            sender.sendMessage(StringModifier.getModifiedString(messages.getString("onlyNumbersAreAllowed")));
                    } else sender.sendMessage(StringModifier.getModifiedString(messages.getString("idIsEmptyError")));
                    break;
                default:
                    invalidArgs(sender, messages);
                    break;
            }
        } else invalidArgs(sender, messages);
    }

    private void invalidArgs(CommandSender sender, ConfigurationSection messages) {
        sender.sendMessage(StringModifier.getModifiedString(messages.getString("invalidCommand")).replace("%usage%", getUsage()));
    }

}
