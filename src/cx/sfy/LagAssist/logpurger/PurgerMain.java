package cx.sfy.LagAssist.logpurger;

import java.io.File;

import org.bukkit.Bukkit;

import cx.sfy.LagAssist.Main;
import cx.sfy.LagAssist.utils.MathUtils;

public class PurgerMain {

	public static void Enabler() {
		if (!Main.config.getBoolean("logcleaner.enabled")) {
			return;
		}

		for (String directories : Main.config.getStringList("logcleaner.list")) {
			File f = new File(directories);
			if (f.exists() && f.isDirectory()) {
				File[] fls = f.listFiles();
				for (File fl : fls) {
					if (isOld(fl) && fl.canWrite()) {
						fl.delete();
					}
				}

			}
		}

		Bukkit.getLogger().info("    §e[§a✔§e] §fLogCleaner System.");

	}

	public static boolean isOld(File fl) {
		int maxsize = Main.config.getInt("logcleaner.conditions.maxsize");
		int maxage = Main.config.getInt("logcleaner.conditions.maxage");

		boolean sizereq = (MathUtils.toMegaByte(fl.length()) > maxsize);
		boolean agereq = (((System.currentTimeMillis() - fl.lastModified()) / 86400000) > maxage);

		return (sizereq || agereq);
	}

}
