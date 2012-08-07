package net.techguard.izone.listeners;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import net.techguard.izone.Variables;
import net.techguard.izone.managers.PvPManager;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Zone;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Painting;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

public class eListener
  implements Listener
{
  private HashMap<String, ItemStack[][]> safeDeath = new HashMap<String, ItemStack[][]>();

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onCreatureSpawn(CreatureSpawnEvent ev)
  {
    Entity entity = ev.getEntity();
    Zone zone = ZoneManager.getZone(ev.getLocation());

    if (zone != null)
      if ((entity instanceof Monster)) {
        if (zone.hasFlag(Flags.MONSTER)) {
          ev.setCancelled(true);
        }
      }
      else if (((entity instanceof Animals)) && 
        (zone.hasFlag(Flags.ANIMAL)))
        ev.setCancelled(true);
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onEntityExplode(EntityExplodeEvent ev)
  {
    Entity entity = ev.getEntity();
    List<Block> blocks = new ArrayList<Block>();

    for (Block b : ev.blockList()) {
      Zone zone = ZoneManager.getZone(b.getLocation());
      if ((zone == null) || (
        (!zone.hasFlag(Flags.EXPLOSION)) && 
        ((!(entity instanceof Creeper)) || (!zone.hasFlag(Flags.CREEPER))) && (
        (!(entity instanceof TNTPrimed)) || (!zone.hasFlag(Flags.TNT))))) continue;
      blocks.add(b);
    }
    Block b;
    for (Iterator<Block> it = blocks.iterator(); it.hasNext(); ev.blockList().remove(b)) b = it.next();
  }

  @EventHandler(priority=EventPriority.NORMAL)
  public void onEntityChangeBlock(EntityChangeBlockEvent ev)
  {
    if ((ev.isCancelled()) || (ev.getEntityType() != EntityType.ENDERMAN)) return;
    Block b = ev.getBlock();
    Zone zone = ZoneManager.getZone(b.getLocation());

    if ((zone != null) && (zone.hasFlag(Flags.ENDERMAN)))
      ev.setCancelled(true);
  }

  @EventHandler(priority=EventPriority.NORMAL)
  public void onPlayerDeath(PlayerDeathEvent ev)
  {
    Player p = ev.getEntity();
    Zone zone = ZoneManager.getZone(p.getLocation());

    if (zone != null) {
      if ((zone.hasFlag(Flags.DEATHDROP)) || (zone.hasFlag(Flags.SAFEDEATH))) {
        ev.getDrops().clear();
      }
      if (zone.hasFlag(Flags.SAFEDEATH)) {
        this.safeDeath.put(p.getName(), new ItemStack[][] { p.getInventory().getArmorContents(), p.getInventory().getContents() });
        ev.setDroppedExp(0);
        ev.setKeepLevel(true);
      }
    }
  }

  @EventHandler(priority=EventPriority.NORMAL)
  public void onPlayerRespawn(PlayerRespawnEvent ev)
  {
    Player p = ev.getPlayer();

    if (this.safeDeath.containsKey(p.getName())) {
      ItemStack[][] drops = (ItemStack[][])this.safeDeath.remove(p.getName());
      p.getInventory().setArmorContents(drops[0]);
      p.getInventory().setContents(drops[1]);
    }
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onEntityDamage(EntityDamageEvent ev)
  {
    Entity defender = ev.getEntity();
    Zone zone = ZoneManager.getZone(defender.getLocation());

    if ((ev instanceof EntityDamageByEntityEvent)) {
      Entity damager = ((EntityDamageByEntityEvent)ev).getDamager();
      if ((defender instanceof Player)) {
        if ((damager instanceof Arrow)) {
          damager = ((Arrow)damager).getShooter();
          if (((damager instanceof Player)) && 
            (PvPManager.onPlayerAttack((Player)defender, (Player)damager))) {
            ev.setCancelled(true);
          }
        }

        if (((damager instanceof Player)) && 
          (PvPManager.onPlayerAttack((Player)defender, (Player)damager))) {
          ev.setCancelled(true);
        }
      }
    }

    if (zone != null) {
      if ((ev instanceof EntityDamageByEntityEvent)) {
        Entity damager = ((EntityDamageByEntityEvent)ev).getDamager();
        if (((defender instanceof Player)) && ((damager instanceof Monster)) && 
          (zone.hasFlag(Flags.MONSTER))) {
          ev.setCancelled(true);
        }
      }

      if (((defender instanceof Player)) && (zone.hasFlag(Flags.GOD)))
        ev.setCancelled(true);
    }
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onPaintingBreak(PaintingBreakEvent ev)
  {
    Painting painting = ev.getPainting();
    Zone zone = ZoneManager.getZone(painting.getLocation());

    if ((zone != null) && (ev.getCause() == PaintingBreakEvent.RemoveCause.ENTITY) && 
      (((PaintingBreakByEntityEvent)ev).getRemover().getType() == EntityType.PLAYER)) {
      Player p = (Player)((PaintingBreakByEntityEvent)ev).getRemover();
      if (!ZoneManager.checkPermission(zone, p, Flags.PROTECTION)) {
        ev.setCancelled(true);
        p.sendMessage(Variables.ZONE_PROTECTED);
      }
    }
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onPaintingPlace(PaintingPlaceEvent ev)
  {
    Player p = ev.getPlayer();
    Painting painting = ev.getPainting();
    Zone zone = ZoneManager.getZone(painting.getLocation());

    if ((zone != null) && (!ZoneManager.checkPermission(zone, p, Flags.PROTECTION))) {
      ev.setCancelled(true);
      p.sendMessage(Variables.ZONE_PROTECTED);
    }
  }
}