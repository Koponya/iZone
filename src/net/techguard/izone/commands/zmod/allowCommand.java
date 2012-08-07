package net.techguard.izone.commands.zmod;

import net.milkbowl.vault.economy.Economy;
import net.techguard.izone.Properties;
import net.techguard.izone.Variables;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.VaultManager;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Zone;
import org.bukkit.entity.Player;

public class allowCommand extends zmodBase
{
  public allowCommand(iZone instance)
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
      if (!zone.getAllowed().contains(player)) {
        if (Properties.usingVault) {
          Economy vault = VaultManager.instance;

          if (vault.has(p.getName(), Properties.V_Allow)) {
            vault.withdrawPlayer(p.getName(), Properties.V_Allow);
          } else {
            p.sendMessage("§cYou don't have enough money (" + vault.format(Properties.V_Allow) + ")");
            return;
          }
        }
        zone.Add(player);
        p.sendMessage("§bAdded §f" + player + "§b to the zone whitelist");
      } else {
        p.sendMessage("§cThat player is already in the zone whitelist");
      }
    } else {
      p.sendMessage("§cThat zone doesn't exist");
    }
  }

  public int getLength() {
    return 4;
  }

  public String[] getInfo() {
    return new String[] { "allow", " <zone> <player>", "Add player to zone list" };
  }

  public String getError(int i) {
    return "§cUsage: /zmod allow <zone> <player>";
  }

  public String getPermission() {
    return Variables.PERMISSION_ALLOW;
  }
}