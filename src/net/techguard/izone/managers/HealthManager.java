package net.techguard.izone.managers;

import net.techguard.izone.Properties;
import net.techguard.izone.iZone;
import net.techguard.izone.zones.Flags;
import net.techguard.izone.zones.Zone;
import org.bukkit.entity.Player;

public class HealthManager
{
  int healing = 0;
  int hurting = 0;

  public HealthManager(iZone plugin) {
    plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {
      public void run() {
        HealthManager.this.tick(iZone.getInstance());
      }
    }
    , 200L, 10L);
    this.healing = Properties.healTime;
    this.hurting = Properties.healTime;
  }

  private void tick(iZone plugin) {
    if (--this.healing < 0) this.healing = Properties.healTime;
    if (--this.hurting < 0) this.hurting = Properties.hurtTime;
    for (Player p2 : plugin.getServer().getOnlinePlayers()) {
      Zone zone = ZoneManager.getZone(p2.getLocation());
      if (zone != null) {
        if ((this.healing == 0) && (zone.hasFlag(Flags.HEAL))) {
          int health = p2.getHealth() + Properties.healAmount;
          if (health < 0) health = 0;
          p2.setHealth(health > 20 ? 20 : health);
        }
        if ((this.hurting == 0) && (zone.hasFlag(Flags.HURT))) {
          int health = p2.getHealth() - Properties.hurtAmount;
          if (health > 20) health = 20;
          p2.setHealth(health < 0 ? 0 : health);
        }
      }
    }
  }
}