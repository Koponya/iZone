package net.techguard.izone.commands;

import java.util.ArrayList;
import net.techguard.izone.Variables;
import net.techguard.izone.commands.zmod.allowCommand;
import net.techguard.izone.commands.zmod.createCommand;
import net.techguard.izone.commands.zmod.deleteCommand;
import net.techguard.izone.commands.zmod.disallowCommand;
import net.techguard.izone.commands.zmod.expandCommand;
import net.techguard.izone.commands.zmod.flagCommand;
import net.techguard.izone.commands.zmod.infoCommand;
import net.techguard.izone.commands.zmod.listCommand;
import net.techguard.izone.commands.zmod.parentCommand;
import net.techguard.izone.commands.zmod.whoCommand;
import net.techguard.izone.commands.zmod.zmodBase;
import net.techguard.izone.iZone;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class zmodCommand extends BaseCommand
{
  private ArrayList<zmodBase> coms = new ArrayList<zmodBase>();

  public zmodCommand(iZone instance) {
    super(instance);
    this.coms.add(new listCommand(instance));
    this.coms.add(new whoCommand(instance));
    this.coms.add(new infoCommand(instance));
    this.coms.add(new createCommand(instance));
    this.coms.add(new deleteCommand(instance));
    this.coms.add(new allowCommand(instance));
    this.coms.add(new disallowCommand(instance));
    this.coms.add(new flagCommand(instance));
    this.coms.add(new parentCommand(instance));
    this.coms.add(new expandCommand(instance));
  }

  public void onPlayerCommand(Player p, String[] cmd)
  {
    if (cmd.length == 1) {
      p.sendMessage("§b===>§f iZone §b<===");
      for (zmodBase zmod : this.coms) {
        boolean permission = p.hasPermission(zmod.getPermission());
        if ((zmod instanceof listCommand)) {
          permission = (permission) || (p.hasPermission(Variables.PERMISSION_LIST_ALL));
        }
        if (permission) {
          String[] info = zmod.getInfo();
          p.sendMessage("§b/zmod " + info[0] + info[1] + " §f- " + info[2]);
        }
      }
    } else {
      for (zmodBase zmod : this.coms)
        if (zmod.getInfo()[0].equalsIgnoreCase(cmd[1])) {
          boolean permission = p.hasPermission(zmod.getPermission());
          if ((zmod instanceof listCommand)) {
            permission = (permission) || (p.hasPermission(Variables.PERMISSION_LIST_ALL));
          }
          if (permission) {
            if (cmd.length < zmod.getLength()) {
              if ((zmod instanceof flagCommand))
                p.sendMessage(((flagCommand)zmod).getError(p, cmd.length));
              else p.sendMessage(zmod.getError(cmd.length)); 
            }
            else
              zmod.onCommand(p, cmd);
          }
          else
            p.sendMessage(Variables.PERMISSION_NO);
        }
    }
  }

  public void onSystemCommand(ConsoleCommandSender p, String[] cmd)
  {
    p.sendMessage("You can only access this from in-game");
  }

  protected String[] getUsage()
  {
    return new String[] { "zmod" };
  }
}