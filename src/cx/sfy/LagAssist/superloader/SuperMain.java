package cx.sfy.LagAssist.superloader;

import org.bukkit.Bukkit;

import cx.sfy.LagAssist.Main;

public class SuperMain {

	public static void Enabler(boolean reload) {
		if (!Main.config.getBoolean("super-loader.enabled")) {
			return;
		}
		
		Bukkit.getLogger().info("    §e[§a✔§e] §fSuperLoader.");
		
		
	}
	
}
