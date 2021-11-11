package Devilmine.LevelMC;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.File;
import java.io.IOException;

public class Listeners implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent pje) {
        Player player = pje.getPlayer();
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("LevelMC").getDataFolder(), player.getUniqueId() + ".yml"); // Create a file object, with the UUID of the User.
            if(!file.exists()) { // Checks if the player doesn't already have a file created for him
                try {
                    file.createNewFile(); // If it doesn't exist yet, create a file.
                    System.out.println("[LevelMC] File created for player \"" + player.getDisplayName() + "\" with the  UUID \"" + player.getUniqueId() + "\"");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    @EventHandler
    public void onLeave(PlayerQuitEvent pqe) {

    }
}
