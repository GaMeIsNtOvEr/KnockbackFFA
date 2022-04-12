package me.gameisntover.kbffa.bots;

import lombok.Getter;
import lombok.Setter;
import me.gameisntover.kbffa.KnockbackFFA;
import me.gameisntover.kbffa.api.Knocker;
import me.gameisntover.kbffa.util.Message;
import org.bukkit.*;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Mob;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public abstract class Bot implements Listener {
    private String name;
    Mob mob;
    private boolean inArena;

    public abstract Mob getMob(Location location);

    public abstract ItemStack getItemInHand();


    public Bot(String name, Location location) {
        assert location.getWorld() != null;
        this.name = name;
        mob = getMob(location);
        ItemStack head = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta skullMeta = (SkullMeta) head.getItemMeta();
        skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(name));
        head.setItemMeta(skullMeta);
        ItemStack leatherChest = new ItemStack(Material.LEATHER_CHESTPLATE);
        LeatherArmorMeta leatherChestMeta = (LeatherArmorMeta) leatherChest.getItemMeta();
        leatherChestMeta.setColor(Color.RED);
        leatherChest.setItemMeta(leatherChestMeta);
        ItemStack leatherLeg = new ItemStack(Material.LEATHER_LEGGINGS);
        LeatherArmorMeta leatherLegMeta = (LeatherArmorMeta) leatherLeg.getItemMeta();
        leatherLegMeta.setColor(Color.RED);
        leatherLeg.setItemMeta(leatherLegMeta);
        ItemStack leatherBoot = new ItemStack(Material.LEATHER_BOOTS);
        LeatherArmorMeta leatherBootMeta = (LeatherArmorMeta) leatherBoot.getItemMeta();
        leatherBootMeta.setColor(Color.RED);
        leatherBoot.setItemMeta(leatherBootMeta);
        //applying equipments
        mob.getEquipment().setHelmet(head);
        mob.getEquipment().setChestplate(leatherChest);
        mob.getEquipment().setLeggings(leatherLeg);
        mob.getEquipment().setBoots(leatherBoot);
        mob.getEquipment().setItemInMainHand(getItemInHand());
        // --------------------------------------
        mob.setRemoveWhenFarAway(false);
        mob.setCanPickupItems(true);
        mob.addPotionEffect(PotionEffectType.SPEED.createEffect(999999, 10));
        mob.setTarget(null);
        mob.setCustomName(ChatColor.translateAlternateColorCodes('&', name));
        KnockbackFFA.getINSTANCE().getServer().getPluginManager().registerEvents(this, KnockbackFFA.getINSTANCE());
        mob.setMetadata("bot", new FixedMetadataValue(KnockbackFFA.getINSTANCE(), "bot-" + mob.getUniqueId()));
        mob.setSilent(true);
        start();
        new BukkitRunnable() {
            @Override
            public void run() {
                update();
                setInArena(KnockbackFFA.getINSTANCE().getArenaManager().getEnabledArena().contains(mob.getLocation()));
            }
        }.runTaskTimer(KnockbackFFA.getINSTANCE(), 20, 20);
    }

    public void remove() {
        HandlerList.unregisterAll(this);
        mob.teleport(new Location(mob.getWorld(), 0, -20, 0));
        mob.setHealth(0);
    }

    public void attackPlayer(Knocker knocker) {
        Location location = knocker.getPlayer().getLocation();
        mob.setTarget(knocker.getPlayer());
        mob.setVelocity(location.getDirection());
    }

    public void jump() {
        mob.setVelocity(mob.getLocation().getDirection().setY(0.5));
    }

    public void setName(String name) {
        this.name = name;
        mob.setCustomName(name);
    }

    public void chat(String message) {
        Bukkit.broadcastMessage(Message.CHATFORMAT.toString().replace("%player%", getName()).replace("%message%", message));
    }

    @EventHandler
    public void onDamageEvent(EntityDamageEvent e) {
        if (e.getEntity() != mob || e.getCause() == EntityDamageEvent.DamageCause.VOID) return;
        if (!isInArena()) e.setCancelled(true);
        else e.setDamage(0);
    }

    @EventHandler
    public void onMobDamageEvent(EntityDamageByEntityEvent e) {
        //Deflection method
        if (e.getEntity() != mob) return;
        if (!isInArena()) return;
        if (Arrays.asList(EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK, EntityDamageEvent.DamageCause.ENTITY_ATTACK).contains(e.getCause())) {
            Random random = new Random();
            if (random.nextInt(4) == 2) mob.launchProjectile(EnderPearl.class, mob.getVelocity());
            //Don't ask why i used integer here instead of nextBoolean since it would be really unfair for players...
        }
    }

    @EventHandler
    public void onDeathEvent(EntityDeathEvent e) {
        if (e.getEntity() != mob) return;
        List<String> stringList = KnockbackFFA.getINSTANCE().getBotManager().getConfig.getStringList("bot-death-messages");
        chat(stringList.get(new Random().nextInt(stringList.size() - 1)));
        e.getEntity().setHealth(20);
        mob.teleport(KnockbackFFA.getINSTANCE().getArenaManager().getEnabledArena().getSpawnLocation());
        //KNOWN ISSUE : Bot has loot drop so it drops some stuffs when he teleports back to lobby.
    }

    @EventHandler
    public void onTargetEvent(EntityTargetEvent e) {
        if (e.getEntity().equals(mob)) e.setCancelled(!isInArena());
    }

    @EventHandler
    public void onTargetEntityEvent(EntityTargetLivingEntityEvent e) {
        if (e.getEntity().equals(mob)) e.setCancelled(!isInArena());
    }

    public abstract void start();

    public abstract void update();
}
