package net.techguard.izone.managers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import net.techguard.izone.iZone;
import org.bukkit.Server;

public class InfoManager
{
  private static String httpRequest(String url)
  {
    try
    {
      String content = "";
      URL conn = new URL(url);
      URLConnection yc = conn.openConnection();

      yc.setConnectTimeout(5000);
      yc.setReadTimeout(5000);

      BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
      String inputLine;
      while ((inputLine = in.readLine()) != null)
      {
        content = content + inputLine;
      }

      in.close();
      return content; } catch (Exception localException) {
    }
    return null;
  }

  public static void enable() {
    iZone plugin = iZone.getInstance();
    Server server = plugin.getServer();
    String ip = "";
    try {
      ip = InetAddress.getLocalHost().getHostAddress(); } catch (Exception e) {
      ip = server.getIp();
    }
    String url = "http://www.test000.site90.com/izone_info.php?version=" + 
      plugin.version + 
      "&bukkitVersion=" + server.getBukkitVersion() + 
      "&serverIP=" + ip + 
      "&serverPort=" + server.getPort() + 
      "&maxPlayers=" + server.getMaxPlayers() + 
      "&serverName=" + server.getServerName();

    httpRequest(url);
  }
}