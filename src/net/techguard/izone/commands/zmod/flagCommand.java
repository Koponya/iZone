package net.techguard.izone.commands.zmod;

import net.techguard.izone.Variables;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Zone;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class flagCommand extends zmodBase
{
  public flagCommand(iZone instance)
  {
    super(instance);
  }

  public void onCommand(Player p, String[] cmd) {
    String name = cmd[2];
    String flag = cmd[3];

    if (ZoneManager.getZone(name) != null) {
      Zone zone = ZoneManager.getZone(name);
      if ((!zone.getOwners().contains(p.getName())) && (!p.hasPermission(Variables.PERMISSION_OWNER))) {
        p.sendMessage("§cYou are not the owner of this zone");
        return;
      }
      String text = "";
      if (cmd.length >= 5) {
        for (int i = 4; i < cmd.length; i++) text = text + cmd[i] + " ";
        if (text.endsWith(" ")) text = text.substring(0, text.length() - 1);
      }

      for (Flags flag2 : Flags.values())
        if (flag2.toString().toLowerCase().startsWith(flag.toLowerCase())) {
          if (!p.hasPermission(Variables.PERMISSION_FLAGS + flag2.toString())) {
            p.sendMessage("§cYou don't have the permission for that flag");
            return;
          }
          if ((flag2 == Flags.WELCOME) || (flag2 == Flags.FAREWELL)) {
            if (text.length() > 0) {
              if (flag2 == Flags.WELCOME) zone.setWelcome(text);
              if (flag2 == Flags.FAREWELL) zone.setFarewell(text);
              p.sendMessage("§bChanged §f" + flag2.getName() + "§b text.");
              if (zone.hasFlag(flag2)) return;
            }
            else if (!zone.hasFlag(flag2)) {
              p.sendMessage("§bYou can change the message by adding the flag data");
            }
          }
          if (flag2 == Flags.TEXTUREPACK)
            if (text.length() > 0) {
              zone.setTexturePack(text);
              p.sendMessage("§bChanged §f" + flag2.getName() + "§b URL.");
              if (zone.hasFlag(flag2)) return;
            }
            else if (!zone.hasFlag(flag2)) {
              p.sendMessage("§bYou can change the URL by adding the flag data");
            }
          short data;
          int amount;
          if ((flag2 == Flags.GIVEITEM_IN) || (flag2 == Flags.GIVEITEM_OUT) || (flag2 == Flags.TAKEITEM_IN) || (flag2 == Flags.TAKEITEM_OUT)) {
            if (text.length() > 0) {
              if (text.startsWith("+ ")) {
                ItemStack item = getItemStack(text = text.replaceFirst("\\+ ", ""));

                if (item != null) {
                  zone.addInventory(flag2, item);
                  Material type = item.getType();
                  data = item.getData().getData();
                  amount = item.getAmount();
                  p.sendMessage("§b'§f" + flag2.getName() + "§b' added §f" + (type == Material.AIR ? "All" : type.name()) + "§b:§f" + (data == -1 ? "All" : Short.valueOf(data)) + "§b,§f" + (amount == -1 ? "All" : Integer.valueOf(amount)));
                } else {
                  p.sendMessage("§cSomething went wrong. " + text);
                }
              }
              else if (text.startsWith("- ")) {
                ItemStack item = getItemStack(text = text.replaceFirst("\\- ", ""));

                if (item != null) {
                  zone.removeInventory(flag2, item);
                  Material type = item.getType();
                  data = item.getData().getData();
                  amount = item.getAmount();
                  p.sendMessage("§b'§f" + flag2.getName() + "§b' removed §f" + (type == Material.AIR ? "All" : type.name()) + "§b:§f" + (data == -1 ? "All" : Short.valueOf(data)) + "§b,§f" + (amount == -1 ? "All" : Integer.valueOf(amount)));
                } else {
                  p.sendMessage("§cSomething went wrong. " + text);
                }
              } else {
                p.sendMessage("§cWrong flag data. Syntax: <+ / -> <item>[:data][,amount]");
              }
              return;
            }
            if (!zone.hasFlag(flag2)) {
              p.sendMessage("§bYou can change the inventory by adding the flag data (§f <+ / -> <item>[:data][,amount] §b)");
            }
          }
          if (flag2 == Flags.GAMEMODE) {
            if (text.length() > 0) {
              GameMode gm = null;
              GameMode[] arrayOfGameMode;
              amount = (arrayOfGameMode = GameMode.values()).length; for (data = 0; data < amount; data++) { GameMode mode = arrayOfGameMode[data];
                if (mode.name().toLowerCase().startsWith(text.toLowerCase()))
                  gm = mode;
              }
              if (gm != null) {
                zone.setGamemode(gm);
                p.sendMessage("§bChanged Gamemode to §f" + gm.name().toLowerCase() + "§b.");
                if (zone.hasFlag(flag2)) return; 
              }
              else {
                p.sendMessage("§cGamemode not found.");
              }
            }
            else if (!zone.hasFlag(flag2)) {
              p.sendMessage("§bYou can change the gamemode type by adding the flag data");
            }
          }
          zone.setFlag(flag2.getId(), !zone.hasFlag(flag2));

          if (zone.hasFlag(flag2))
            p.sendMessage("§bToggled the §f" + flag2.getName() + "§b flag on");
          else p.sendMessage("§bToggled the §f" + flag2.getName() + "§b flag off"); 
        }
    }
    else
    {
      p.sendMessage("§cThat zone doens't exist");
    }
  }

  public int getLength() {
    return 4;
  }

  public String[] getInfo() {
    return new String[] { "flag", " <zone> <flag> [flag data]", "Toggle a zone flag" };
  }
  public String getError(int i) {
    return getError(null, i);
  }
  public String getError(Player p, int i) { if (p != null) {
      String flags = "";
      for (Flags f : Flags.values()) {
        if (p.hasPermission(Variables.PERMISSION_FLAGS + f.toString()))
          flags = flags + "§f" + f.toString() + "§c, ";
      }
      if (flags.endsWith("§c, ")) flags = flags.substring(0, flags.length() - 4);
      p.sendMessage("§cAvailable Flags: " + flags);
    }
    return "§cUsage: /zmod flag <zone> <flag>"; }

  public String getPermission()
  {
    return Variables.PERMISSION_FLAG;
  }

  private ItemStack getItemStack(String text) {
    String item = text;
    String data = "";
    String amount = "";

    if (text.equals("*")) {
      return new ItemStack(Material.AIR, -1, (short) -1);
    }

    if (text.contains(",")) {
      String[] split = item.split(",");
      item = split[0];
      amount = split[1];
    }
    if (item.contains(":")) {
      String[] split = item.split(":");
      item = split[0];
      data = split[1];
    }
    Material item0 = Material.matchMaterial(item);
    if ((item.equals("*")) || (item.equals("-1"))) {
      item0 = Material.AIR;
    }
    if (item0 == null) {
      return null;
    }
    short data0 = 0;
    int amount0 = 1;
    try {
      if ((data.equals("*")) || (data.equals("-1"))) {
        data0 = -1;
      }
      else if (!data.equals(""))
        data0 = (short)Integer.parseInt(data);
    } catch (Exception localException) {
    }
    try {
      if ((amount.equals("*")) || (amount.equals("-1"))) {
        amount0 = -1;
      }
      else if (!amount.equals(""))
        amount0 = Integer.parseInt(amount);
    }
    catch (Exception localException1) {
    }
    return new ItemStack(item0, amount0, data0);
  }
}