package com.lynx.dynamicacplus;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReregisterCommand implements CommandExecutor {
    private final DynamicACPlus plugin;

    public ReregisterCommand(DynamicACPlus plugin) {
        this.plugin = plugin;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "Only players can use this command."));
            return true;
        } else {
            Player player = (Player) sender;
            if (!this.plugin.getLoginListener().isLoggedIn(player)) {
                player.sendMessage(
                        ChatColor.translateAlternateColorCodes('&', "&cYou must be logged in to use this command."));
                return true;
            } else if (args.length != 2) {
                player.sendMessage(
                        ChatColor.translateAlternateColorCodes('&', "&cUsage: /reregister <newPassword> <confirm>"));
                return true;
            } else {
                String password = args[0];
                String confirm = args[1];
                if (!password.equals(confirm)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPasswords do not match."));
                    return true;
                } else {
                    this.plugin.getPasswordManager().registerPlayer(player.getUniqueId(), password);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aPassword successfully reset!"));
                    return true;
                }
            }
        }
    }
}