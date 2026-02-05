package com.lynx.dynamicacplus;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginCommand implements CommandExecutor {
   private final DynamicACPlus plugin;

   public LoginCommand(DynamicACPlus plugin) {
      this.plugin = plugin;
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "Only players can use this command."));
         return true;
      } else {
         Player player = (Player)sender;
         if (args.length != 1) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /login <password>"));
            return true;
         } else {
            String password = args[0];
            if (!this.plugin.getPasswordManager().isRegistered(player.getUniqueId())) {
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou are not registered. Use /register <password> <confirm>"));
               return true;
            } else {
               if (this.plugin.getPasswordManager().checkPassword(player.getUniqueId(), password)) {
                  this.plugin.getLoginListener().loginPlayer(player);
               } else {
                  player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cIncorrect password."));
               }

               return true;
            }
         }
      }
   }
}
