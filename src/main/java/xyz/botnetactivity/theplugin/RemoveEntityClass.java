package main.java.xyz.botnetactivity.theplugin;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class RemoveEntityClass {
    private ThePlugin plugin;
    public BukkitTask laggtask;

    public RemoveEntityClass(ThePlugin p) {
        this.plugin = p;
    }

    public void startLagg() {
        int delay = plugin.getConfig().getInt("Clearlag-delay");
        laggtask = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new BukkitRunnable() {

            @Override
            public void run() {
                plugin.getServer().broadcastMessage(ChatColor.GRAY+"["+ChatColor.GREEN + "ClearLag"+ChatColor.GRAY+"]"+ChatColor.GREEN+" Removing Entitys in 60 seconds");
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new BukkitRunnable() {
                    @Override
                    public void run() {
                        cleanEntity();
                    }
                }, 60*20);
            }
        }, 0, ((delay-60)*20));
    }

    public void cleanEntity() {
        int removedentitys = 0;
        for(World world : plugin.getServer().getWorlds()) {
            for(Entity entity : world.getEntities()) {
                if(entity.isOnGround()) {
                    entity.remove();
                    removedentitys++;
                }
            }
        }
        plugin.getServer().broadcastMessage(ChatColor.GRAY+"["+ChatColor.GREEN + "ClearLag"+ChatColor.GRAY+"]"+ChatColor.GREEN+" removed "+removedentitys+" entitys!");
    }
}
