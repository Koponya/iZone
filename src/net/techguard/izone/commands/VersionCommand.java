package net.techguard.izone.commands;

import net.techguard.izone.iZone;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class VersionCommand extends BaseCommand
{
  public VersionCommand(iZone instance)
  {
    super(instance);
  }

  public void onPlayerCommand(Player p, String[] cmd)
  {
    p.sendMessage("§6" + this.plugin.displayname + "§7 created by §6TechGuard");
  }

  public void onSystemCommand(ConsoleCommandSender p, String[] cmd)
  {
    p.sendMessage("§6" + this.plugin.displayname + "§7 created by §6TechGuard");
  }

  protected String[] getUsage()
  {
    return new String[] { "izone" };
  }
}