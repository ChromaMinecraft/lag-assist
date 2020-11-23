package cx.sfy.LagAssist.hoppers;

import java.util.SplittableRandom;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Hopper;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.inventory.InventoryHolder;

import cx.sfy.LagAssist.Main;
import cx.sfy.LagAssist.utils.V1_12;
import cx.sfy.LagAssist.utils.V1_8;
import cx.sfy.LagAssist.utils.VersionMgr;
import cx.sfy.LagAssist.utils.WorldMgr;

public class HopperManager implements Listener {

	private static SplittableRandom r = new SplittableRandom();

	public static boolean chunkhoppers;
	public static boolean denyhoppers = false;

	public static void Enabler(boolean reload) {
		denyhoppers = Main.config.getBoolean("hopper-check.enabled");
		chunkhoppers = Main.config.getBoolean("hopper-check.chunk-hoppers.enabled");

		if (!reload) {
			Main.p.getServer().getPluginManager().registerEvents(new HopperManager(), Main.p);
		}

		if (chunkhoppers) {
			ChunkHoppers.Enabler(reload);
		}

		Bukkit.getLogger().info("    §e[§a✔§e] §fHopper Manager.");
	}

	@EventHandler
	public void disableCraft(CraftItemEvent e) {
		if (e.getRecipe().getResult().getType().equals(Material.HOPPER) && denyhoppers == true) {
			e.setCancelled(true);
			for (HumanEntity human : e.getViewers())
				if (human instanceof Player) {
					Player p = Bukkit.getPlayer(human.getName());
					p.sendMessage(
							ChatColor.translateAlternateColorCodes('&', Main.config.getString("hopper-check.reason")));
				}

		}
	}

	@EventHandler
	public void hopperBoom(InventoryMoveItemEvent e) {
		InventoryHolder holder = e.getInitiator().getHolder();
		
		if (!(holder instanceof Hopper)) {
			return;
		}
		Hopper h = (Hopper) holder;
		
		if (WorldMgr.blacklist.contains(h.getWorld().getName())) {
			return;
		}

		if (Main.config.getInt("hopper-check.chance") <= 0) {
			return;
		}

		String stg = VersionMgr.isV1_8() ? V1_8.getHopperName(h) : V1_12.getHopperName(h);

		if (!(stg.equalsIgnoreCase("container.hopper"))) {
			return;
		}

		int rand = r.nextInt(10000 - 1) + 1;
		int chance = Main.config.getInt("hopper-check.chance");
		Hopper hopp = (Hopper) e.getInitiator().getHolder();
		if (rand <= chance) {
			hopp.getBlock().setType(Material.STONE);
		}

	}

}
