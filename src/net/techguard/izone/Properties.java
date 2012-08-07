package net.techguard.izone;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import net.techguard.izone.managers.VaultManager;
import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Zone;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Properties
{
  public static String dataFolder = "";
  private static File configFile;
  private static File VaultFile;
  public static int defineTool;
  public static int checkTool;
  public static int healTime;
  public static int healAmount;
  public static int hurtTime;
  public static int hurtAmount;
  public static boolean[] autoFlag = new boolean[Flags.values().length];
  public static HashMap<String, Boolean> pvp = new HashMap<String, Boolean>();
  public static double V_Create;
  public static double V_Delete;
  public static double V_Allow;
  public static double V_Disallow;
  public static boolean usingVault;

  private static void load()
  {
    dataFolder = iZone.getInstance().getDataFolder().getAbsolutePath() + "/";
    configFile = new File(dataFolder + "config.yml");
    VaultFile = new File(dataFolder + "vault.yml");

    checkForConfig();
    checkForVault();

    loadConfig(new Configuration(configFile));
    loadVault(new Configuration(VaultFile));

    loadUpdate();
  }

  public static void reload() {
    load();
  }

  private static void checkForVault() {
    if (!VaultFile.exists())
      try {
        VaultFile.getParentFile().mkdir();
        VaultFile.createNewFile();
        Configuration config = new Configuration(VaultFile);

        config.setProperty("enabled", Boolean.valueOf(false));
        config.setProperty("create-zone", Integer.valueOf(0));
        config.setProperty("delete-zone", Integer.valueOf(0));
        config.setProperty("allow-player", Integer.valueOf(0));
        config.setProperty("disallow-player", Integer.valueOf(0));

        config.save();
      } catch (Exception e) {
        iZone.getInstance().sM(e.getMessage());
        e.printStackTrace();
      }
  }

  private static void checkForConfig()
  {
    if (!configFile.exists())
      try {
        configFile.getParentFile().mkdir();
        configFile.createNewFile();
        Configuration config = new Configuration(configFile);

        for (Flags flag : Flags.values()) {
          config.setProperty("on-create." + flag.toString(), Boolean.valueOf((flag == Flags.PROTECTION) || (flag == Flags.INTERACT)));
        }
        config.setProperty("tools.check", Integer.valueOf(268));
        config.setProperty("tools.define", Integer.valueOf(269));
        config.setProperty("healing.time", Integer.valueOf(3));
        config.setProperty("healing.amount", Integer.valueOf(1));
        config.setProperty("hurting.time", Integer.valueOf(7));
        config.setProperty("hurting.amount", Integer.valueOf(2));

        config.setProperty("restriction.size.(-1, -1, -1)", "izone.zone.max-size.unlimited");
        config.setProperty("restriction.size.(50, 256, 50)", "izone.zone.max-size.1");
        config.setProperty("restriction.zone.-1", "izone.zone.max-zone.unlimited");
        config.setProperty("restriction.zone.3", "izone.zone.max-zone.1");

        config.save();
      } catch (Exception e) {
        iZone.getInstance().sM(e.getMessage());
        e.printStackTrace();
      }
  }

  private static void loadVault(Configuration config)
  {
    try {
      config.load();

      usingVault = config.getBoolean("enabled");

      if (!VaultManager.isEnabled()) usingVault = false;

      V_Create = config.getDouble("create-zone");
      V_Delete = config.getDouble("delete-zone");
      V_Allow = config.getDouble("allow-player");
      V_Disallow = config.getDouble("disallow-player");
    } catch (Exception e) {
      iZone.getInstance().sM(e.getMessage());
      e.printStackTrace();
    }
  }

  private static void loadConfig(Configuration config) {
    try {
      config.load();

      for (Flags flag : Flags.values()) {
        autoFlag[flag.getId().intValue()] = config.getBoolean("on-create." + flag.toString(), false);
      }
      checkTool = config.getInt("tools.check");
      defineTool = config.getInt("tools.define");
      healTime = config.getInt("healing.time");
      healAmount = config.getInt("healing.amount");
      hurtTime = config.getInt("hurting.time");
      hurtAmount = config.getInt("hurting.amount");
      for (String key : config.getKeys("restriction.size")) {
        Vector size = getVector(key);
        if (size == null)
          continue;
        String permission = config.getString("restriction.size." + key, "none");
        if (permission.equals("none"))
          continue;
        Variables.PERMISSION_MAX_SIZE.put(permission, size);
      }
      for (String key : config.getKeys("restriction.zone")) {
        int size = 0;
        try { size = Integer.parseInt(key); } catch (Exception e) { continue;
        }
        String permission = config.getString("restriction.zone." + key, "none");
        if (permission.equals("none"))
          continue;
        Variables.PERMISSION_MAX_ZONE.put(permission, Integer.valueOf(size));
      }
    } catch (Exception e) {
      iZone.getInstance().sM(e.getMessage());
      e.printStackTrace();
    }
  }

  private static Vector getVector(String value) {
    Vector vector = new Vector(0, 0, 0);
    String[] split = value.split(", ");
    try {
      vector.setX(Double.parseDouble(split[0].substring(1)));
      vector.setY(Double.parseDouble(split[1]));
      vector.setZ(Double.parseDouble(split[2].substring(0, split[2].length() - 1))); } catch (Exception e) {
      return null;
    }return vector;
  }

  private static ItemStack getItemStack(String value) {
    ItemStack item = null;
    String[] split = value.split(", ");
    try {
      ItemStack stack = new ItemStack(0);
      stack.setType(Material.matchMaterial(split[0].substring(1)));
      stack.setAmount(Integer.parseInt(split[1]));
      stack.setDurability(Short.parseShort(split[2].substring(0, split[2].length() - 1)));
      item = stack; } catch (Exception e) {
      return null;
    }return item;
  }

  private static boolean loadZone(File file) {
    boolean failed = true;
    try {
      Configuration saveFile = new Configuration(file);
      saveFile.load();

      Zone zone = new Zone(file.getName().replace(".yml", ""));
      zone.setSave(false);
      try {
        zone.setWorld(iZone.getInstance().getServer().getWorld(saveFile.getString("world", "world")));
      } catch (NullPointerException npe) {
        iZone.getInstance().sM("Failed to set world for '" + zone.getName() + "'! " + 
          "The world '" + saveFile.getString("world", "world") + "' does NOT exist!");
        failed = false;
      }
      zone.setWelcome(saveFile.getString("welcome", ""));
      zone.setFarewell(saveFile.getString("farewell", ""));
      zone.setGamemode(GameMode.valueOf(saveFile.getString("gamemode", "SURVIVAL")));
      zone.setBorder(1, new Location(zone.getWorld(), saveFile.getInt("border1.x", 0), saveFile.getInt("border1.y", 0), saveFile.getInt("border1.z", 0)));
      zone.setBorder(2, new Location(zone.getWorld(), saveFile.getInt("border2.x", 0), saveFile.getInt("border2.y", 0), saveFile.getInt("border2.z", 0)));
      for (Flags flag : Flags.values()) {
        zone.setFlag(flag.getId(), saveFile.getBoolean("flag." + flag.toString(), false));
      }
      zone.setAllowed((ArrayList<String>)saveFile.getStringList("allowed"));
      zone.setParent(saveFile.getString("parent-zone", ""));
      zone.setCreationDate(Long.valueOf(saveFile.getLong("creation-date", 0L)));
      zone.setTexturePack(saveFile.getString("texturepack", ""));
      for (Flags flag : new Flags[] { Flags.GIVEITEM_IN, Flags.GIVEITEM_OUT, Flags.TAKEITEM_IN, Flags.TAKEITEM_OUT }) {
        List<String> items = saveFile.getStringList("inventory." + flag.toString());
        for (String key : items) {
          ItemStack item = getItemStack(key);
          if (item == null) continue; zone.addInventory(flag, item);
        }
      }

      zone.setSave(true);
      ZoneManager.add(zone);
      return true;
    } catch (Exception e) {
      if (failed) e.printStackTrace();
    }
    return false;
  }

  public static void loadUpdate() {
    try {
      int loaded = 0;
      File dir = new File(dataFolder + "saves/");
      if (!dir.exists()) dir.mkdirs();

      for (File file : dir.listFiles()) {
        if ((file.getName().endsWith(".yml")) && 
          (loadZone(file))) loaded++;
      }

      iZone.getInstance().sM("Loaded " + loaded + " zones");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}