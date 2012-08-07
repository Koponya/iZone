package net.techguard.izone.commands.zmod;

import java.io.File;
import net.milkbowl.vault.economy.Economy;
import net.techguard.izone.Properties;
import net.techguard.izone.Variables;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.VaultManager;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Zone;
import org.bukkit.entity.Player;

public class deleteCommand extends zmodBase
{
  public deleteCommand(iZone instance)
  {
    super(instance);
  }

  public void onCommand(Player p, String[] cmd) {
    String name = cmd[2];

    if (ZoneManager.getZone(name) != null) {
      Zone zone = ZoneManager.getZone(name);
      if ((!zone.getOwners().contains(p.getName())) && (!p.hasPermission(Variables.PERMISSION_OWNER))) {
        p.sendMessage("§cYou are not the owner of this zone");
        return;
      }
      if (Properties.usingVault) {
        Economy vault = VaultManager.instance;

        if (vault.has(p.getName(), Properties.V_Delete)) {
          vault.withdrawPlayer(p.getName(), Properties.V_Delete);
        } else {
          p.sendMessage("§cYou don't have enough money (" + vault.format(Properties.V_Delete) + ")");
          return;
        }
      }
      ZoneManager.delete(zone);
      File file = zone.getSaveFile();
      file.delete();
      p.sendMessage("§bDeleted zone, §f" + zone.getName());
    } else {
      p.sendMessage("§cThat zone doesn't exist");
    }
  }

  public int getLength() {
    return 3;
  }

  public String[] getInfo() {
    return new String[] { "delete", " <name>", "Delete a zone" };
  }

  public String getError(int i) {
    return "§cUsage: /zmod delete <name>";
  }

  public String getPermission() {
    return Variables.PERMISSION_DELETE;
  }
}