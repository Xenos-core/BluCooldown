package me.macronis.blucooldown;

import me.macronis.blucooldown.command.BluCooldownCommand;
import me.macronis.blucooldown.util.VersionChecker;
import me.macronis.blucooldown.listener.ItemUseListener;
import me.macronis.blucooldown.listener.UpdateListener;
import me.macronis.blucooldown.manager.CooldownManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;


public final class BluCooldown extends JavaPlugin {

    private static BluCooldown instance;
    private CooldownManager cooldownManager;
    private boolean updateAvailable;
    private String latestVersion;

    @Override
    public void onEnable() {
        instance = this;


        getServer().getPluginManager().registerEvents(
                new UpdateListener(this),
                this
        );

        saveDefaultConfig();
        reloadPluginConfig();

        cooldownManager = new CooldownManager(this);
        registerCommands();

        getServer().getPluginManager().registerEvents(
                new ItemUseListener(this),
                this
        );

        new VersionChecker(this).check();
    }

    public static BluCooldown getInstance() {
        return instance;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public void reloadPluginConfig() {
        reloadConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    private void registerCommands() {
        PluginCommand command = getCommand("blucooldown");

        if (command == null) {
            getLogger().warning("Could not register /blucooldown command.");
            return;
        }

        BluCooldownCommand bluCooldownCommand = new BluCooldownCommand(this);
        command.setExecutor(bluCooldownCommand);
        command.setTabCompleter(bluCooldownCommand);
    }

    public boolean isUpdateAvailable() {
        return updateAvailable;
    }
    
    public void setUpdateAvailable(boolean updateAvailable) {
        this.updateAvailable = updateAvailable;
    }

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }
}
