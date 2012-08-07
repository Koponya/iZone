package net.techguard.izone.listeners;

import net.techguard.izone.managers.ZoneManager;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Zone;
import org.bukkit.entity.LightningStrike;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.LightningStrikeEvent;

public class wListener
  implements Listener
{
  @EventHandler(priority=EventPriority.NORMAL, ignoreCancelled=true)
  public void onLightningStrike(LightningStrikeEvent ev)
  {
    LightningStrike strike = ev.getLightning();
    Zone zone = ZoneManager.getZone(strike.getLocation());

    if ((zone != null) && (zone.hasFlag(Flags.LIGHTNING)))
      ev.setCancelled(true);
  }
}