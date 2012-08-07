package net.techguard.izone.commands.zmod;

import net.milkbowl.vault.economy.Economy;
import net.techguard.izone.Properties;
import net.techguard.izone.Variables;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.VaultManager;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Settings;
import net.techguard.izone.zones.Zone;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class createCommand extends zmodBase
{
  public createCommand(iZone instance)
  {
    super(instance);
  }

  public void onCommand(Player p, String[] cmd) {
    Settings settings = Settings.getSett(p);
    String name = cmd[2];

    if (ZoneManager.getZone(name) == null) {
      Location[] c = { settings.getBorder1(), settings.getBorder2() };
      if ((c[0] == null) || (c[1] == null)) {
        p.sendMessage("§cYou need to define the borders first");
        p.sendMessage("§cYou can use a §f" + Material.getMaterial(Properties.defineTool).name() + "§c for that.");
        return;
      }
      String permission = ZoneManager.canBuildZone(p, c);
      if (!permission.equals("")) {
        if (permission.equals("max")) {
          String size = settings.getOwnedZones() + "/" + settings.getMaxZones();
          p.sendMessage("§cYou own too many zones (" + size + ")");
        }
        if (permission.startsWith("size")) {
          Vector maxSize = settings.getMaxSize();
          p.sendMessage("§cThe defined zone is too big  " + permission.split(":")[1] + " / (" + (int)maxSize.getX() + ", " + (int)maxSize.getY() + ", " + (int)maxSize.getZ() + ")");
        }
        return;
      }
      if (Properties.usingVault) {
        Economy vault = VaultManager.instance;

        if (vault.has(p.getName(), Properties.V_Create)) {
          vault.withdrawPlayer(p.getName(), Properties.V_Create);
        } else {
          p.sendMessage("§cYou don't have enough money (" + vault.format(Properties.V_Create) + ")");
          return;
        }
      }
      if(ZoneManager.checkZoneInside(c)) {
    	  p.sendMessage("§cNot touch each other with the zones!");
          return;
      }
      Zone zone = ZoneManager.add(name, c);
      zone.Add("o:" + p.getName());

      p.sendMessage("§bCreated a new zone, §f" + zone.getName());
    } else {
      p.sendMessage("§cThat zone already exists");
    }
  }

  public int getLength() {
    return 3;
  }

  public String[] getInfo() {
    return new String[] { "create", " <name>", "Create a new zone" };
  }

  public String getError(int i) {
    return "§cUsage: /zmod create <name>";
  }

  public String getPermission() {
    return Variables.PERMISSION_CREATE;
  }
}