package Devilmine.LevelMC;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public class Leveling extends Listeners {

    // Creating enum for all kinds of Logs
    public enum logs {
        OAK_LOG,
        SPRUCE_LOG,
        BIRCH_LOG,
        JUNGLE_LOG,
        ACACIA_LOG,
        DARK_OAK_LOG,
        CRIMSON_STEM,
    }
    BossBar bar = Bukkit.createBossBar("",BarColor.YELLOW,BarStyle.SOLID);

    @EventHandler
    public void onBlockBreak(BlockBreakEvent bbe) {
        Player p = bbe.getPlayer();
        Block b = bbe.getBlock();
        // Tries to get an enum value of the broken block, if it doesn't exist it won't find it, and it will be null
        try {
            if (logs.valueOf(b.getType().name()).toString() != null) {
                Foraging(b, p);
            }
        } catch (IllegalArgumentException ignored) {};
    }


    public void Foraging(Block log, Player p) {
        // Objects
        // Getting original file for the YAMLConfiguration
        File filel = new File(Bukkit.getServer().getPluginManager().getPlugin("LevelMC").getDataFolder(), p.getUniqueId() + ".yml");

        // Getting Yaml file
        YamlConfiguration file = YamlConfiguration.loadConfiguration(filel);

        if (!file.isSet("ForagingLVL") || file.getDouble("ForagingLVL") < 1) {
            file.set("ForagingLVL", 1);
        }

        // Create enum object to compare
        logs currentlog = logs.valueOf(log.getType().name());
        switch (currentlog) {

            // Granting different XP values
            case OAK_LOG, BIRCH_LOG -> file.set("ForagingXP", file.getDouble("ForagingXP") + 8);
            case ACACIA_LOG -> file.set("ForagingXP", file.getDouble("ForagingXP") + 12);
            case CRIMSON_STEM -> file.set("ForagingXP", file.getDouble("ForagingXP") + 20);

            // Spruce, Jungle, Dark Oak
            default -> file.set("ForagingXP", file.getDouble("ForagingXP") + 4);

        }
        p.playSound(p.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,0.2f,1.0f);

        // Base XP for LVL 2
        double reqxp = 100;

        // Each level increases the current Requirements by 1.2x
        for (int i = 1;i!=file.getDouble("ForagingLVL");i++) {
            reqxp = reqxp * 1.2;
        }
        // If the player has more XP than needed for the level, give him a Level up and remove the current XP to keep overflow XP
        if (file.getDouble("ForagingXP") > reqxp) {
            file.set("ForagingLVL", file.getDouble("ForagingLVL") + 1);
            file.set("ForagingXP", file.getDouble("ForagingXP")-reqxp);
            p.sendMessage("§e§lLevelMC §8⋙ §7Congratulations §e" + p.getDisplayName() +"§7 for leveling up your Foraging to §eLevel " + (int) file.getDouble("ForagingLVL") + "§e!");
            p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1.0f,0.8f);
        }

        // Bossbar for current XP and Level
        bar.setVisible(true );
        bar.setTitle(("§7Foraging: ( §e" + (int) file.getDouble("ForagingXP") + " / " + (int) reqxp + "§7 ) | Current Level: §e" + (int) file.getDouble("ForagingLVL")));
        bar.setColor(BarColor.YELLOW);
        bar.setStyle(BarStyle.SOLID);
        bar.removeAll();
        bar.removePlayer(p);
        //p.sendMessage("XP: " + file.getDouble("ForagingXP") + " | LVL: " + file.getDouble("ForagingLVL"));
        bar.addPlayer(p);
        bar.setProgress(file.getDouble("ForagingXP")/reqxp);

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
}
