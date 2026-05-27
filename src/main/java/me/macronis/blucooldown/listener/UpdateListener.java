package me.macronis.blucooldown.listener;

import me.macronis.blucooldown.BluCooldown;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateListener implements Listener {

    private final BluCooldown plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public UpdateListener(BluCooldown plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if (!plugin.isUpdateAvailable()) {
            return;
        }

        Player player = event.getPlayer();

        if (!player.isOp()) {
            return;
        }

        player.sendMessage(miniMessage.deserialize(""));
        player.sendMessage(miniMessage.deserialize(
                "<gradient:#5865F2:#8ea1ff><bold>BluCooldown Update Available</bold></gradient>"
        ));
        player.sendMessage(miniMessage.deserialize(
                "<gray>Current Version: <white>" + plugin.getDescription().getVersion()
        ));
        player.sendMessage(miniMessage.deserialize(
                "<gray>Latest Version: <white>" + plugin.getLatestVersion()
        ));
        player.sendMessage(miniMessage.deserialize(
                "<yellow>Download the latest update from GitHub.</yellow>"
        ));
        player.sendMessage(miniMessage.deserialize(""));
    }
}