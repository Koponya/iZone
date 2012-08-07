package net.techguard.izone;

import java.util.HashMap;
import org.bukkit.util.Vector;

public class Variables
{
  public static String ZONE_PROTECTED = "§4This is a protected zone";
  public static String ZONE_RESTRICTION = "§4That is a restricted zone";
  public static String ZONE_JAIL = "§4This is a restricted zone";

  public static String PERMISSION_NO = "§cYou don't have permission for that";

  public static String PERMISSION_DEFINE = "izone.tool.define";
  public static String PERMISSION_CHECK = "izone.tool.check";
  public static String PERMISSION_OWNER = "izone.zone.restriction.ignoreowner";
  public static String PERMISSION_ALLOW = "izone.zone.allow";
  public static String PERMISSION_CREATE = "izone.zone.create";
  public static String PERMISSION_DISALLOW = "izone.zone.disallow";
  public static String PERMISSION_DELETE = "izone.zone.delete";
  public static String PERMISSION_EXPAND = "izone.zone.expand";
  public static String PERMISSION_FLAG = "izone.zone.flag";
  public static String PERMISSION_LIST = "izone.zone.list";
  public static String PERMISSION_LIST_ALL = "izone.zone.list.all";
  public static String PERMISSION_INFO = "izone.zone.info";
  public static String PERMISSION_WHO = "izone.zone.who";
  public static String PERMISSION_PARENT = "izone.zone.parent";
  public static String PERMISSION_FLAGS = "izone.zone.flag.";

  public static HashMap<String, Vector> PERMISSION_MAX_SIZE = new HashMap<String, Vector>();
  public static HashMap<String, Integer> PERMISSION_MAX_ZONE = new HashMap<String, Integer>();
}