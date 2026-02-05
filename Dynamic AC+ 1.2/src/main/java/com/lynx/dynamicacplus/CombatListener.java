package com.lynx.dynamicacplus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.ChatColor;

public class CombatListener implements Listener {
   private final DynamicACPlus plugin;
   private final DetectionManager detectionManager;
   private final Map<UUID, Long> lastDamageTime = new HashMap();
   private final Map<UUID, BossBar> combatBars = new HashMap();

   public CombatListener(DynamicACPlus plugin, DetectionManager detectionManager) {
      this.plugin = plugin;
      this.detectionManager = detectionManager;
   }

   @EventHandler
   public void onEntityDamage(EntityDamageByEntityEvent event) {
      if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
         Player victim = (Player)event.getEntity();
         Player attacker = (Player)event.getDamager();
         long now = System.currentTimeMillis();
         this.lastDamageTime.put(victim.getUniqueId(), now);
         this.lastDamageTime.put(attacker.getUniqueId(), now);
         this.showCombatBar(victim);
         this.showCombatBar(attacker);
      }
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      UUID uuid = player.getUniqueId();
      if (this.lastDamageTime.containsKey(uuid)) {
         long timeSinceDamage = System.currentTimeMillis() - (Long)this.lastDamageTime.get(uuid);
         if (timeSinceDamage < (long)(this.plugin.getConfigManager().getCombatLogCooldown() * 1000)) {
            this.detectionManager.flagPlayer(player, "Combat logging");
            player.setHealth(0.0D);
         }
      }

      if (this.combatBars.containsKey(uuid)) {
         ((BossBar)this.combatBars.get(uuid)).removeAll();
         this.combatBars.remove(uuid);
      }

   }

   @EventHandler
   public void onPlayerCommand(PlayerCommandPreprocessEvent event) {
      Player player = event.getPlayer();
      if (this.isInCombat(player)) {
         event.setCancelled(true);
         player.sendMessage(ChatColor.RED + "You cannot use commands while in combat.");
      }
   }

   private boolean isInCombat(Player player) {
      UUID uuid = player.getUniqueId();
      if (!this.lastDamageTime.containsKey(uuid)) {
         return false;
      }

      long timeSinceDamage = System.currentTimeMillis() - (Long)this.lastDamageTime.get(uuid);
      return timeSinceDamage < (long)(this.plugin.getConfigManager().getCombatLogCooldown() * 1000);
   }

   private void showCombatBar(Player player) {
      final UUID uuid = player.getUniqueId();
      if (this.combatBars.containsKey(uuid)) {
         ((BossBar)this.combatBars.get(uuid)).setProgress(1.0D);
      } else {
         BossBar bar = this.plugin.getServer().createBossBar("Combat Cooldown", BarColor.RED, BarStyle.SOLID, new BarFlag[0]);
         bar.addPlayer(player);
         this.combatBars.put(uuid, bar);
      }

      (new BukkitRunnable() {
         int timeLeft;

         {
            this.timeLeft = CombatListener.this.plugin.getConfigManager().getCombatLogCooldown();
         }

         public void run() {
            if (this.timeLeft <= 0) {
               if (CombatListener.this.combatBars.containsKey(uuid)) {
                  ((BossBar)CombatListener.this.combatBars.get(uuid)).removeAll();
                  CombatListener.this.combatBars.remove(uuid);
               }

               this.cancel();
            } else {
               if (CombatListener.this.combatBars.containsKey(uuid)) {
                  ((BossBar)CombatListener.this.combatBars.get(uuid)).setProgress((double)this.timeLeft / (double)CombatListener.this.plugin.getConfigManager().getCombatLogCooldown());
                  ((BossBar)CombatListener.this.combatBars.get(uuid)).setTitle("Combat Cooldown: " + this.timeLeft + "s");
               }

               --this.timeLeft;
            }
         }
      }).runTaskTimer(this.plugin, 0L, 20L);
   }
}
