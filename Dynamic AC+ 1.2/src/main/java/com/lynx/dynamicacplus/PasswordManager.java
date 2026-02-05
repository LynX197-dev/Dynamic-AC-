package com.lynx.dynamicacplus;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class PasswordManager {
   private final DynamicACPlus plugin;
   private FileConfiguration passwordConfig;
   private File passwordFile;

   public PasswordManager(DynamicACPlus plugin) {
      this.plugin = plugin;
      this.passwordFile = new File(plugin.getDataFolder(), "passwords.yml");
      this.passwordConfig = YamlConfiguration.loadConfiguration(this.passwordFile);
   }

   public boolean isRegistered(UUID uuid) {
      return this.passwordConfig.contains(uuid.toString());
   }

   public void registerPlayer(UUID uuid, String password) {
      String hashed = this.hashPassword(password);
      this.passwordConfig.set(uuid.toString(), hashed);
      this.saveConfig();
   }

   public boolean checkPassword(UUID uuid, String password) {
      String stored = this.passwordConfig.getString(uuid.toString());
      return stored != null && stored.equals(this.hashPassword(password));
   }

   private String hashPassword(String password) {
      try {
         MessageDigest md = MessageDigest.getInstance("SHA-256");
         byte[] hash = md.digest(password.getBytes());
         StringBuilder sb = new StringBuilder();
         byte[] var5 = hash;
         int var6 = hash.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            byte b = var5[var7];
            sb.append(String.format("%02x", b));
         }

         return sb.toString();
      } catch (NoSuchAlgorithmException var9) {
         var9.printStackTrace();
         return password;
      }
   }

   private void saveConfig() {
      try {
         this.passwordConfig.save(this.passwordFile);
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }
}
