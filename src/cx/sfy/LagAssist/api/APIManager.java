package cx.sfy.LagAssist.api;

import org.bukkit.Bukkit;

import cx.sfy.LagAssist.Main;

public class APIManager {
	
	
	public static void Enabler(boolean reload) {
		Bukkit.getLogger().info("    §e[§a✔§e] §fAPI Tools.");
		
		if (!reload) {
			Main.p.getServer().getPluginManager().registerEvents(new MotdAPI(), Main.p);
		}
	}
}
