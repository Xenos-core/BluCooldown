package me.macronis.blucooldown.util;

import me.macronis.blucooldown.BluCooldown;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;

public class VersionChecker {

    private static final String REPO_API_URL = "https://api.github.com/repos/Xenos-core/BluCooldown/releases/latest";

    private final BluCooldown plugin;

    public VersionChecker(BluCooldown plugin) {
        this.plugin = plugin;
    }

    public void check() {

        Bukkit.getAsyncScheduler().runNow(plugin, task -> {

            HttpURLConnection connection = null;

            try {

                URI uri = URI.create(REPO_API_URL);
                connection = (HttpURLConnection) uri.toURL().openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept", "application/vnd.github+json");
                connection.setRequestProperty("User-Agent", "BluCooldown-Plugin");
                connection.setConnectTimeout(5000);
                connection.setReadTimeout(5000);

                int responseCode = connection.getResponseCode();

                if (responseCode != 200) {
                    plugin.getLogger().warning("Could not check for updates. (HTTP " + responseCode + ")");
                    return;
                }

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                String latestVersion = extractTag(response.toString());

                if (latestVersion == null || latestVersion.isBlank()) {
                    plugin.getLogger().warning("Could not check for updates. (invalid response)");
                    return;
                }

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

                plugin.getLogger().info("BluCooldown is running the latest version.");

            } catch (Exception exception) {

                plugin.getLogger().warning("Could not check for updates.");

            } finally {

                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }

    private String extractTag(String json) {

        String tag = "\"tag_name\"";

        int index = json.indexOf(tag);

        if (index == -1) {
            return null;
        }

        int start = json.indexOf("\"", index + tag.length() + 1);

        if (start == -1) {
            return null;
        }

        int end = json.indexOf("\"", start + 1);

        if (end == -1) {
            return null;
        }

        String version = json.substring(start + 1, end);

        if (version.startsWith("v") || version.startsWith("V")) {
            version = version.substring(1);
        }

        return version;
    }
}
