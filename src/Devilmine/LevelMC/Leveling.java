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
                Foraging fg = new Foraging(b,p);
            }
        } catch (IllegalArgumentException ignored) {};
    }
}
