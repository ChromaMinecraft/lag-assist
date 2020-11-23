package cx.sfy.LagAssist.microfeatures;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.material.PistonBaseMaterial;

import cx.sfy.LagAssist.Main;

@SuppressWarnings("deprecation")
public class MicroManager implements Listener {

	public static void Enabler(boolean reload) {
		Bukkit.getLogger().info("    §e[§a✔§e] §fMicroFeatures.");

		if (!reload) {
			runTask();
		}

		Main.p.getServer().getPluginManager().registerEvents(new MicroManager(), Main.p);
	}

	private static void runTask() {
		Bukkit.getScheduler().runTaskTimer(Main.p, () -> {
			for (BlockState b : breakables) {
				b.getBlock().breakNaturally();
			}
			
			breakables.clear();
			
		}, 5, 5);
	}

	private static List<BlockState> breakables = new ArrayList<>();
	private static final List<BlockFace> growablefaces = Arrays.asList(BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH,
			BlockFace.SOUTH);

	@EventHandler(priority = EventPriority.HIGHEST)
	public void onGrowableGrow(BlockGrowEvent e) {
		if (!Main.config.getBoolean("microfeatures.optimize-growable-farms.enable")) {
			return;
		}

		if (e.isCancelled()) {
			return;
		}

		BlockState b = e.getNewState();

		Material mat = b.getType();

		if (!Main.config.getStringList("microfeatures.optimize-growable-farms.blocks").contains(mat.toString())) {
			return;
		}

		for (BlockFace face : growablefaces) {
			Block piston = b.getBlock().getRelative(face);
			if (piston == null) {
				continue;
			}

			MaterialData mdata = piston.getState().getData();

			if (!(mdata instanceof PistonBaseMaterial)) {
				continue;
			}

			PistonBaseMaterial pdata = (PistonBaseMaterial) mdata;

			if (pdata.getFacing().getOppositeFace() != face) {
				continue;
			}

			breakables.add(b);

			return;
		}

	}

}
