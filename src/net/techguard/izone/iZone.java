package net.techguard.izone;

import java.util.Iterator;
import net.minecraft.server.Packet250CustomPayload;
import net.techguard.izone.commands.CommandManager;
import net.techguard.izone.commands.VersionCommand;
import net.techguard.izone.commands.zmodCommand;
import net.techguard.izone.listeners.bListener;
import net.techguard.izone.listeners.eListener;
import net.techguard.izone.listeners.pListener;
import net.techguard.izone.listeners.wListener;
import net.techguard.izone.managers.HealthManager;
import net.techguard.izone.managers.InfoManager;
import net.techguard.izone.managers.PvPManager;
import net.techguard.izone.managers.UpdateManager;
import net.techguard.izone.managers.VaultManager;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Zone;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class iZone extends JavaPlugin
{
  public String name;
  public String displayname;
  public String version;
  public String description;
  private static Plugin instance;
  private bListener bL = new bListener();
  private eListener eL = new eListener();
  private pListener pL = new pListener();
  private wListener wL = new wListener();
  private CommandManager commandManager;
  public HealthManager healthManager;

  public void onDisable()
  {
    PvPManager.onDisable(this);
  }

  public void onEnable() {
    this.name = getDescription().getName();
    this.displayname = getDescription().getFullName();
    this.version = getDescription().getVersion();
    this.description = getDescription().getDescription();
    PvPManager.onEnable(this);

    registerEvents();
    registerCommands();
    this.healthManager = new HealthManager(this);

    instance = this;
    InfoManager.enable();
    VaultManager.load(this);
    Properties.reload();
    checkForUpdate();
  }

  public static iZone getInstance() {
    return (iZone)instance;
  }

  private void checkForUpdate() {
    UpdateManager.check();
    if (UpdateManager.isUpdated())
      sM("NEW VERSION AVAILABLE: v" + UpdateManager.LATEST_VERSION + "!");
  }

  private void registerEvents()
  {
    PluginManager pm = getServer().getPluginManager();

    pm.registerEvents(this.bL, this);
    pm.registerEvents(this.pL, this);
    pm.registerEvents(this.eL, this);
    pm.registerEvents(this.wL, this);
  }

  private void registerCommands() {
    this.commandManager = new CommandManager();
    this.commandManager.registerCommand(new VersionCommand(this));
    this.commandManager.registerCommand(new zmodCommand(this));
  }

  public void sendInfo(Player p, Location loc) {
    Zone zone = ZoneManager.getZone(loc);

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
      p.sendMessage("§bNo zone found");
    }
  }

  public void sM(String message) {
    System.out.println("[" + this.displayname + "] " + message);
  }

  public Player getPlayer(String name) {
    for (Player pl : getServer().getOnlinePlayers()) {
      if (pl.getName().toLowerCase().startsWith(name.toLowerCase())) {
        return pl;
      }
    }
    return null;
  }

  public void sendGlobalPacket(String channel, String message) {
    sendGlobalPacket(channel, message.getBytes());
  }
  public void sendGlobalPacket(String channel, byte[] message) {
    for (Player p : getServer().getOnlinePlayers())
      sendPlayerPacket(p, channel, message);
  }

  public void sendPlayerPacket(Player p, String channel, String message)
  {
    sendPlayerPacket(p, channel, message.getBytes());
  }
  public void sendPlayerPacket(Player p, String channel, byte[] message) {
    CraftPlayer cp = (CraftPlayer)p;

    Packet250CustomPayload packet = new Packet250CustomPayload();
    packet.tag = channel;
    packet.length = message.length;
    packet.data = message;
    cp.getHandle().netServerHandler.sendPacket(packet);
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
  {
    return this.commandManager.dispatch(sender, label, args);
  }
}