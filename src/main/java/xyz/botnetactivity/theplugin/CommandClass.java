package main.java.xyz.botnetactivity.theplugin;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class CommandClass implements Listener, CommandExecutor {
    private ThePlugin plugin;
    private TeleportClass teleport;

    public CommandClass(ThePlugin p) {
        this.plugin = p;
        teleport = new TeleportClass(p);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("spawn")) {
            if (sender instanceof Player) {
                if(!sender.hasPermission("ThePlugin.spawn.nodelay")) {
                    teleport.teleport_spawn((Player) sender, true);
                    return true;
                } else {
                    teleport.teleport_spawn((Player) sender, false);
                    sender.sendMessage(ChatColor.LIGHT_PURPLE + "Teleported to spawn");
                }
            } else {
                sender.sendMessage("Only players may use this command!");
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("setspawn")) {
            if (sender instanceof Player) {
                if(!sender.hasPermission("ThePlugin.setspawn")) {
                    sender.sendMessage(ChatColor.DARK_RED + "You dont have permission ThePlugin.setspawn");
                    return true;
                }
                Location loc = ((Player) sender).getLocation();
                plugin.getConfig().set("spawn-loc.world", loc.getWorld().getName());
                plugin.getConfig().set("spawn-loc.x", loc.getX());
                plugin.getConfig().set("spawn-loc.y", loc.getY());
                plugin.getConfig().set("spawn-loc.z", loc.getZ());
                plugin.getConfig().set("spawn-loc.pitch", loc.getPitch());
                plugin.getConfig().set("spawn-loc.yaw", loc.getYaw());
                plugin.saveConfig();
                sender.sendMessage(ChatColor.GREEN + "Set server spawn!");
            } else {
                sender.sendMessage("Only players may use this command!");
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("warp")) {
            if (sender instanceof Player) {
                if (args.length == 1) {
                    if (!teleport.teleport_warp((Player) sender, args[0])) {
                        sender.sendMessage(ChatColor.RED + "Invalid Warp " + args[0] + ". To see all avalible warps do /warp");
                    }
                } else {
                    int warplist_len = 0;
                    Set<String> warps = plugin.getConfig().getConfigurationSection("warps").getKeys(false);
                    Iterator<String> warpsitr = warps.iterator();
                    StringBuilder message = new StringBuilder();

                    message.append(ChatColor.AQUA + "List Of Avalable Warps: ");

                    for (int i = 0; i < warps.size(); i++) {
                        String warp = warpsitr.next();
                        if(!sender.hasPermission("ThePlugin.warp."+warp)) continue;
                        if (i != warps.size() - 1) message.append(ChatColor.LIGHT_PURPLE + warp + ", ");
                        else message.append(ChatColor.LIGHT_PURPLE +warp);
                        warplist_len++;
                    }
                    if(warplist_len == 0) {
                        sender.sendMessage(ChatColor.AQUA + "There is currently no avalible warps");
                    } else {
                        sender.sendMessage(message.toString());
                    }
                }
            } else {
                sender.sendMessage("Only players may use this command!");
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("setwarp")) {
            if (sender instanceof Player) {
                if(!sender.hasPermission("ThePlugin.setwarp")) {
                    sender.sendMessage(ChatColor.DARK_RED + "You dont have permission ThePlugin.setwarp");
                    return true;
                }
                if (args.length == 1 | args.length == 2) {
                    Player player = ((Player) sender).getPlayer();
                    Location loc = player.getLocation();
                    Integer delay = 5;
                    if (args.length == 2) delay = Integer.parseInt(args[1]);
                    plugin.getConfig().set("warps." + args[0] + ".delay", delay);
                    plugin.getConfig().set("warps." + args[0] + ".world", loc.getWorld().getName());
                    plugin.getConfig().set("warps." + args[0] + ".x", loc.getX());
                    plugin.getConfig().set("warps." + args[0] + ".y", loc.getY());
                    plugin.getConfig().set("warps." + args[0] + ".z", loc.getZ());
                    plugin.getConfig().set("warps." + args[0] + ".pitch", loc.getPitch());
                    plugin.getConfig().set("warps." + args[0] + ".yaw", loc.getYaw());
                    plugin.saveConfig();
                    sender.sendMessage(ChatColor.GREEN + "Set warp " + args[0] + " with delay of " + delay);
                } else {
                    sender.sendMessage(ChatColor.GOLD + "Usage: /setwarp <warpname> [delay]");
                }
            } else {
                sender.sendMessage("Only players may use this command!");
            }
            return true;
        }
        if(command.getName().equalsIgnoreCase("delwarp")) {
            if(!sender.hasPermission("ThePlugin.delwarp")) {
                sender.sendMessage(ChatColor.DARK_RED + "You dont have permission ThePlugin.delwarp");
                return true;
            }
            if(args.length == 1) {
                Set<String> warps = plugin.getConfig().getConfigurationSection("warps").getKeys(false);
                for(String warp : warps) {
                    if(warp.equalsIgnoreCase(args[0])) {
                        plugin.getConfig().set("warps."+args[0], null);
                        plugin.saveConfig();
                        sender.sendMessage(ChatColor.AQUA + "Deleted warp "+args[0]);
                        return true;
                    }
                }
                sender.sendMessage(ChatColor.RED + "Warp doesnt exist "+args[0]+"!");
            } else {
                sender.sendMessage(ChatColor.GOLD + "Usage: /delwarp <warpname>");
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("help")) {
            List<String> helplist = plugin.getConfig().getStringList("server-help");
            for (String line : helplist) {
                sender.sendMessage(line);
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("i")) {
            if (sender instanceof Player) {
                if(!sender.hasPermission("ThePlugin.giveitem")) {
                    sender.sendMessage(ChatColor.DARK_RED + "You dont have permission ThePlugin.giveitem");
                    return true;
                }
                if (args.length >= 1) {
                    Material item = Material.getMaterial(args[0].toUpperCase());
                    if (item != null) {
                        Inventory inv = ((Player) sender).getInventory();
                        Integer amount = 64;
                        if (args.length == 2) {
                            amount = Integer.parseInt(args[1]);
                        }
                        inv.addItem(new ItemStack(item, amount));
                        sender.sendMessage(ChatColor.BLUE + "Added " + amount + " of " + args[0] + " to inventory!");
                    } else {
                        sender.sendMessage(ChatColor.AQUA + args[0] + ChatColor.RED + " isn't a valid item");
                    }
                } else {
                    sender.sendMessage(ChatColor.GOLD + "Usage: /i <item> [amount]");
                }
            } else {
                sender.sendMessage("Only players may use this command!");
            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("fly")) {
            if (sender instanceof Player) {
                if(!sender.hasPermission("ThePlugin.fly")) {
                    sender.sendMessage(ChatColor.DARK_RED + "You dont have permission ThePlugin.fly");
                    return true;
                }
                if (((Player) sender).getAllowFlight()) {
                    ((Player) sender).setAllowFlight(false);
                    sender.sendMessage(ChatColor.GREEN + "Set fly mode to false!");
                } else {
                    ((Player) sender).setAllowFlight(true);
                    sender.sendMessage(ChatColor.GREEN + "Set fly mode to true!");
                }
            } else {
                sender.sendMessage("Only players may use this command!");
            }
            return true;
        }

        return false;
    }
}
