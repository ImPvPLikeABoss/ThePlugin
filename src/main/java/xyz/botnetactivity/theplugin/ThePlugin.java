package main.java.xyz.botnetactivity.theplugin;

import org.bukkit.plugin.java.JavaPlugin;

public final class ThePlugin extends JavaPlugin {
    private CommandClass commands = new CommandClass(this);
    private RemoveEntityClass lagg = new RemoveEntityClass(this);

    @Override
    public void onEnable() {
        initCmd();
        initConfig();
        lagg.startLagg();
        this.getServer().getPluginManager().registerEvents(new EventsClass(this), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void initCmd() {
        this.getCommand("help").setExecutor(commands);
        this.getCommand("i").setExecutor(commands);
        this.getCommand("spawn").setExecutor(commands);
        this.getCommand("setspawn").setExecutor(commands);
        this.getCommand("warp").setExecutor(commands);
        this.getCommand("setwarp").setExecutor(commands);
        this.getCommand("delwarp").setExecutor(commands);
        this.getCommand("fly").setExecutor(commands);
    }

    private void initConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
    }
}
