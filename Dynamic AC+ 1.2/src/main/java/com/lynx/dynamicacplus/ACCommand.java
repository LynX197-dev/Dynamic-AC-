package com.lynx.dynamicacplus;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ACCommand implements CommandExecutor {
   private final DynamicACPlus plugin;
   private final EvidenceManager evidenceManager;

   public ACCommand(DynamicACPlus plugin, EvidenceManager evidenceManager) {
      this.plugin = plugin;
      this.evidenceManager = evidenceManager;
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (!sender.hasPermission("dynamicac.admin")) {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have permission!"));
         return true;
      } else if (args.length == 0) {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /ac <reload|history|check>"));
         return true;
      } else {
         String var5 = args[0].toLowerCase();
         byte var6 = -1;
         switch(var5.hashCode()) {
         case -934641255:
            if (var5.equals("reload")) {
               var6 = 0;
            }
            break;
         case 94627080:
            if (var5.equals("check")) {
               var6 = 2;
            }
            break;
         case 926934164:
            if (var5.equals("history")) {
               var6 = 1;
            }
         }

         switch(var6) {
         case 0:
            this.plugin.getConfigManager().reload();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aConfig reloaded!"));
            break;
         case 1:
            if (args.length < 2) {
               sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /ac history <player>"));
               return true;
            }

            Player target = this.plugin.getServer().getPlayer(args[1]);
            if (target == null) {
               sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlayer not found!"));
               return true;
            }

            List<String> history = this.evidenceManager.getPlayerHistory(target.getUniqueId());
            String var10002 = target.getName();
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eHistory for " + var10002 + ": " + String.valueOf(history)));
            break;
         case 2:
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eActive detections: None"));
            break;
         default:
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUnknown subcommand!"));
         }

         return true;
      }
   }
}
