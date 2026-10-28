package me.macronis.blucooldown.listener;

import me.macronis.blucooldown.BluCooldown;
import me.macronis.blucooldown.manager.CooldownManager;
import me.macronis.blucooldown.util.TimeUtil;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.projectiles.ProjectileSource;

public class ItemUseListener implements Listener {

    private static final String BYPASS_PERMISSION = "blucooldown.bypass";
    private static final String VIP_TIER_PERMISSION = "blucooldown.tier.vip";
    private static final double DEFAULT_VIP_MULTIPLIER = 0.5D;

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

        handleCooldown(player, item.getType().name(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPearl(PlayerTeleportEvent event) {

        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) {
            return;
        }

        handleCooldown(
                event.getPlayer(),
                Material.ENDER_PEARL.name(),
                event
        );
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileLaunch(ProjectileLaunchEvent event) {

        if (event.getEntity().getType() != org.bukkit.entity.EntityType.TRIDENT) {
            return;
        }

        ProjectileSource shooter = event.getEntity().getShooter();

        if (!(shooter instanceof Player player)) {
            return;
        }

        handleCooldown(player, Material.TRIDENT.name(), event);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {

        Action action = event.getAction();

        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null) {
            return;
        }

        Material material = item.getType();

        if (material == Material.AIR) {
            return;
        }

        if (material == Material.TRIDENT && item.containsEnchantment(Enchantment.RIPTIDE)) {
            handleCooldown(player, "TRIDENT_RIPTIDE", event);
            return;
        }

        if (!isRightClickable(material)) {
            return;
        }

        handleCooldown(player, material.name(), event);
    }

    private boolean isRightClickable(Material material) {

        if (material.isEdible()) {
            return true;
        }

        if (material == Material.BOW
                || material == Material.CROSSBOW
                || material == Material.TRIDENT
                || material == Material.ENDER_PEARL
                || material == Material.FIREWORK_ROCKET
                || material == Material.WIND_CHARGE
                || material == Material.SNOWBALL
                || material == Material.EGG
                || material == Material.ENDER_EYE
                || material == Material.SPLASH_POTION
                || material == Material.LINGERING_POTION) {
            return true;
        }

        String name = material.name();

        return name.endsWith("_SPAWN_EGG")
                || name.equals("WRITABLE_BOOK")
                || name.equals("WRITTEN_BOOK")
                || name.equals("GOAT_HORN")
                || name.equals("BRUSH")
                || name.equals("BUCKET")
                || name.equals("WATER_BUCKET")
                || name.equals("LAVA_BUCKET")
                || name.equals("MILK_BUCKET")
                || name.equals("POWDER_SNOW_BUCKET")
                || name.equals("AXOLOTL_BUCKET")
                || name.equals("COD_BUCKET")
                || name.equals("PUFFERFISH_BUCKET")
                || name.equals("SALMON_BUCKET")
                || name.equals("TADPOLE_BUCKET")
                || name.equals("TROPICAL_FISH_BUCKET");
    }

    private void handleCooldown(Player player, String key, org.bukkit.event.Cancellable event) {

        if (player.hasPermission(BYPASS_PERMISSION)) {
            return;
        }

        int cooldown = cooldownManager.getConfiguredCooldown(key);

        if (cooldown <= 0) {
            return;
        }

        if (player.hasPermission(VIP_TIER_PERMISSION)) {
            cooldown = getVipCooldown(cooldown);
        }

        if (cooldownManager.hasCooldown(player.getUniqueId(), key)) {

            long remaining = cooldownManager.getRemaining(
                    player.getUniqueId(),
                    key
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
                key,
                cooldown
        );

        Material material = materialFromName(key);

        if (material != null) {
            player.setCooldown(material, cooldown * 20);
        }
    }

    private int getVipCooldown(int cooldown) {
        double multiplier = plugin.getConfig()
                .getDouble("cooldown-tiers.vip-multiplier", DEFAULT_VIP_MULTIPLIER);

        if (!Double.isFinite(multiplier) || multiplier <= 0.0D || multiplier > 1.0D) {
            multiplier = DEFAULT_VIP_MULTIPLIER;
        }

        return Math.max(1, (int) Math.ceil(cooldown * multiplier));
    }

    private Material materialFromName(String name) {

        try {
            return Material.valueOf(name);
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }
}
