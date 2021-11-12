package Devilmine.LevelMC;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;

public class Foraging {

    BossBar bar = Bukkit.createBossBar("", BarColor.YELLOW, BarStyle.SOLID);
    // Required XP for Foraging Levels
    double basereqxp = 100;

    public Foraging(Block log, Player p) {

        // Getting original file for the YAMLConfiguration
        File filel = new File(Bukkit.getServer().getPluginManager().getPlugin("LevelMC").getDataFolder(), p.getUniqueId() + ".yml");
        // Getting Yaml file
        YamlConfiguration file = YamlConfiguration.loadConfiguration(filel);

        if (!file.isSet("ForagingLVL") || getLVL(p) < 1) {
            file.set("ForagingLVL", 1);
        }

        // Create enum object to compare
        Leveling.logs currentlog = Leveling.logs.valueOf(log.getType().name());
        switch (currentlog) {

            // Granting different XP values
            case OAK_LOG, BIRCH_LOG -> file.set("ForagingXP", getXP(p) + 8);
            case ACACIA_LOG -> file.set("ForagingXP", getXP(p) + 12);
            case CRIMSON_STEM -> file.set("ForagingXP", getXP(p) + 20);

            // Spruce, Jungle, Dark Oak
            default -> file.set("ForagingXP", getXP(p) + 4);

        }
        // Play sound at the Location of the Player
        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,0.2f,1.0f);

        double reqxp = getreqxp(p);
        // If the player has more XP than needed for the level, give him a Level up and remove the current XP to keep overflow XP
        if (file.getDouble("ForagingXP") > reqxp) {
            file.set("ForagingLVL", file.getDouble("ForagingLVL") + 1);
            file.set("ForagingXP", file.getDouble("ForagingXP")-reqxp);
            p.sendMessage("§e§lLevelMC §8⋙ §7Congratulations §e" + p.getDisplayName() +"§7 for leveling up your Foraging to §eLevel " + (int) getLVL(p) + "§e!");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1.0f,0.8f);
        }

        // Bossbar for current XP and Level
        bar.setVisible(true);
        bar.setTitle(("§7Foraging: ( §e" + (int) getXP(p) + " / " + (int) reqxp + "§7 ) | Current Level: §e" + (int) getLVL(p)));
        bar.setColor(BarColor.YELLOW);
        bar.setStyle(BarStyle.SOLID);
        bar.removeAll();
        bar.removePlayer(p);
        bar.addPlayer(p);
        bar.setProgress(getXP(p)/reqxp);

        new BukkitRunnable() {

            @Override
            public void run() {
                try {
                    bar.removeAll();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskLater(Bukkit.getPluginManager().getPlugin("LevelMC"), 50);

        try {
            file.save(filel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double getXP(Player p) {
        File filel = new File(Bukkit.getServer().getPluginManager().getPlugin("LevelMC").getDataFolder(), p.getUniqueId() + ".yml");
        YamlConfiguration file = YamlConfiguration.loadConfiguration(filel);
        return file.getDouble("ForagingXP");
    }
    public double getLVL(Player p) {
        File filel = new File(Bukkit.getServer().getPluginManager().getPlugin("LevelMC").getDataFolder(), p.getUniqueId() + ".yml");
        YamlConfiguration file = YamlConfiguration.loadConfiguration(filel);
        return file.getDouble("ForagingLVL");
    }
    public double getreqxp(Player p) {
        double reqxp = basereqxp;

        // Each level increases the current Requirements by 1.2x
        for (int i = 1;i!=getLVL(p);i++) {
            reqxp = reqxp * 1.2;
        }
        return reqxp;
    }
}
