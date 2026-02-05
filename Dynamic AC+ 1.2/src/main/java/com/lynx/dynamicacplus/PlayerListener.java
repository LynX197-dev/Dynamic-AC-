package com.lynx.dynamicacplus;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerListener implements Listener {
   private final DynamicACPlus plugin;
   private final DetectionManager detectionManager;

   public PlayerListener(DynamicACPlus plugin, DetectionManager detectionManager) {
      this.plugin = plugin;
      this.detectionManager = detectionManager;
   }

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event) {
      this.detectionManager.checkForXray(event.getPlayer());
      this.detectionManager.checkForAlt(event.getPlayer());
      if (this.plugin.getConfigManager().isKickFlaggedOnJoin() && !this.plugin.getEvidenceManager().getPlayerHistory(event.getPlayer().getUniqueId()).isEmpty()) {
         event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', this.plugin.getConfigManager().getKickMessage()));
      }

   }

   @EventHandler
   public void onPlayerMove(PlayerMoveEvent event) {
      this.detectionManager.checkForSpeed(event.getPlayer());
      this.detectionManager.checkForFly(event.getPlayer());
   }

   @EventHandler
   public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
      String msg = event.getMessage();
      String[] parts;
      String[] args;
      if (msg.startsWith("/login")) {
         event.setCancelled(true);
         parts = msg.split(" ");
         args = new String[parts.length - 1];
         System.arraycopy(parts, 1, args, 0, args.length);
         this.plugin.getLoginCommand().onCommand(event.getPlayer(), (Command)null, "login", args);
      } else if (msg.startsWith("/register")) {
         event.setCancelled(true);
         parts = msg.split(" ");
         args = new String[parts.length - 1];
         System.arraycopy(parts, 1, args, 0, args.length);
         this.plugin.getRegisterCommand().onCommand(event.getPlayer(), (Command)null, "register", args);
      }

   }
}
