package Devilmine.LevelMC;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;

import java.io.File;
import java.util.Objects;

public class CommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {

        // Get plugin.yaml trough Bukkit
        PluginDescriptionFile pdf = Objects.requireNonNull(Bukkit.getServer().getPluginManager().getPlugin("LevelMC")).getDescription();

        // If there's no arguments to the command, return the command usage into the chat
        if(args.length == 0) {
            return false;
        }
        // Command w/ the arg info, returning plugin information
        else if(args[0].equalsIgnoreCase("info")) {
            commandSender.sendMessage("§e§lLevelMC §8⋙ §7 LevelMC is an Hypixel Skyblock inspired leveling system made by §eDevilmine§7!");
            return true;
        }
        // Command for retrieving the version of the Plugin.
        else if (args[0].equalsIgnoreCase("version")) {
            commandSender.sendMessage("§e§lLevelMC §8⋙ §7 This server is running on version §e" + pdf.getVersion() + "§7 on Minecraft §e1.17§7!");
            return true;
        }
        else if (args[0].equalsIgnoreCase("foraging")) {
            // Getting original file for the YAMLConfiguration
            Player p = (Player) commandSender;
            File filel = new File(Bukkit.getServer().getPluginManager().getPlugin("LevelMC").getDataFolder(), p.getUniqueId() + ".yml");

            // Getting Yaml file
            YamlConfiguration file = YamlConfiguration.loadConfiguration(filel);
            commandSender.sendMessage("§e§lLevelMC §8⋙ §e"+ commandSender.getName() + "§7 is foraging level + §e" + (int) file.getDouble("ForagingLVL") + "!");
        }

        // If nothing matches it.
        return false;
    }
}
