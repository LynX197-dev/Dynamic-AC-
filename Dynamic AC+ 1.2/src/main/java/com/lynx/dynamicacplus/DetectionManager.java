package com.lynx.dynamicacplus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class DetectionManager {
   private final DynamicACPlus plugin;
   private final ConfigManager configManager;
   private final EvidenceManager evidenceManager;
   private final Map<UUID, Long> lastFlagTime = new HashMap();
   private final Map<String, Set<UUID>> ipToPlayers = new HashMap();

   public DetectionManager(DynamicACPlus plugin, ConfigManager configManager, EvidenceManager evidenceManager) {
      this.plugin = plugin;
      this.configManager = configManager;
      this.evidenceManager = evidenceManager;
      this.loadExistingAlts();
   }

   private boolean shouldCheck(Player player) {
      return player.getGameMode() == GameMode.SURVIVAL;
   }

   private void loadExistingAlts() {
      if (this.evidenceManager.getEvidenceConfig().getConfigurationSection("alt-accounts") != null) {
         Iterator var1 = this.evidenceManager.getEvidenceConfig().getConfigurationSection("alt-accounts").getKeys(false).iterator();

         while(var1.hasNext()) {
            String ip = (String)var1.next();
            String originalStr = this.evidenceManager.getEvidenceConfig().getString("alt-accounts." + ip + ".original");
            List<String> altsStr = this.evidenceManager.getEvidenceConfig().getStringList("alt-accounts." + ip + ".alts");
            Set<UUID> players = new HashSet();
            if (originalStr != null) {
               players.add(UUID.fromString(originalStr));
            }

            Iterator var6 = altsStr.iterator();

            while(var6.hasNext()) {
               String altStr = (String)var6.next();
               players.add(UUID.fromString(altStr));
            }

            this.ipToPlayers.put(ip, players);
         }
      }

   }

   public void flagPlayer(Player player, String reason) {
      long now = System.currentTimeMillis();
      UUID uuid = player.getUniqueId();
      if (!this.lastFlagTime.containsKey(uuid) || now - (Long)this.lastFlagTime.get(uuid) >= 5000L) {
         this.lastFlagTime.put(uuid, now);
         String location = player.getLocation().toString();
         this.evidenceManager.addEvidence(uuid, reason, location, now);
         this.punishPlayer(player, reason);
         this.plugin.getServer().broadcastMessage(ChatColor.translateAlternateColorCodes('&', this.configManager.getAlertMessage().replace("%player%", player.getName())));
      }
   }

   private void punishPlayer(Player player, String reason) {
      player.sendMessage(ChatColor.translateAlternateColorCodes('&', this.configManager.getWarnMessage()));
      player.kickPlayer(ChatColor.translateAlternateColorCodes('&', this.configManager.getKickMessage()));
   }

   public void checkForXray(Player player) {
      if (this.configManager.isAntiXrayEnabled()) {
      }

   }

   public void checkForSpeed(Player player) {
      if (this.shouldCheck(player)) {
         if (this.configManager.getConfig().getBoolean("general.movement-detection.enabled", true)) {
            double speed = player.getVelocity().length();
            double maxSpeed = this.configManager.getConfig().getDouble("general.movement-detection.max-speed-multiplier", 1.2D) * 0.2D;
            if (speed > maxSpeed) {
               this.flagPlayer(player, "Speed hack detected");
            }

         }
      }
   }

   public void checkForFly(Player player) {
      if (this.shouldCheck(player)) {
         if (this.configManager.getConfig().getBoolean("general.movement-detection.enabled", true)) {
            if (!player.isOnGround() && player.getVelocity().getY() > -0.08D && !player.isFlying() && player.getLocation().getBlock().getType().isAir()) {
               this.flagPlayer(player, "Fly hack detected");
            }

         }
      }
   }

   public void checkForAlt(Player player) {
      String ip = player.getAddress().getAddress().getHostAddress();
      UUID uuid = player.getUniqueId();
      Set<UUID> playersFromIP = (Set)this.ipToPlayers.computeIfAbsent(ip, (k) -> {
         return new HashSet();
      });
      boolean isNew = playersFromIP.add(uuid);
      if (playersFromIP.size() > 1 && isNew) {
         this.evidenceManager.addAltAccount(ip, new ArrayList(playersFromIP));
         if (this.configManager.isAltDetectionEnabled()) {
            int count = playersFromIP.size();
            String message = this.configManager.getAltDetectedMessage().replace("%player%", player.getName()).replace("%ip%", ip).replace("%accounts%", String.valueOf(count));
            this.plugin.getServer().getOnlinePlayers().stream().filter((p) -> {
               return p.hasPermission("dynamicac.player.notify");
            }).forEach((p) -> {
               p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
            });
         }
      }

   }
}
