package cx.sfy.LagAssist;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import cx.sfy.LagAssist.api.APIManager;
import cx.sfy.LagAssist.chunks.ChkAnalyse;
import cx.sfy.LagAssist.chunks.ChkLimiter;
import cx.sfy.LagAssist.chunks.DynViewer;
import cx.sfy.LagAssist.client.ClientMain;
import cx.sfy.LagAssist.cmd.CommandListener;
import cx.sfy.LagAssist.cmd.CommandTabListener;
import cx.sfy.LagAssist.cmd.StatsAnalyse;
import cx.sfy.LagAssist.economy.EconomyManager;
import cx.sfy.LagAssist.gui.DataGUI;
import cx.sfy.LagAssist.hoppers.HopperManager;
import cx.sfy.LagAssist.logpurger.PurgerMain;
import cx.sfy.LagAssist.metrics.MetricsManager;
import cx.sfy.LagAssist.microfeatures.MicroManager;
import cx.sfy.LagAssist.mobs.SmartMob;
import cx.sfy.LagAssist.mobs.SpawnerMgr;
import cx.sfy.LagAssist.packets.PacketInjector;
import cx.sfy.LagAssist.packets.PacketMain;
import cx.sfy.LagAssist.packets.Reflection;
import cx.sfy.LagAssist.safety.SafetyManager;
import cx.sfy.LagAssist.stacker.StackManager;
import cx.sfy.LagAssist.updater.SmartUpdater;
import cx.sfy.LagAssist.utils.Others;
import cx.sfy.LagAssist.utils.VersionMgr;
import cx.sfy.LagAssist.utils.WorldMgr;

public class Main extends JavaPlugin implements Listener {

	public static String USER = "%%__USER__%%";

	public static final String PREFIX = "§c§lLag§f§lAssist §e» §f";

	public static JavaPlugin p;
	public static boolean paper = false;

	// Debug mode means people will get verbose info.
	public static int debug = 0;

	private static File file;
	public static FileConfiguration config = new YamlConfiguration();

	@Override
	public void onEnable() {
		p = this;

		file = new File(getDataFolder(), "server.yml");
		config = Others.getConfig(file, 29);

		paper = VersionMgr.isPaper();

		// Start Smart updater to check for updates.
		SmartUpdater.Enabler();

		Bukkit.getLogger().info(Main.PREFIX + "Enabling Systems:");
		EnableClasses(false);

		getServer().getPluginManager().registerEvents(this, this);
		getCommand("lagassist").setExecutor(new CommandListener());
		getCommand("lagassist").setTabCompleter(new CommandTabListener());
	}

	private static void EnableClasses(boolean reload) {

		EconomyManager.Enabler(reload);

		SafetyManager.Enabler(reload);
		Reflection.Enabler();
		Data.Enabler();
		SmartMob.Enabler(reload);
		MicroManager.Enabler(reload);
		HopperManager.Enabler(reload);
		StackManager.Enabler(reload);
		Redstone.Enabler(reload);
		Physics.Enabler(reload);
		Monitor.Enabler(reload);
		MonTools.Enabler(reload);
		WorldMgr.Enabler();
		ChkAnalyse.Enabler();
		ChkLimiter.Enabler(reload);
		StatsAnalyse.Enabler(reload);
		PurgerMain.Enabler();

		SpawnerMgr.Enabler(reload);
		DynViewer.Enabler(reload);
		ClientMain.Enabler();
		DataGUI.Enabler(reload);

		MetricsManager.Enabler(reload);

		PacketMain.Enabler(reload);

		// API ICON
		APIManager.Enabler(reload);
	}

	public static void ReloadPlugin(CommandSender s) {
		config = Others.getConfig(file, 29);

		Bukkit.getLogger().info(Main.PREFIX + "Reloading Systems:");
		EnableClasses(true);

		s.sendMessage(Main.PREFIX + "Reloaded the config successfully.");
	}

	@Override
	public void onDisable() {
		if (PacketMain.isPacketEnabled()) {
			PacketInjector.Disabler();
		}
		StackManager.Disabler();
	}

	public static void sendDebug(String msg, int mindebug) {
		if (mindebug > debug) {
			return;
		}

		for (Player p : Bukkit.getOnlinePlayers()) {
			if (!p.hasPermission("lagassist.debug")) {
				continue;
			}

			p.sendMessage(msg + "(MINDEBUG: " + mindebug + ")");
		}

		if (debug == 3) {
			try {
				throw new Exception(msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
	}

}
