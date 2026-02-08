package com.lynx.dynamicacplus;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
   private final DynamicACPlus plugin;
   private FileConfiguration config;

   public ConfigManager(DynamicACPlus plugin) {
      this.plugin = plugin;
      this.loadConfig();
   }

   public FileConfiguration getConfig() {
      return this.config;
   }

   private void loadConfig() {
      this.plugin.saveDefaultConfig();
      this.config = this.plugin.getConfig();
   }

   public boolean isLoginSecurityEnabled() {
      return this.config.getBoolean("general.login-security.enabled", true);
   }

   public boolean isAntiXrayEnabled() {
      return this.config.getBoolean("general.anti-xray.enabled", true);
   }

   public boolean isCombatLogEnabled() {
      return this.config.getBoolean("combat-log.enabled", true);
   }

   public boolean isAltDetectionEnabled() {
      return this.config.getBoolean("alt-detection.enabled", true);
   }

   public boolean isKickFlaggedOnJoin() {
      return this.config.getBoolean("general.kick-flagged-on-join.enabled", false);
   }

   public int getCombatLogCooldown() {
      return this.config.getInt("combat-log.cooldown", 10);
   }

   public String getWarnMessage() {
      return this.config.getString("messages.warn", "&cYou have been warned for cheating!");
   }

   public String getKickMessage() {
      return this.config.getString("messages.kick", "&cYou have been kicked for cheating!");
   }

   public String getBanMessage() {
      return this.config.getString("messages.ban", "&cYou have been banned for cheating!");
   }

   public String getAlertMessage() {
      return this.config.getString("messages.alert", "&ePlayer %player% has been flagged for cheating!");
   }

   public String getOpNotifyMessage() {
      return this.config.getString("messages.op-notify", "&c[AC] Player %player% flagged: %reason%");
   }

   public String getAltDetectedMessage() {
      return this.config.getString("messages.operator.alt-alert", "&c[AC] Alt detected: %player% (%ip%) has %accounts% accounts");
   }

   public void reload() {
      this.plugin.reloadConfig();
      this.config = this.plugin.getConfig();
   }
}
