package net.techguard.izone.listeners;

import net.techguard.izone.Variables;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Zone;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockSpreadEvent;

public class bListener
  implements Listener
{
  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onBlockBreak(BlockBreakEvent ev)
  {
    Player p = ev.getPlayer();
    Block b = ev.getBlock();
    Zone zone = ZoneManager.getZone(b.getLocation());

    if ((zone != null) && 
      (!ZoneManager.checkPermission(zone, p, Flags.PROTECTION))) {
      ev.setCancelled(true);
      p.sendMessage(Variables.ZONE_PROTECTED);
    }
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onBlockPlace(BlockPlaceEvent ev)
  {
    Player p = ev.getPlayer();
    Block b = ev.getBlock();
    Zone zone = ZoneManager.getZone(b.getLocation());

    if ((zone != null) && 
      (!ZoneManager.checkPermission(zone, p, Flags.PROTECTION))) {
      ev.setCancelled(true);
      p.sendMessage(Variables.ZONE_PROTECTED);
    }
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onBlockPistonExtend(BlockPistonExtendEvent ev)
  {
    Zone zone = ZoneManager.getZone(ev.getBlock().getLocation());
    Zone zoneIn = null;

    for (Block b : ev.getBlocks()) {
      Zone f = ZoneManager.getZone(b.getLocation());
      if (f != null) {
        zoneIn = f; break;
      }
    }
    if ((zone != zoneIn) && 
      (zoneIn != null) && (zoneIn.hasFlag(Flags.PROTECTION)))
      ev.setCancelled(true);
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onBlockPistonRetract(BlockPistonRetractEvent ev)
  {
    Block b = ev.getBlock();
    Zone zone = ZoneManager.getZone(ev.getBlock().getLocation());
    Zone zoneIn = ZoneManager.getZone(ev.getRetractLocation().getBlock()
      .getRelative(ev.getDirection()).getLocation());

    if ((b.getType() == Material.PISTON_STICKY_BASE) && (zone != zoneIn) && 
      (zoneIn != null) && (zoneIn.hasFlag(Flags.PROTECTION)))
      ev.setCancelled(true);
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onBlockBurn(BlockBurnEvent ev)
  {
    Block b = ev.getBlock();
    Zone zone = ZoneManager.getZone(b.getLocation());

    if ((zone != null) && (zone.hasFlag(Flags.FIRE)))
      ev.setCancelled(true);
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onBlockIgnite(BlockIgniteEvent ev)
  {
    Block b = ev.getBlock();
    BlockIgniteEvent.IgniteCause cause = ev.getCause();
    Zone zone = ZoneManager.getZone(b.getLocation());

    if (zone != null)
      if (cause == BlockIgniteEvent.IgniteCause.FLINT_AND_STEEL) {
        Player p = ev.getPlayer();
        if (zone.hasFlag(Flags.PROTECTION)) {
          if (!ZoneManager.checkPermission(zone, p, Flags.PROTECTION))
            ev.setCancelled(true);
        }
        else if ((zone.hasFlag(Flags.FIRE)) && 
          (!ZoneManager.checkPermission(zone, p, Flags.FIRE))) {
          ev.setCancelled(true);
        }
        if (ev.isCancelled()) p.sendMessage(Variables.ZONE_PROTECTED);
      }
      else if (zone.hasFlag(Flags.FIRE)) { ev.setCancelled(true);
      }
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onBlockSpread(BlockSpreadEvent ev)
  {
    Block b = ev.getSource();
    Material newState = ev.getNewState().getType();
    Zone zone = ZoneManager.getZone(b.getLocation());

    if ((zone != null) && 
      (newState == Material.FIRE) && (zone.hasFlag(Flags.FIRE)))
      ev.setCancelled(true);
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onBlockFade(BlockFadeEvent ev)
  {
    Block before = ev.getBlock();
    BlockState after = ev.getNewState();
    Zone zone = ZoneManager.getZone(before.getLocation());

    if ((zone != null) && (zone.hasFlag(Flags.MELT)) && (
      ((before.getType() == Material.SNOW) && (after.getType() == Material.AIR)) || (
      (before.getType() == Material.ICE) && (after.getType() == Material.STATIONARY_WATER))))
      ev.setCancelled(true);
  }
}