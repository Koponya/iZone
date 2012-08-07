package net.techguard.izone.commands.zmod;

import net.techguard.izone.Variables;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Zone;
import org.bukkit.entity.Player;

public class parentCommand extends zmodBase
{
  public parentCommand(iZone instance)
  {
    super(instance);
  }

  public void onCommand(Player p, String[] cmd) {
    Zone child = null;
    Zone parent = null;

    if (ZoneManager.getZone(cmd[2]) != null)
      child = ZoneManager.getZone(cmd[2]);
    else {
      p.sendMessage("§c" + cmd[2] + " isn't a zone");
    }
    if (ZoneManager.getZone(cmd[3]) != null)
      parent = ZoneManager.getZone(cmd[3]);
    else {
      p.sendMessage("§c" + cmd[3] + " isn't a zone");
    }

    if ((child != null) && (parent != null)) {
      if ((!child.getOwners().contains(p.getName())) && (!p.hasPermission(Variables.PERMISSION_OWNER))) {
        p.sendMessage("§cYou are not the owner of this zone");
        return;
      }
      child.setParent(parent.getName());
      p.sendMessage("§bSet parent zone to §f" + parent.getName() + "§b of §f" + child.getName());
    }
  }

  public int getLength() {
    return 4;
  }

  public String[] getInfo() {
    return new String[] { "parent", " <child> <parent>", "Set a parent for a child zone" };
  }

  public String getError(int i) {
    return "§cUsage: /zmod parent <child> <parent>";
  }

  public String getPermission() {
    return Variables.PERMISSION_PARENT;
  }
}