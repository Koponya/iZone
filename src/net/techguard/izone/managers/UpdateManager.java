package net.techguard.izone.managers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import net.techguard.izone.iZone;

public class UpdateManager
{
  public static String CURRENT_VERSION = "";
  public static String LATEST_VERSION = "";
  public static String REASON = "";

  public static void check() {
    iZone plugin = iZone.getInstance();
    CURRENT_VERSION = plugin.version;
    try {
      URL url = new URL("http://www.techguard.info/Bukkit/version_izone.txt");
      BufferedReader input = new BufferedReader(new InputStreamReader(url.openStream()));
      LATEST_VERSION = input.readLine();
      REASON = input.readLine();
      input.close();
    } catch (Exception e) {
      LATEST_VERSION = CURRENT_VERSION;
    }
  }

  public static boolean isUpdated() {
    int currentVersion = 0;
    int latestVersion = 0;
    try {
      currentVersion = Integer.parseInt(CURRENT_VERSION.replaceAll("\\.", ""));
      latestVersion = Integer.parseInt(LATEST_VERSION.replaceAll("\\.", "")); } catch (Exception localException) {
    }
    return currentVersion < latestVersion;
  }
}