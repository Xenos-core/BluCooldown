package me.macronis.blucooldown.manager;

import me.macronis.blucooldown.BluCooldown;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class CooldownManager {

    private final BluCooldown plugin;

    private final Map<UUID, Map<String, Long>> cooldowns = new ConcurrentHashMap<>();

    public CooldownManager(BluCooldown plugin) {
        this.plugin = plugin;
    }

    public boolean hasCooldown(UUID uuid, String key) {

        Map<String, Long> map = cooldowns.get(uuid);

        if (map == null) {
            return false;
        }

        Long time = map.get(key);

        if (time == null) {
            return false;
        }

        if (System.currentTimeMillis() >= time) {
            map.remove(key);

            if (map.isEmpty()) {
                cooldowns.remove(uuid);
            }

            return false;
        }

        return true;
    }

    public long getRemaining(UUID uuid, String key) {

        Map<String, Long> map = cooldowns.get(uuid);

        if (map == null) {
            return 0;
        }

        Long time = map.get(key);

        if (time == null) {
            return 0;
        }

        return Math.max(0, (time - System.currentTimeMillis()) / 1000);
    }

    public void setCooldown(UUID uuid, String key, int seconds) {
        cooldowns.computeIfAbsent(uuid, k -> new ConcurrentHashMap<>())
                .put(key, System.currentTimeMillis() + (seconds * 1000L));
    }

    public int getConfiguredCooldown(String key) {

        String path = "items." + key;

        if (!plugin.getConfig().contains(path)) {
            return -1;
        }

        if (!plugin.getConfig().getBoolean(path + ".enabled")) {
            return -1;
        }

        return plugin.getConfig().getInt(path + ".cooldown");
    }
}
