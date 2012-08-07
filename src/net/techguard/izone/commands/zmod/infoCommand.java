package net.techguard.izone.commands.zmod;

import java.util.Iterator;
import net.techguard.izone.Variables;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Zone;
import org.bukkit.entity.Player;

public class infoCommand extends zmodBase
{
  public infoCommand(iZone instance)
  {
    super(instance);
  }

  public void onCommand(Player p, String[] cmd) {
    Zone zone = ZoneManager.getZone(cmd[2]);

    if (zone != null) {
      String flags = ""; String allowed = "";
      Flags flag;
      for (Iterator<Flags> localIterator = zone.getFlags().iterator(); localIterator.hasNext(); flags = flags + "§f" + flag.getName() + "§b, ") flag = localIterator.next();
      if (flags.endsWith("§b, ")) flags = flags.substring(0, flags.length() - 4);
      String s;
      for (Iterator<String> localIterator = zone.getAllowed().iterator(); localIterator.hasNext(); allowed = allowed + "§f" + s + "§b, ") s = localIterator.next();
      if (allowed.endsWith("§b, ")) allowed = allowed.substring(0, allowed.length() - 4);

      p.sendMessage("§b===>§f Zone found §b<===");
      p.sendMessage("§bName: §f" + zone.getName());
      p.sendMessage("§bFlags: §f" + flags);
      p.sendMessage("§bAllowed: §f" + allowed);
    } else {
      p.sendMessage("§cZone not found");
    }
  }

  public int getLength() {
    return 3;
  }

  public String[] getInfo() {
    return new String[] { "info", " <name>", "Prints info about the zone" };
  }

  public String getError(int i) {
    return "§cUsage: /zmod info <name>";
  }

  public String getPermission() {
    return Variables.PERMISSION_INFO;
  }
}