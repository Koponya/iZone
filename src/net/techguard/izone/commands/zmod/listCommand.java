package net.techguard.izone.commands.zmod;

import java.util.ArrayList;
import net.techguard.izone.Variables;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Zone;
import org.bukkit.entity.Player;

public class listCommand extends zmodBase
{
  public listCommand(iZone instance)
  {
    super(instance);
  }

  public void onCommand(Player p, String[] cmd) {
    ArrayList<Zone> zones = ZoneManager.getZones();
    String list = "§bZone list(§f" + zones.size() + "§b):";

    for (Zone zone : zones) {
      if ((!p.hasPermission(Variables.PERMISSION_LIST_ALL)) && 
        (!zone.getOwners().contains(p.getName()))) continue;
      list = list + " §f" + zone.getName() + "§b,";
    }

    if (list.endsWith("§b,")) list = list.substring(0, list.length() - 3);

    p.sendMessage(list);
  }

  public int getLength() {
    return 2;
  }

  public String[] getInfo() {
    return new String[] { "list", "", "Prints list off all owned zones" };
  }

  public String getError(int i) {
    return "";
  }

  public String getPermission() {
    return Variables.PERMISSION_LIST;
  }
}