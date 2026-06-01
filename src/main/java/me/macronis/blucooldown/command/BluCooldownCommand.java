package me.macronis.blucooldown.command;

import me.macronis.blucooldown.BluCooldown;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BluCooldownCommand implements CommandExecutor, TabCompleter {

    private static final String RELOAD_PERMISSION = "blucooldown.reload";
    private static final String HAND_ITEM_PERMISSION = "blucooldown.handitem";

    private final BluCooldown plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public BluCooldownCommand(BluCooldown plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(miniMessage.deserialize("<gray>Usage: <white>/" + label + " reload <gray>| <white>/" + label + " handitem <seconds>"));
            return true;
        }

        if (args[0].equalsIgnoreCase("reload")) {
            return handleReload(sender);
        }

        if (args[0].equalsIgnoreCase("handitem")) {
            return handleHandItem(sender, args);
        }

        sender.sendMessage(miniMessage.deserialize("<gray>Usage: <white>/" + label + " reload <gray>| <white>/" + label + " handitem <seconds>"));
        return true;
    }

    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission(RELOAD_PERMISSION)) {
            sendMessage(sender, "messages.no-permission");
            return true;
        }

        plugin.reloadPluginConfig();
        sendMessage(sender, "messages.reload-success");
        return true;
    }

    private boolean handleHandItem(CommandSender sender, String[] args) {
        if (!sender.hasPermission(HAND_ITEM_PERMISSION)) {
            sendMessage(sender, "messages.no-permission");
            return true;
        }

        if (!(sender instanceof Player player)) {
            sendMessage(sender, "messages.player-only");
            return true;
        }

        if (args.length < 2) {
            sendMessage(sender, "messages.invalid-cooldown");
            return true;
        }

        int cooldown;

        try {
            cooldown = Integer.parseInt(args[1]);
        } catch (NumberFormatException exception) {
            sendMessage(sender, "messages.invalid-cooldown");
            return true;
        }

        if (cooldown <= 0) {
            sendMessage(sender, "messages.invalid-cooldown");
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        Material material = item.getType();

        if (material == Material.AIR) {
            sendMessage(sender, "messages.no-item-in-hand");
            return true;
        }

        String path = "items." + material.name();
        plugin.getConfig().set(path + ".enabled", true);
        plugin.getConfig().set(path + ".cooldown", cooldown);
        plugin.saveConfig();
        plugin.reloadPluginConfig();

        sendMessage(sender, "messages.cooldown-updated", material.name(), cooldown);
        return true;
    }

    private void sendMessage(CommandSender sender, String path) {
        sendMessage(sender, path, null, 0);
    }

    private void sendMessage(CommandSender sender, String path, @Nullable String material, int cooldown) {
        String message = plugin.getConfig().getString(path, "");

        if (message.isBlank()) {
            return;
        }

        if (material != null) {
            message = message
                    .replace("%material%", material)
                    .replace("%cooldown%", String.valueOf(cooldown));
        }

        sender.sendMessage(miniMessage.deserialize(message));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) {
            return Collections.emptyList();
        }

        List<String> completions = new ArrayList<>();

        if (sender.hasPermission(RELOAD_PERMISSION)) {
            completions.add("reload");
        }

        if (sender.hasPermission(HAND_ITEM_PERMISSION)) {
            completions.add("handitem");
        }

        String input = args[0].toLowerCase();
        completions.removeIf(completion -> !completion.startsWith(input));

        return completions;
    }
}
