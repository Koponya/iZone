package net.techguard.izone.commands;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class CommandManager
{
  protected List<BaseCommand> commands;

  public CommandManager()
  {
    this.commands = new ArrayList<BaseCommand>();
  }

  public boolean dispatch(CommandSender sender, String label, String[] args) {
    BaseCommand match = null;

    for (BaseCommand cmd : this.commands)
    {
      String[] arrayOfString2;
      int i = (arrayOfString2 = cmd.getUsage()).length; 
      for (int str1 = 0; str1 < i; str1++) { 
    	  String usage = arrayOfString2[str1];
        if (!usage.equalsIgnoreCase(label)) continue; match = cmd;
      }
    }

    String tmp = label + " ";
    String[] arrayOfString1;
    int str1 = (arrayOfString1 = args).length;
    for (int usage = 0; usage < str1; usage++) { 
    	String s = arrayOfString1[usage];
    	tmp = tmp + s + " ";
    }
    args = tmp.split(" ");

    if (match != null) {
      try {
        if ((sender instanceof Player)) {
          match.onPlayerCommand((Player)sender, args);
        }
        else if ((sender instanceof ConsoleCommandSender)) {
          match.onSystemCommand((ConsoleCommandSender)sender, args);
        }
        return true;
      } catch (Exception e) {
        sender.sendMessage("§cError trying to execute the command.");
        e.printStackTrace();
      }
    }

    return false;
  }

  public void registerCommand(BaseCommand command) {
    this.commands.add(command);
  }

  public void unRegisterCommand(BaseCommand command) {
    this.commands.remove(command);
  }
}