package main.java.xyz.botnetactivity.theplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Set;

public class TeleportClass {
    private ThePlugin plugin;

    public TeleportClass(ThePlugin p) {
        this.plugin = p;
    }

    public Location getSpawnLoc() {
        World world = Bukkit.getWorld(plugin.getConfig().getString("spawn-loc.world"));
        double x = plugin.getConfig().getDouble("spawn-loc.x");
        double y = plugin.getConfig().getDouble("spawn-loc.y");
        double z = plugin.getConfig().getDouble("spawn-loc.z");
        float yaw = (float) plugin.getConfig().getDouble("spawn-loc.yaw");
        float pitch = (float) plugin.getConfig().getDouble("spawn-loc.pitch");

        Location loc = new Location(world, x, y, z, yaw, pitch);
        return loc;
    }

    public void teleport_spawn(Player player, Boolean isDelay) {
        Location loc = this.getSpawnLoc();

        if(isDelay) {
            Integer delay = plugin.getConfig().getInt("spawn-loc.delay");
            player.sendMessage(ChatColor.GREEN + "Dont move! teleporting in " + delay + " seconds!");
            EventsClass.teleports.put(player.getUniqueId(), plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable() {
                @Override
                public void run() {
                    player.sendMessage(ChatColor.LIGHT_PURPLE + "Teleported to spawn");
                    player.teleport(loc);
                    EventsClass.teleports.remove(player.getUniqueId());
                }
            }, delay * 20));
        } else {
            player.teleport(loc);
        }
    }

    public boolean teleport_warp(Player player, String warp) {
        Set<String> warps = plugin.getConfig().getConfigurationSection("warps").getKeys(false);
        for (String swarp : warps) {
            if (swarp.equalsIgnoreCase(warp)) {
                if(!player.hasPermission("ThePlugin.warp."+warp)) {
                    player.sendMessage(ChatColor.DARK_RED + "No permission for warp "+warp);
                    return true;
                }
                World world = Bukkit.getWorld(plugin.getConfig().getString("warps." + warp + ".world"));
                double x = plugin.getConfig().getDouble("warps." + warp + ".x");
                double y = plugin.getConfig().getDouble("warps." + warp + ".y");
                double z = plugin.getConfig().getDouble("warps." + warp + ".z");
                float yaw = (float) plugin.getConfig().getDouble("warps." + warp + ".yaw");
                float pitch = (float) plugin.getConfig().getDouble("warps." + warp + ".pitch");
                int delay = plugin.getConfig().getInt("warps." + warp + ".delay");
                Location loc = new Location(world, x, y, z, yaw, pitch);

                player.sendMessage(ChatColor.GREEN + "Dont move! teleporting in " + delay + " seconds!");
                EventsClass.teleports.put(player.getUniqueId(), plugin.getServer().getScheduler().runTaskLater(plugin, new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.sendMessage(ChatColor.LIGHT_PURPLE + "Teleported to warp"+warp);
                        player.teleport(loc);
                        EventsClass.teleports.remove(player.getUniqueId());
                    }
                }, delay * 20));

                return true;
            }
        }
        return false;
    }
}
