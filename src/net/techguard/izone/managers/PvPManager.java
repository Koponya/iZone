package net.techguard.izone.managers;

import java.util.HashMap;
import net.techguard.izone.Variables;
import net.techguard.izone.iZone;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Zone;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class PvPManager
{
  private static HashMap<String, Boolean> worlds = new HashMap<String, Boolean>();

  public static boolean onPlayerAttack(Player defender, Player damager) {
    World w = defender.getWorld();
    Zone defend_zone = ZoneManager.getZone(defender.getLocation());
    Zone attack_zone = ZoneManager.getZone(damager.getLocation());

    if (canPvP(w)) {
      if (((defend_zone != null) && (defend_zone.hasFlag(Flags.PVP))) || ((attack_zone != null) && (attack_zone.hasFlag(Flags.PVP)))) {
        damager.sendMessage(Variables.ZONE_PROTECTED);
        return true;
      }
      return false;
    }

    return (defend_zone == null) || (attack_zone == null) || 
      (!defend_zone.hasFlag(Flags.PVP)) || (!attack_zone.hasFlag(Flags.PVP));
  }

  public static void setPvP(World world, boolean pvp)
  {
    setPvP(world.getName(), pvp);
  }
  public static void setPvP(String world, boolean pvp) { worlds.put(world, Boolean.valueOf(pvp)); }

  public static boolean canPvP(World world) {
    return canPvP(world.getName());
  }
  public static boolean canPvP(String world) { if (worlds.containsKey(world)) {
      return ((Boolean)worlds.get(world)).booleanValue();
    }
    return false; }

  public static void onEnable(iZone s)
  {
    for (World w : s.getServer().getWorlds()) {
      if (w == null)
        continue;
      setPvP(w, w.getPVP());
      s.sM(w.getName() + ".pvp = " + w.getPVP());
      w.setPVP(true);
    }
  }

  public static void onDisable(iZone s) {
    for (World w : s.getServer().getWorlds()) {
      if (w == null)
        continue;
      w.setPVP(canPvP(w));
    }
  }
}