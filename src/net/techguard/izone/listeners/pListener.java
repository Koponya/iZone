package net.techguard.izone.listeners;

import java.util.ArrayList;
import java.util.Iterator;
import net.techguard.izone.OpInfoThread;
import net.techguard.izone.Properties;
import net.techguard.izone.Variables;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.InvManager;
import net.techguard.izone.managers.UpdateManager;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Settings;
import net.techguard.izone.zones.Zone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class pListener
  implements Listener
{
  @EventHandler(priority=EventPriority.NORMAL)
  public void onPlayerJoin(PlayerJoinEvent ev)
  {
    Player p = ev.getPlayer();

    if ((p.isOp()) && (UpdateManager.isUpdated())) {
      Player pp = p;
      Bukkit.getScheduler().scheduleSyncDelayedTask(iZone.getInstance(), new OpInfoThread(pp), 20L);
    }
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onPlayerInteract(PlayerInteractEvent ev)
  {
    Player p = ev.getPlayer();
    Settings sett = Settings.getSett(p);
    ItemStack inHand = p.getItemInHand();
    Location clicked = ev.getClickedBlock().getLocation();

    if (ev.getAction() == Action.RIGHT_CLICK_BLOCK) {
      if ((p.hasPermission(Variables.PERMISSION_CHECK)) && (inHand.getTypeId() == Properties.checkTool)) {
        iZone.getInstance().sendInfo(p, clicked);
        ev.setCancelled(true);
      }
      else if ((p.hasPermission(Variables.PERMISSION_DEFINE)) && (inHand.getTypeId() == Properties.defineTool)) {
        sett.setBorder(2, clicked);
        p.sendMessage("§bSecond Position set(§f" + clicked.getBlockX() + "§b,§f" + clicked.getBlockY() + "§b,§f" + clicked.getBlockZ() + "§b)");
        ev.setCancelled(true);
      }
    }
    else if ((ev.getAction() == Action.LEFT_CLICK_BLOCK) && 
      (p.hasPermission(Variables.PERMISSION_DEFINE)) && (inHand.getTypeId() == Properties.defineTool)) {
      sett.setBorder(1, clicked);
      p.sendMessage("§bFirst Position set(§f" + clicked.getBlockX() + "§b,§f" + clicked.getBlockY() + "§b,§f" + clicked.getBlockZ() + "§b)");
      ev.setCancelled(true);
    }

    Zone zone = ZoneManager.getZone(clicked);
    if ((zone != null) && (!ZoneManager.checkPermission(zone, p, Flags.INTERACT))) {
      ev.setCancelled(true);
      p.sendMessage(Variables.ZONE_PROTECTED);
    }
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onPlayerTeleport(PlayerTeleportEvent ev)
  {
    Player p = ev.getPlayer();
    Location from = ev.getFrom();
    from = new Location(from.getWorld(), from.getBlockX(), from.getBlockY(), from.getBlockZ(), from.getYaw(), from.getPitch());
    Location to = ev.getTo();
    to = new Location(to.getWorld(), to.getBlockX(), to.getBlockY(), to.getBlockZ(), to.getYaw(), to.getPitch());

    Zone fzone = ZoneManager.getZone(from);
    Zone tzone = ZoneManager.getZone(to);

    if ((tzone != fzone) && (tzone != null) && (tzone.hasFlag(Flags.RESTRICTION)) && 
      (!ZoneManager.checkPermission(tzone, p, Flags.RESTRICTION))) {
      p.sendMessage(Variables.ZONE_RESTRICTION);
      ev.setCancelled(true);
      return;
    }

    if ((fzone != tzone) && (fzone != null) && (fzone.hasFlag(Flags.JAIL)) && 
      (!ZoneManager.checkPermission(fzone, p, Flags.JAIL))) {
      p.sendMessage(Variables.ZONE_PROTECTED);
      ev.setCancelled(true);
      return;
    }

    if ((fzone != tzone) && (fzone != null)) {
      if (fzone.hasFlag(Flags.FAREWELL)) {
        String s = fzone.getFarewell();
        sendWFM(p, s);
      }
      if ((fzone.hasFlag(Flags.GAMEMODE)) && 
        (p.getServer().getDefaultGameMode() != p.getGameMode())) {
        p.sendMessage(fzone.getName() + "§b changed your gamemode!");
        p.setGameMode(p.getServer().getDefaultGameMode());
      }

      if (fzone.hasFlag(Flags.GIVEITEM_OUT)) {
        ArrayList<ItemStack> inventory = fzone.getInventory(Flags.GIVEITEM_OUT);
        for (ItemStack item : inventory) {
          InvManager.addToInventory(p.getInventory(), item);
        }
      }
      if (fzone.hasFlag(Flags.TAKEITEM_OUT)) {
        ArrayList<ItemStack> inventory = fzone.getInventory(Flags.TAKEITEM_OUT);
        for (ItemStack item : inventory) {
          InvManager.removeFromInventory(p.getInventory(), item);
        }
      }
    }
    if ((tzone != fzone) && (tzone != null)) {
      if (tzone.hasFlag(Flags.WELCOME)) {
        String s = tzone.getWelcome();
        sendWFM(p, s);
      }
      if ((tzone.hasFlag(Flags.GAMEMODE)) && 
        (tzone.getGamemode() != p.getGameMode())) {
        p.sendMessage(tzone.getName() + "§b changed your gamemode!");
        p.setGameMode(tzone.getGamemode());
      }

      if (tzone.hasFlag(Flags.GIVEITEM_IN)) {
        ArrayList<ItemStack> inventory = tzone.getInventory(Flags.GIVEITEM_IN);
        for (ItemStack item : inventory) {
          InvManager.addToInventory(p.getInventory(), item);
        }
      }
      if (tzone.hasFlag(Flags.TAKEITEM_IN)) {
        ArrayList<ItemStack> inventory = tzone.getInventory(Flags.TAKEITEM_IN);
        for (ItemStack item : inventory)
          InvManager.removeFromInventory(p.getInventory(), item);
      }
    }
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onPlayerMove(PlayerMoveEvent ev)
  {
    Player p = ev.getPlayer();
    Location from = ev.getFrom();
    from = new Location(from.getWorld(), from.getBlockX(), from.getBlockY(), from.getBlockZ(), from.getYaw(), from.getPitch());
    Location to = ev.getTo();
    to = new Location(to.getWorld(), to.getBlockX(), to.getBlockY(), to.getBlockZ(), to.getYaw(), to.getPitch());

    if ((from.getBlockX() == to.getBlockX()) && (from.getBlockY() == to.getBlockY()) && (from.getBlockZ() == to.getBlockZ())) return;
    Zone fzone = ZoneManager.getZone(from);
    Zone tzone = ZoneManager.getZone(to);

    if ((tzone != fzone) && (tzone != null) && (tzone.hasFlag(Flags.RESTRICTION)) && 
      (!ZoneManager.checkPermission(tzone, p, Flags.RESTRICTION))) {
      p.sendMessage(Variables.ZONE_RESTRICTION);
      from.setX(from.getBlockX() + 0.5D);
      from.setY(from.getBlockY() + 0.0D);
      from.setZ(from.getBlockZ() + 0.5D);
      p.teleport(from);
      return;
    }

    if ((fzone != tzone) && (fzone != null) && (fzone.hasFlag(Flags.JAIL)) && 
      (!ZoneManager.checkPermission(fzone, p, Flags.JAIL))) {
      p.sendMessage(Variables.ZONE_PROTECTED);
      from.setX(from.getBlockX() + 0.5D);
      from.setY(from.getBlockY() + 0.0D);
      from.setZ(from.getBlockZ() + 0.5D);
      p.teleport(from);
      return;
    }
    if ((fzone != tzone) && (fzone != null)) {
      if (fzone.hasFlag(Flags.FAREWELL)) {
        String s = fzone.getFarewell();
        sendWFM(p, s);
      }
      if ((fzone.hasFlag(Flags.GAMEMODE)) && 
        (p.getServer().getDefaultGameMode() != p.getGameMode())) {
        p.sendMessage(fzone.getName() + "§b changed your gamemode!");
        p.setGameMode(p.getServer().getDefaultGameMode());
      }

      if (fzone.hasFlag(Flags.GIVEITEM_OUT)) {
        ArrayList<ItemStack> inventory = fzone.getInventory(Flags.GIVEITEM_OUT);
        for (ItemStack item : inventory) {
          InvManager.addToInventory(p.getInventory(), item);
        }
      }
      if (fzone.hasFlag(Flags.TAKEITEM_OUT)) {
        ArrayList<ItemStack> inventory = fzone.getInventory(Flags.TAKEITEM_OUT);
        for (ItemStack item : inventory) {
          InvManager.removeFromInventory(p.getInventory(), item);
        }
      }
      if (fzone.hasFlag(Flags.TEXTUREPACK)) {
    	  System.out.println("[iZone] Need spout server for the Texturepack flag!");
    	  //need spout
        /*CraftServer cserver = (CraftServer)iZone.getInstance().getServer();

        String texturepack = cserver.getHandle().getServer().getTexturePack();
        if (texturepack.length() <= 0) texturepack = "http://techguard.info/Bukkit/empty.zip";

        data = texturepack + "" + cserver.getHandle().getServer().R();
        iZone.getInstance().sendPlayerPacket(p, "MC|TPack", ((String)data).getBytes());*/
      }
    }
    if ((tzone != fzone) && (tzone != null)) {
      if (tzone.hasFlag(Flags.WELCOME)) {
        String s = tzone.getWelcome();
        sendWFM(p, s);
      }
      if (tzone.hasFlag(Flags.GIVEITEM_IN)) {
        ArrayList<ItemStack> inventory = tzone.getInventory(Flags.GIVEITEM_IN);
        for (Iterator<ItemStack> it = inventory.iterator(); it.hasNext(); ) { 
        	ItemStack item = it.next();
        	InvManager.addToInventory(p.getInventory(), item);
        }
      }
      if (tzone.hasFlag(Flags.TAKEITEM_IN)) {
        ArrayList<ItemStack> inventory = tzone.getInventory(Flags.TAKEITEM_IN);
        for (Iterator<ItemStack> it = inventory.iterator(); it.hasNext(); ) { 
        	ItemStack item = it.next();
        	InvManager.removeFromInventory(p.getInventory(), item);
        }
      }
      if (tzone.hasFlag(Flags.TEXTUREPACK)) {
    	  System.out.println("[iZone] Need spout server for the Texturepack flag!");
    	  //need spout
        /*CraftServer cserver = (CraftServer)iZone.getInstance().getServer();

        data = tzone.getTexturePack() + "" + cserver.getHandle().getServer().R();
        iZone.getInstance().sendPlayerPacket(p, "MC|TPack", data.getBytes());*/
      }
    }
    if ((tzone != null) && 
      (tzone.hasFlag(Flags.GAMEMODE)) && 
      (tzone.getGamemode() != p.getGameMode())) {
      p.sendMessage(tzone.getName() + "§b changed your gamemode!");
      p.setGameMode(tzone.getGamemode());
    }
  }

  private void sendWFM(Player p, String s)
  {
    String[] chars = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    s = s.replaceAll("§", "");
    for (String key : chars) {
      s = s.replaceAll("&" + key, "§" + key);
    }
    String start = "§e";
    if (s.startsWith("§")) start = s.substring(0, 2);
    p.sendMessage(start + "~ " + s);
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onPlayerDropItem(PlayerDropItemEvent ev)
  {
    Player p = ev.getPlayer();
    Item item = ev.getItemDrop();
    Zone zone = ZoneManager.getZone(item.getLocation());

    if ((zone != null) && (!ZoneManager.checkPermission(zone, p, Flags.DROP)))
      ev.setCancelled(true);
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onPlayerPickupItem(PlayerPickupItemEvent ev)
  {
    Player p = ev.getPlayer();
    Item item = ev.getItem();
    Zone zone = ZoneManager.getZone(item.getLocation());

    if ((zone != null) && (!ZoneManager.checkPermission(zone, p, Flags.DROP))) {
      ev.getItem().remove();
      ev.setCancelled(true);
    }
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onPlayerBucketEmpty(PlayerBucketEmptyEvent ev)
  {
    Player p = ev.getPlayer();
    Block b = ev.getBlockClicked().getRelative(ev.getBlockFace());
    Zone zone = ZoneManager.getZone(b.getLocation());

    if ((zone != null) && (!ZoneManager.checkPermission(zone, p, Flags.PROTECTION))) {
      ev.setCancelled(true);
      p.sendMessage(Variables.ZONE_PROTECTED);
    }
  }

  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onPlayerBucketFill(PlayerBucketFillEvent ev)
  {
    Player p = ev.getPlayer();
    Block b = ev.getBlockClicked().getRelative(ev.getBlockFace());
    Zone zone = ZoneManager.getZone(b.getLocation());

    if ((zone != null) && (!ZoneManager.checkPermission(zone, p, Flags.PROTECTION))) {
      ev.setCancelled(true);
      p.sendMessage(Variables.ZONE_PROTECTED);
    }
  }
}