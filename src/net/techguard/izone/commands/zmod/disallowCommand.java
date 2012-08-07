package net.techguard.izone.commands.zmod;

import net.milkbowl.vault.economy.Economy;
import net.techguard.izone.Properties;
import net.techguard.izone.Variables;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.VaultManager;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Zone;
import org.bukkit.entity.Player;

public class disallowCommand extends zmodBase
{
  public disallowCommand(iZone instance)
  {
    super(instance);
  }

  public void onCommand(Player p, String[] cmd) {
    String name = cmd[2];
    String player = cmd[3];

    if (ZoneManager.getZone(name) != null) {
      Zone zone = ZoneManager.getZone(name);
      if ((!zone.getOwners().contains(p.getName())) && (!p.hasPermission(Variables.PERMISSION_OWNER))) {
        p.sendMessage("§cYou are not the owner of this zone");
        return;
      }
      if (zone.getAllowed().contains(player)) {
        if (Properties.usingVault) {
          Economy vault = VaultManager.instance;

          if (vault.has(p.getName(), Properties.V_Disallow)) {
            vault.withdrawPlayer(p.getName(), Properties.V_Disallow);
          } else {
            p.sendMessage("§cYou don't have enough money (" + vault.format(Properties.V_Disallow) + ")");
            return;
          }
        }
        zone.Remove(player);
        p.sendMessage("§bRemoved §f" + player + "§b from the zone whitelist");
      } else {
        p.sendMessage("§cThat player isn't in the zone whitelist");
      }
    } else {
      p.sendMessage("§cThat zone doesn't exist");
    }
  }

  public int getLength() {
    return 4;
  }

  public String[] getInfo() {
    return new String[] { "disallow", " <zone> <player>", "Remove player from zone list" };
  }

  public String getError(int i) {
    return "§cUsage: /zmod disallow <zone> <player>";
  }

  public String getPermission() {
    return Variables.PERMISSION_DISALLOW;
  }
}