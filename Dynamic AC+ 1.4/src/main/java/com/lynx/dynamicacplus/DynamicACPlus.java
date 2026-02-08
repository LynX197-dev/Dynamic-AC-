package com.lynx.dynamicacplus;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DynamicACPlus extends JavaPlugin {
   private ConfigManager configManager;
   private EvidenceManager evidenceManager;
   private DetectionManager detectionManager;
   private PasswordManager passwordManager;
   private LoginListener loginListener;
   private LoginCommand loginCommand;
   private RegisterCommand registerCommand;

   public void onEnable() {
      this.configManager = new ConfigManager(this);
      this.evidenceManager = new EvidenceManager(this);
      this.detectionManager = new DetectionManager(this, this.configManager, this.evidenceManager);
      this.getCommand("ac").setExecutor(new ACCommand(this, this.evidenceManager));
      if (this.configManager.isLoginSecurityEnabled()) {
         this.passwordManager = new PasswordManager(this);
         this.loginListener = new LoginListener(this);
         this.loginCommand = new LoginCommand(this);
         this.registerCommand = new RegisterCommand(this);
         this.getCommand("login").setExecutor(this.loginCommand);
         this.getCommand("register").setExecutor(this.registerCommand);
         this.getServer().getPluginManager().registerEvents(this.loginListener, this);
      }
      getCommand("dac").setExecutor(
            new ReloadCommand(this, "dynamicac.admin.reload"));

      this.getServer().getPluginManager().registerEvents(new PlayerListener(this, this.detectionManager), this);
      this.getServer().getPluginManager().registerEvents(new CombatListener(this, this.detectionManager), this);
      this.getLogger().info("Dynamic AC+ by LynX enabled!");
      // ASCII
      Bukkit.getConsoleSender().sendMessage(AsciiBanner.DESIGN_CREDIT);
   }

   public void onDisable() {
      this.getLogger().info("Dynamic AC+ by LynX disabled!");
   }

   public ConfigManager getConfigManager() {
      return this.configManager;
   }

   public EvidenceManager getEvidenceManager() {
      return this.evidenceManager;
   }

   public PasswordManager getPasswordManager() {
      return this.passwordManager;
   }

   public LoginListener getLoginListener() {
      return this.loginListener;
   }

   public DetectionManager getDetectionManager() {
      return this.detectionManager;
   }

   public LoginCommand getLoginCommand() {
      return this.loginCommand;
   }

   public RegisterCommand getRegisterCommand() {
      return this.registerCommand;
   }
}
