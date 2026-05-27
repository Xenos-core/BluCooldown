package me.macronis.blucooldown.manager;

import me.macronis.blucooldown.BluCooldown;
import org.bukkit.Material;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownManager {

    private final BluCooldown plugin;

    private final Map<UUID, Map<Material, Long>> cooldowns = new ConcurrentHashMap<>();

    public CooldownManager(BluCooldown plugin) {
        this.plugin = plugin;
    }

    public boolean hasCooldown(UUID uuid, Material material) {
        Map<Material, Long> map = cooldowns.get(uuid);

        if (map == null) {
            return false;
        }

        Long time = map.get(material);

        if (time == null) {
            return false;
        }

        if (System.currentTimeMillis() >= time) {
            map.remove(material);

            if (map.isEmpty()) {
                cooldowns.remove(uuid);
            }

            return false;
        }

        return true;
    }

    public long getRemaining(UUID uuid, Material material) {
        Map<Material, Long> map = cooldowns.get(uuid);

        if (map == null) {
            return 0;
        }

        Long time = map.get(material);

        if (time == null) {
            return 0;
        }

        return Math.max(0, (time - System.currentTimeMillis()) / 1000);
    }

    public void setCooldown(UUID uuid, Material material, int seconds) {
        cooldowns.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>())
                .put(material, System.currentTimeMillis() + (seconds * 1000L));
    }

    public int getConfiguredCooldown(Material material) {
        String path = "items." + material.name();

        if (!plugin.getConfig().contains(path)) {
            return -1;
        }

        if (!plugin.getConfig().getBoolean(path + ".enabled")) {
            return -1;
        }

        return plugin.getConfig().getInt(path + ".cooldown");
    }
}