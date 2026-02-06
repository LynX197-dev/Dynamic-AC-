package com.lynx.dynamicacplus;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class EvidenceManager {
   private final DynamicACPlus plugin;
   private File evidenceFile;
   private FileConfiguration evidenceConfig;

   public EvidenceManager(DynamicACPlus plugin) {
      this.plugin = plugin;
      this.loadEvidence();
   }

   private void loadEvidence() {
      this.evidenceFile = new File(this.plugin.getDataFolder().getParentFile(), "DynamicACPlus/evidence.yml");
      if (!this.evidenceFile.exists()) {
         try {
            this.evidenceFile.getParentFile().mkdirs();
            this.evidenceFile.createNewFile();
         } catch (IOException var2) {
            this.plugin.getLogger().severe("Could not create evidence.yml!");
         }
      }

      this.evidenceConfig = YamlConfiguration.loadConfiguration(this.evidenceFile);
   }

   public void addEvidence(UUID playerUUID, String reason, String location, long timestamp) {
      String var10000 = playerUUID.toString();
      String key = var10000 + "." + timestamp;
      this.evidenceConfig.set(key + ".reason", reason);
      this.evidenceConfig.set(key + ".location", location);
      this.evidenceConfig.set(key + ".timestamp", timestamp);
      this.saveEvidence();
   }

   public List<String> getPlayerHistory(UUID playerUUID) {
      return this.evidenceConfig.getStringList(playerUUID.toString() + ".history");
   }

   public void addAltAccount(String ip, List<UUID> players) {
      if (!players.isEmpty()) {
         this.evidenceConfig.set("alt-accounts." + ip + ".original", ((UUID)players.get(0)).toString());
         List<String> alts = new ArrayList();

         for(int i = 1; i < players.size(); ++i) {
            alts.add(((UUID)players.get(i)).toString());
         }

         this.evidenceConfig.set("alt-accounts." + ip + ".alts", alts);
         this.saveEvidence();
      }

   }

   public FileConfiguration getEvidenceConfig() {
      return this.evidenceConfig;
   }

   private void saveEvidence() {
      try {
         this.evidenceConfig.save(this.evidenceFile);
      } catch (IOException var2) {
         this.plugin.getLogger().severe("Could not save evidence.yml!");
      }

   }
}
