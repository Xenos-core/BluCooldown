package me.macronis.blucooldown;

import me.macronis.blucooldown.util.VersionChecker;
import me.macronis.blucooldown.listener.ItemUseListener;
import me.macronis.blucooldown.listener.UpdateListener;
import me.macronis.blucooldown.manager.CooldownManager;
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

        cooldownManager = new CooldownManager(this);

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