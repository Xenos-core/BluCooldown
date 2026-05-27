package me.macronis.blucooldown.listener;

import me.macronis.blucooldown.BluCooldown;
import me.macronis.blucooldown.manager.CooldownManager;
import me.macronis.blucooldown.util.TimeUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class ItemUseListener implements Listener {

    private final BluCooldown plugin;
    private final CooldownManager cooldownManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public ItemUseListener(BluCooldown plugin) {
        this.plugin = plugin;
        this.cooldownManager = plugin.getCooldownManager();
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onConsume(PlayerItemConsumeEvent event) {

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        Material material = item.getType();

        handleCooldown(player, material, event);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPearl(PlayerTeleportEvent event) {

        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            return;
        }

        handleCooldown(
                event.getPlayer(),
                Material.ENDER_PEARL,
                event
        );
    }

    private void handleCooldown(Player player, Material material, org.bukkit.event.Cancellable event) {

        int cooldown = cooldownManager.getConfiguredCooldown(material);

        if (cooldown <= 0) {
            return;
        }

        if (cooldownManager.hasCooldown(player.getUniqueId(), material)) {

            long remaining = cooldownManager.getRemaining(
                    player.getUniqueId(),
                    material
            );

            String message = plugin.getConfig()
                    .getString("messages.cooldown.actionbar", "")
                    .replace("%time%", TimeUtil.format(remaining));

            player.sendActionBar(
                    miniMessage.deserialize(message)
            );

            event.setCancelled(true);
            return;
        }

        cooldownManager.setCooldown(
                player.getUniqueId(),
                material,
                cooldown
        );

        player.setCooldown(
                material,
                cooldown * 20
        );
    }
}