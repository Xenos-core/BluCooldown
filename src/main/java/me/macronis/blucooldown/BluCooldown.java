package me.macronis.blucooldown;

import me.macronis.blucooldown.listener.ItemUseListener;
import me.macronis.blucooldown.manager.CooldownManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class BluCooldown extends JavaPlugin {

    private static BluCooldown instance;
    private CooldownManager cooldownManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        cooldownManager = new CooldownManager(this);

        getServer().getPluginManager().registerEvents(
                new ItemUseListener(this),
                this
        );
    }

    public static BluCooldown getInstance() {
        return instance;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
}