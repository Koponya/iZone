package net.techguard.izone.zones;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.techguard.izone.Properties;
import net.techguard.izone.iZone;
import net.techguard.izone.managers.ZoneManager;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class Zone
{
  private String name = "";
  private World world = null;
  private Location border1 = null;
  private Location border2 = null;
  private long creationDate = 0L;
  private String welcome = "";
  private String farewell = "";
  private String texturepack = "";
  private GameMode gamemode = GameMode.SURVIVAL;
  private HashMap<Flags, ArrayList<ItemStack>> inventory;
  private boolean cansave = true;
  private boolean[] flags = new boolean[Flags.values().length];
  private ArrayList<String> allowed = new ArrayList<String>();
  private String parent = "";
  private YamlConfiguration saveFile;

  public Zone(String s)
  {
    this.name = s;
    this.saveFile = YamlConfiguration.loadConfiguration(getSaveFile());
    this.inventory = new HashMap<Flags, ArrayList<ItemStack>>();
  }

  public String getName() {
    return this.name;
  }

  public ArrayList<String> getOwners() {
    ArrayList<String> owners = new ArrayList<String>();
    for (String a : this.allowed) {
      if (!a.startsWith("o:")) continue; owners.add(a.substring(2));
    }
    return owners;
  }

  public Location getBorder1() {
    return this.border1;
  }

  public Location getBorder2() {
    return this.border2;
  }

  public ArrayList<ItemStack> getInventory(Flags a) {
    if (this.inventory.containsKey(a)) return this.inventory.get(a);
    return new ArrayList<ItemStack>();
  }

  public ArrayList<String> getAllowed() {
    return this.allowed;
  }

  public boolean hasFlag(Flags a) {
    boolean flag = this.flags[a.getId().intValue()];
    if ((getParent() != null) && (!flag)) {
      flag = getParent().hasFlag(a);
    }
    return flag;
  }

  public String getWelcome() {
    if ((getParent() != null) && 
      (this.welcome.equals(""))) {
      return getParent().getWelcome();
    }

    return this.welcome;
  }

  public String getFarewell() {
    if ((getParent() != null) && 
      (this.farewell.equals(""))) {
      return getParent().getFarewell();
    }

    return this.farewell;
  }

  public GameMode getGamemode() {
    return this.gamemode;
  }

  public World getWorld() {
    return this.world;
  }

  public ArrayList<Flags> getFlags() {
    ArrayList<Flags> list = new ArrayList<Flags>();
    for (Flags flag : Flags.values()) {
      if (!hasFlag(flag)) continue; list.add(flag);
    }

    return list;
  }

  public Zone getParent() {
    Zone parent = null;
    if (this.parent.length() > 0) {
      parent = ZoneManager.getZone(this.parent);
      if (parent == null) {
        setParent("");
      }
    }
    return parent;
  }

  public boolean canSave() {
    return this.cansave;
  }

  public File getSaveFile() {
    File file = new File(Properties.dataFolder + "saves/" + this.name + ".yml");
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return file;
  }

  public long getCreationDate() {
    return this.creationDate;
  }

  public String getTexturePack() {
    return this.texturepack;
  }

  public void setBorder(int a, Location b) {
    if (a == 1) {
      this.border1 = b;
      setProperty("border1.x", Integer.valueOf(b.getBlockX()));
      setProperty("border1.y", Integer.valueOf(b.getBlockY()));
      setProperty("border1.z", Integer.valueOf(b.getBlockZ()));
    }
    else if (a == 2) {
      this.border2 = b;
      setProperty("border2.x", Integer.valueOf(b.getBlockX()));
      setProperty("border2.y", Integer.valueOf(b.getBlockY()));
      setProperty("border2.z", Integer.valueOf(b.getBlockZ()));
    }
    setWorld(b.getWorld());
  }

  public void setWorld(World a) {
    this.world = a;
    setProperty("world", a.getName());
  }

  public void setAllowed(ArrayList<String> a) {
    this.allowed = a;
    setProperty("allowed", a);
  }

  public void setFlag(Integer a, boolean b) {
    this.flags[a.intValue()] = b;
    setProperty("flag." + Flags.values()[a.intValue()].toString(), Boolean.valueOf(b));
  }

  public void setWelcome(String a) {
    this.welcome = a;
    setProperty("welcome", a);
  }

  public void setFarewell(String a) {
    this.farewell = a;
    setProperty("farewell", a);
  }

  public void setGamemode(GameMode a) {
    this.gamemode = a;
    setProperty("gamemode", a.name());
  }

  public void addInventory(Flags a, ItemStack b) {
    if (!this.inventory.containsKey(a)) {
      this.inventory.put(a, new ArrayList<ItemStack>());
    }
    this.inventory.get(a).add(b);
    saveInventory(a);
  }

  public void removeInventory(Flags a, ItemStack b) {
    if (this.inventory.containsKey(a) && this.inventory.get(a).contains(b)) {
      this.inventory.get(a).remove(b);
    }

    saveInventory(a);
  }

  public void saveInventory(Flags a) {
    if (this.inventory.get(a) == null) return;
    List<String> items = new ArrayList<String>();

    for (ItemStack item : this.inventory.get(a)) {
      if (item != null)
        items.add("(" + item.getTypeId() + ", " + item.getAmount() + ", " + item.getData().getData() + ")");
    }
    setProperty("inventory." + a.toString(), items);
  }

  public boolean Add(String a) {
    if (!this.allowed.contains(a)) {
      this.allowed.add(a);
      setProperty("allowed", this.allowed);
      return true;
    }
    return false;
  }

  public boolean Remove(String a) {
    if (this.allowed.contains(a)) {
      this.allowed.remove(a);
      setProperty("allowed", this.allowed);
      return true;
    }
    return false;
  }

  public void resetOwners() {
    for (String s : getOwners()) {
      s = "o:" + s;
      Remove(s);
    }
  }

  public void setParent(String a) {
    this.parent = a;
    setProperty("parent-zone", this.parent);
  }

  public void setSave(boolean a) {
    this.cansave = a;
  }

  public void setCreationDate(Long a) {
    this.creationDate = a.longValue();
    setProperty("creation-date", Long.valueOf(this.creationDate));
  }

  public void setTexturePack(String a) {
    this.texturepack = a;
    setProperty("texturepack", a);
  }

  private void setProperty(String key, Object value) {
    if (canSave())
      try {
        this.saveFile.load(getSaveFile());
        this.saveFile.set(key, value);
        this.saveFile.save(getSaveFile());
      } catch (Exception e) {
        e.printStackTrace();
        iZone.getInstance().sM("Couldn't set property '" + key + "' for zone '" + getName() + "'");
      }
  }
}