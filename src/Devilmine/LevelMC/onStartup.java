package Devilmine.LevelMC;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class onStartup extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        // Create an instance
        onStartup startup = this;

        // Create the LevelMC folder
        File dir = new File(this.getDataFolder(), "LevelMC");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Register events
        Bukkit.getPluginManager().registerEvents(new Listeners(), this);

        // Create command "LevelMC"
        Objects.requireNonNull(this.getCommand("levelmc")).setExecutor(new CommandHandler());

        // Create player data
        if (Bukkit.getOnlinePlayers() != null) {
            System.out.println("[LevelMC] Create File for currently online Players");
            for (Player list : Bukkit.getOnlinePlayers()) {
                {
                    createFile(list.getPlayer());
                }
            }
        }
    }
    public void onDisable() {
    }
    public void createFile(Player player) {
        File file = new File(this.getDataFolder(), player.getUniqueId() + ".yml"); // Create a file object, with the UUID of the User.
        if(!file.exists()) { // Checks if the player doesn't already have a file created for him
            try {
                file.createNewFile(); // If it doesn't exist yet, create a file.
                System.out.println("[LevelMC] File created for player \"" + player.getDisplayName() + "\" with the  UUID \"" + player.getUniqueId() + "\"");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
