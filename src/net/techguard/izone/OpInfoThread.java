package net.techguard.izone;

import net.techguard.izone.managers.UpdateManager;

import org.bukkit.entity.Player;

public class OpInfoThread extends Thread {

	Player player;
	public OpInfoThread(Player p) {
		this.player = p;
	}
	
	@Override
	public void run() {
		this.player.sendMessage("§2===> §6iZone Updated§2 <===");
		this.player.sendMessage("§6Current Version: §a" + UpdateManager.CURRENT_VERSION);
		this.player.sendMessage("§6Latest Version:   §a" + UpdateManager.LATEST_VERSION);
		this.player.sendMessage("§6Reason: §a" + UpdateManager.REASON);
	}
	
}
