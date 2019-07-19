package main.java.xyz.botnetactivity.theplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class EventsClass implements Listener {
    private ThePlugin plugin;
    private TeleportClass teleport;

    public EventsClass(ThePlugin p) {
        this.plugin = p;
        teleport = new TeleportClass(p);
    }



    static public HashMap<UUID, BukkitTask> teleports = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if(teleports.containsKey(uuid)) {
            BukkitTask task = teleports.get(uuid);
            task.cancel();
            teleports.remove(uuid);
            player.sendMessage(ChatColor.RED + "You Moved! Teleportation canceled");
        }
    }

    @EventHandler
    public void chatEvent(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();

        boolean isEnabled = plugin.getConfig().getBoolean("anticurse-enabled");
        List<String> wordlist = plugin.getConfig().getStringList("anticurse-bannedwords");

        if (isEnabled) {
            for (String bannedword : wordlist) {
                if (message.contains(bannedword)) {
                    event.setCancelled(true);
                    player.sendMessage(ChatColor.RED + "You May Not Use Such Vulgar Language");
                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        event.setJoinMessage("");
        if (player.hasPlayedBefore()) {
            plugin.getServer().broadcastMessage(ChatColor.GRAY + "[" + ChatColor.GREEN + "+" + ChatColor.GRAY + "] " + ChatColor.AQUA + player.getName());
            //TODO: send motd
        } else {
            plugin.getServer().broadcastMessage(ChatColor.GOLD + player.getName()+" has joined for the first time!");
            teleport.teleport_spawn(player, false);
            //TODO: send first join motd
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        event.setQuitMessage("");
        plugin.getServer().broadcastMessage(ChatColor.GRAY +"[" + ChatColor.RED + "-" +ChatColor.GRAY + "] " + ChatColor.AQUA + player.getName());
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        event.setRespawnLocation(teleport.getSpawnLoc());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        event.setDeathMessage(ChatColor.YELLOW +"Player "+player.getDisplayName()+" died!");
    }

}
