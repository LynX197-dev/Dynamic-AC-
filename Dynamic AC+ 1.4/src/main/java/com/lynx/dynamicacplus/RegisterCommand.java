package com.lynx.dynamicacplus;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor {
   private final DynamicACPlus plugin;

   public RegisterCommand(DynamicACPlus plugin) {
      this.plugin = plugin;
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "Only players can use this command."));
         return true;
      } else {
         Player player = (Player)sender;
         if (args.length != 2) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /register <password> <confirm>"));
            return true;
         } else {
            String password = args[0];
            String confirm = args[1];
            if (!password.equals(confirm)) {
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPasswords do not match."));
               return true;
            } else if (this.plugin.getPasswordManager().isRegistered(player.getUniqueId())) {
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are already registered. Use /login <password>"));
               return true;
            } else {
               this.plugin.getPasswordManager().registerPlayer(player.getUniqueId(), password);
               this.plugin.getLoginListener().loginPlayer(player);
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccessfully registered and logged in!"));
               return true;
            }
         }
      }
   }
}
