package com.lynx.dynamicacplus;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final String permission;

    public ReloadCommand(JavaPlugin plugin, String permission) {
        this.plugin = plugin;
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length != 1 || !args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage("§cUsage: /" + label + " reload");
            return true;
        }

        if (!sender.hasPermission(permission)) {
            sender.sendMessage("§cNo permission.");
            return true;
        }

        long start = System.currentTimeMillis();

        plugin.reloadConfig();

        long end = System.currentTimeMillis();
        sender.sendMessage("§a" + plugin.getName() + " reloaded in §f" + (end - start) + "ms");
        return true;
    }
}