package me.macronis.blucooldown.util;

import me.macronis.blucooldown.BluCooldown;
import org.bukkit.Bukkit;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class VersionChecker {

    private final BluCooldown plugin;

    public VersionChecker(BluCooldown plugin) {
        this.plugin = plugin;
    }

    public void check() {

        Bukkit.getAsyncScheduler().runNow(plugin, task -> {

            try {

                String url = "https://raw.githubusercontent.com/Xenos-core/BluCooldown/main/version.txt";

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                URI.create(url).toURL().openStream()
                        )
                );

                String latestVersion = reader.readLine();

                reader.close();

                String currentVersion = plugin.getDescription().getVersion();

                if (!currentVersion.equalsIgnoreCase(latestVersion)) {

                    plugin.setUpdateAvailable(true);
                    plugin.setLatestVersion(latestVersion);

                    plugin.getLogger().warning(" ");
                    plugin.getLogger().warning("New update available!");
                    plugin.getLogger().warning("Current version: " + currentVersion);
                    plugin.getLogger().warning("Latest version: " + latestVersion);
                    plugin.getLogger().warning("Download:");
                    plugin.getLogger().warning("https://github.com/Xenos-core/BluCooldown/releases");
                    plugin.getLogger().warning(" ");

                    return;
                }

                plugin.getLogger().info("BluCooldown is running latest version.");

            } catch (Exception exception) {

                plugin.getLogger().warning("Could not check for updates.");
             /* plugin.getLogger().warning("Could not check for updates."); */
            }

        });
    }
}