package me.gameisntover.kbffa.knockbackffa.otherlisteners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.ArenaConfiguration;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.PlaySoundConfiguration;
import me.gameisntover.kbffa.knockbackffa.KnockbackFFA;
import me.gameisntover.kbffa.knockbackffa.CustomConfigs.MessageConfiguration;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class deathlistener implements Listener {
    Map<Entity, Integer> killStreak = new HashMap<>();
    Map<Entity, Entity> damagers = new HashMap<>();
    Map<Entity, Integer> deathCount = new HashMap<>();


    @EventHandler
    public void damaged(EntityDamageByEntityEvent e) {
        Entity player = e.getEntity();
        Entity damager = e.getDamager();
        if (damager instanceof Player && player instanceof Player) {

            damagers.put(player, damager);
        }
    }

    @EventHandler
    public void voidDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (e.getCause() == EntityDamageEvent.DamageCause.CONTACT.VOID) {
                p.setHealth(0.5);
                p.getInventory().clear();
            }
        }
    }
    @EventHandler
    public void playerDeathByVoid(PlayerDeathEvent e) {
        Player player = e.getEntity();
        Entity damager = damagers.get(player);
        killStreak.put(player, 0);
        double x = ArenaConfiguration.get().getDouble("arena1.x");
        double y = ArenaConfiguration.get().getDouble("arena1.y");;
        double z = ArenaConfiguration.get().getDouble("arena1.z");
        World world = Bukkit.getWorld(ArenaConfiguration.get().getString("arena1.world"));
        player.spigot().respawn();
        player.teleport(new org.bukkit.Location(world, x, y, z));

        if (damager == null) {
            player.sendMessage(ChatColor.AQUA + "You died by falling into the void");
        } else{
        String deathText = MessageConfiguration.get().getString("deathmessage").replace("&", "§").replace("%killer%", damager.getName());
        deathText = PlaceholderAPI.setPlaceholders(e.getEntity(), deathText);
        e.setDeathMessage(deathText);
        if (killStreak.get(damager) == null) {
                killStreak.put(damager, 1);
            } else {
            damagers.remove(player);
            killStreak.put(damager, killStreak.get(damager).intValue() + 1);
            if (killStreak.get(damager) == 5) {
                ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, 4);
                ItemMeta enderpearlmeta = enderpearl.getItemMeta();
                enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
                enderpearl.setItemMeta(enderpearlmeta);
                Player killer = (Player) damager;
                Inventory inv = killer.getInventory();
                Bukkit.broadcastMessage(KnockbackFFA.getInstance().getConfig().getString("killstreak.5message").replace("&", "§").replace("%killer%", killer.getDisplayName()));
                inv.addItem(enderpearl);
                killer.playSound(killer.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("5kills")), 1, 1);
            } else if (killStreak.get(damager) == 10) {

                ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, 8);
                ItemMeta enderpearlmeta = enderpearl.getItemMeta();
                enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
                enderpearl.setItemMeta(enderpearlmeta);
                Player killer = (Player) damager;
                Inventory inv = killer.getInventory();
                Bukkit.broadcastMessage(KnockbackFFA.getInstance().getConfig().getString("killstreak.10message").replace("&", "§").replace("%killer%", killer.getDisplayName()));
                inv.addItem(enderpearl);
                killer.playSound(killer.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("10kills")), 1, 1);
            } else if (killStreak.get(damager) == 15) {
                ItemStack enderpearl = new ItemStack(Material.ENDER_PEARL, 16);
                ItemMeta enderpearlmeta = enderpearl.getItemMeta();
                enderpearlmeta.setDisplayName(ChatColor.GREEN + "Ender Pearl");
                enderpearl.setItemMeta(enderpearlmeta);
                Player killer = (Player) damager;
                Inventory inv = killer.getInventory();
                Bukkit.broadcastMessage(KnockbackFFA.getInstance().getConfig().getString("killstreak.15message").replace("&", "§").replace("%killer%", killer.getDisplayName()));
                inv.addItem(enderpearl);
                killer.playSound(killer.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("15kills")), 1, 1);
            } else if (killStreak.get(damager) >= 15) {
                Player killer = (Player) damager;
                killer.playSound(killer.getLocation(), Sound.valueOf(PlaySoundConfiguration.get().getString("+15kills")), 1, 1);
                Bukkit.broadcastMessage(KnockbackFFA.getInstance().getConfig().getString("killstreak.lastmessage").replace("&", "§").replace("%killer%", killer.getDisplayName()).replace("%killstreak%", killStreak.get(damager).intValue() - 1 + "kills"));
            }
        }
            }
            }
        }
