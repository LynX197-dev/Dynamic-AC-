package com.lynx.dynamicacplus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class LoginListener implements Listener {
   private final DynamicACPlus plugin;
   private final Set<Player> loggedInPlayers = new HashSet();
   private final Map<Player, Location> originalLocations = new HashMap();

   public LoginListener(DynamicACPlus plugin) {
      this.plugin = plugin;
   }

   @EventHandler
   public void onPlayerJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      this.loggedInPlayers.remove(player);
      Location safeLoc = this.findSafeLocation(player.getWorld());
      if (safeLoc != null) {
         this.originalLocations.put(player, player.getLocation());
         player.teleport(safeLoc);
      }

      player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 1));

      if (this.plugin.getPasswordManager().isRegistered(player.getUniqueId())) {
         player.sendMessage("§cPlease login with /login <password>");
      } else {
         player.sendMessage("§cPlease register with /register <password> <confirm>");
      }

   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      this.loggedInPlayers.remove(player);
      this.originalLocations.remove(player);
   }

   @EventHandler
   public void onPlayerMove(PlayerMoveEvent event) {
      Player player = event.getPlayer();
      if (!this.loggedInPlayers.contains(player)) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onEntityDamage(EntityDamageEvent event) {
      if (event.getEntity() instanceof Player) {
         Player player = (Player) event.getEntity();
         if (!this.loggedInPlayers.contains(player)) {
            event.setCancelled(true);
         }
      }

   }

   @EventHandler
   public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
      Player player = event.getPlayer();
      String cmd = event.getMessage().toLowerCase();
      if (!this.loggedInPlayers.contains(player) && !cmd.startsWith("/register ") && !cmd.startsWith("/login ")) {
         event.setCancelled(true);
         player.sendMessage("§cYou must login first!");
      }

   }

   @EventHandler
   public void onPlayerInteract(PlayerInteractEvent event) {
      Player player = event.getPlayer();
      if (!this.loggedInPlayers.contains(player)) {
         event.setCancelled(true);
      }

   }

   @EventHandler
   public void onInventoryClick(InventoryClickEvent event) {
      if (event.getWhoClicked() instanceof Player) {
         Player player = (Player) event.getWhoClicked();
         if (!this.loggedInPlayers.contains(player)) {
            event.setCancelled(true);
            player.closeInventory();
         }
      }
   }

   @EventHandler
   public void onInventoryOpen(InventoryOpenEvent event) {
      if (event.getPlayer() instanceof Player) {
         Player player = (Player) event.getPlayer();
         if (!this.loggedInPlayers.contains(player)) {
            event.setCancelled(true);
         }
      }
   }

   public void loginPlayer(Player player) {
      this.loggedInPlayers.add(player);
      player.removePotionEffect(PotionEffectType.BLINDNESS);
      Location original = (Location) this.originalLocations.get(player);
      if (original != null) {
         player.teleport(original);
         this.originalLocations.remove(player);
      }

      player.sendMessage("§aSuccessfully logged in!");
   }

   private Location findSafeLocation(World world) {
      int x = 0;
      int z = 0;
      int y = world.getHighestBlockYAt(x, z);
      Block block = world.getBlockAt(x, y, z);
      return block.getType().isSolid() ? new Location(world, (double) x, (double) (y + 1), (double) z)
            : new Location(world, (double) x, 64.0D, (double) z);
   }

   public boolean isLoggedIn(Player player) {
      return this.loggedInPlayers.contains(player);
   }
}
