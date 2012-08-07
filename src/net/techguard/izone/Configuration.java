package net.techguard.izone;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class Configuration
{
  private File fileFile;
  private YamlConfiguration ymlFile;

  public Configuration(String file)
  {
    this(new File(file));
  }

  public Configuration(File file) {
    this.fileFile = file;
    this.ymlFile = new YamlConfiguration();
  }

  public void load() {
    this.ymlFile = YamlConfiguration.loadConfiguration(this.fileFile);
  }

  public void save() {
    try {
      this.ymlFile.save(this.fileFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setProperty(String s, Object obj) {
    this.ymlFile.set(s, obj);
  }

  public boolean contains(String s) {
    return this.ymlFile.contains(s);
  }

  public Object get(String s) {
    return this.ymlFile.get(s);
  }

  public Object get(String s, Object obj) {
    return this.ymlFile.get(s, obj);
  }

  public boolean getBoolean(String s) {
    return this.ymlFile.getBoolean(s);
  }

  public boolean getBoolean(String s, boolean b) {
    return this.ymlFile.getBoolean(s, b);
  }

  public List<Boolean> getBooleanList(String s) {
    return this.ymlFile.getBooleanList(s);
  }

  public List<Byte> getByteList(String s) {
    return this.ymlFile.getByteList(s);
  }

  public List<Character> getCharacterList(String s) {
    return this.ymlFile.getCharacterList(s);
  }

  public double getDouble(String s) {
    return this.ymlFile.getDouble(s);
  }

  public double getDouble(String s, double d) {
    return this.ymlFile.getDouble(s, d);
  }

  public List<Double> getDoubleList(String s) {
    return this.ymlFile.getDoubleList(s);
  }

  public List<Float> getFloatList(String s) {
    return this.ymlFile.getFloatList(s);
  }

  public int getInt(String s) {
    return this.ymlFile.getInt(s);
  }

  public int getInt(String s, int i) {
    return this.ymlFile.getInt(s, i);
  }

  public List<Integer> getIntegerList(String s) {
    return this.ymlFile.getIntegerList(s);
  }

  public ItemStack getItemStack(String s) {
    return this.ymlFile.getItemStack(s);
  }

  public ItemStack getItemStack(String s, ItemStack stack) {
    return this.ymlFile.getItemStack(s, stack);
  }

  public List<?> getList(String s) {
    return this.ymlFile.getList(s);
  }

  public List<?> getList(String s, List<?> l) {
    return this.ymlFile.getList(s, l);
  }

  public long getLong(String s) {
    return this.ymlFile.getLong(s);
  }

  public long getLong(String s, long l) {
    return this.ymlFile.getLong(s, l);
  }

  public List<Long> getLongList(String s) {
    return this.ymlFile.getLongList(s);
  }

  public String getName() {
    return this.ymlFile.getName();
  }

  public OfflinePlayer getOfflinePlayer(String s) {
    return this.ymlFile.getOfflinePlayer(s);
  }

  public OfflinePlayer getOfflinePlayer(String s, OfflinePlayer player) {
    return this.ymlFile.getOfflinePlayer(s, player);
  }

  public List<Short> getShortList(String s) {
    return this.ymlFile.getShortList(s);
  }

  public String getString(String s) {
    return this.ymlFile.getString(s);
  }

  public String getString(String s1, String s2) {
    return this.ymlFile.getString(s1, s2);
  }

  public List<String> getStringList(String s) {
    return this.ymlFile.getStringList(s);
  }

  public Vector getVector(String s) {
    return this.ymlFile.getVector(s);
  }

  public Vector getVector(String s, Vector arg1) {
    return this.ymlFile.getVector(s);
  }

  public Set<String> getKeys(String s) {
    return this.ymlFile.getConfigurationSection(s).getKeys(false);
  }

  public Map<String, Object> getAll() {
    return this.ymlFile.getDefaultSection().getValues(true);
  }
}