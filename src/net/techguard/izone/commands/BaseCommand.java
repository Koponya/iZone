package net.techguard.izone.commands;

import net.techguard.izone.iZone;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public abstract class BaseCommand
{
  public iZone plugin;

  public BaseCommand(iZone instance)
  {
    this.plugin = instance;
  }

  public abstract void onPlayerCommand(Player paramPlayer, String[] paramArrayOfString);

  public abstract void onSystemCommand(ConsoleCommandSender paramConsoleCommandSender, String[] paramArrayOfString);

  protected abstract String[] getUsage();
}