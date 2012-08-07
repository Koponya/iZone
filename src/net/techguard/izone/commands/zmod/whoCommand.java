package net.techguard.izone.commands.zmod;

import net.techguard.izone.Variables;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Zone;
import org.bukkit.entity.Player;

public class whoCommand extends zmodBase
{
  public whoCommand(iZone instance)
  {
    super(instance);
  }

  public void onCommand(Player p, String[] cmd) {
    Zone zone = ZoneManager.getZone(p.getLocation());
    if (zone == null) {
      p.sendMessage("§cYou need to be in a zone");
      return;
    }
    int Count = 0;
    String List = "";
    for (Player p2 : p.getWorld().getPlayers()) {
      if (ZoneManager.getZone(p2.getLocation()) == zone) {
        List = List + " §f" + p2.getName() + "§b,";
        Count++;
      }
    }
    if (List.endsWith("§b,")) List = List.substring(0, List.length() - 3);
    p.sendMessage("§b" + zone.getName() + "(§f" + Count + "§b):" + List);
  }

  public int getLength() {
    return 2;
  }

  public String[] getInfo() {
    return new String[] { "who", "", "List of players in your current zone" };
  }

  public String getError(int i) {
    return "";
  }

  public String getPermission() {
    return Variables.PERMISSION_WHO;
  }
}