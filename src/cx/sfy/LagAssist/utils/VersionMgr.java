package cx.sfy.LagAssist.utils;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import cx.sfy.LagAssist.packets.Reflection;

@SuppressWarnings("deprecation")
public class VersionMgr {

	public static ItemStack getMap() {
		return (isNewMaterials() ? V1_13.getLagMap() : V1_12.getLagMap());
	}

	public static int getMapId(ItemStack s) {
		return (isNewMaterials()) ? (int) V1_13.getMapId(s) : V1_12.getMapId(s);
	}

	public static ItemStack[] getStatics() {
		if (VersionMgr.isNewMaterials()) {
			return V1_13.getStatics();
		} else {
			return V1_12.getStatics();
		}
	}

	public static boolean isV1_8() {
		return Bukkit.getVersion().contains("1.8");
	}
	
	public static boolean isV1_9() {
		return Bukkit.getVersion().contains("1.9");
	}
	
	public static boolean isV1_10() {
		return Bukkit.getVersion().contains("1.10");
	}

	public static boolean isV1_11() {
		return Bukkit.getVersion().contains("1.11");
	}
	
	public static boolean isV1_12() {
		return Bukkit.getVersion().contains("1.12");
	}
	
	public static boolean isV1_13() {
		return Bukkit.getVersion().contains("1.13");
	}

	public static boolean isV1_14() {
		return Bukkit.getVersion().contains("1.14");
	}
	
	public static boolean isNewMaterials() {
		if (isV1_8()) {
			return false;
		}
		
		if (isV1_9()) {
			return false;
		}
		
		if (isV1_10()) {
			return false;
		}
		
		if (isV1_11()) {
			return false;
		}
		
		if (isV1_12()) {
			return false;
		}
		
		return true;
		
	}

	public static boolean isPaper() {
		try {
			Class.forName("com.destroystokyo.paper.PaperConfig");
		} catch (ClassNotFoundException e) {
			return false;
		}
		return true;
	}

	public static String ChunkExistsName() {
		return isNewMaterials() ? "f" : "e";
	}

//	private static HashSet<EntityType> exceptions = Sets.newHashSet(EntityType.ARMOR_STAND, EntityType.VILLAGER, EntityType.ENDER_DRAGON, EntityType.ITEM_FRAME, EntityType.PAINTING);
//	private static SplittableRandom sr = new SplittableRandom();

	public static void loadChunk(World world, int x, int z) {

		Chunk chk = world.getChunkAt(x, z);

//		for (Entity e : chk.getEntities()) {
//			if (e.getCustomName() != null) {
//				continue;
//			}
//			if (exceptions.contains(e.getType())) {
//				continue;
//			}
//			if (sr.nextInt(100) > 75) {
//				continue;
//			}
//			e.remove();
//		}
//		
		chk.unload();
	}

	public static Object setUnbreakable(ItemMeta imeta, boolean unbreakable) {
		return (isNewMaterials() || isV1_12()) ? V1_13.setUnbreakable(imeta, unbreakable) : V1_8.setUnbreakable(imeta, unbreakable);
	}
	
	public static boolean isUnbreakable(ItemMeta imeta) {
		return (isNewMaterials() || isV1_12()) ? V1_13.isUnbreakable(imeta) : V1_8.isUnbreakable(imeta);
	}
	
	public static boolean isChunkGenerated(World world, Object provider, int x, int z) {
		return isNewMaterials() ? V1_13.isChunkGenerated(world, x, z) : Reflection.isChunkExistent(provider, x, z);
	}
	
	
	public static boolean hasPassengers(Entity ent) {
		if (isV1_8()) {
			return ent.getPassenger() != null;
		} else {
			return !ent.getPassengers().isEmpty();
		}
	}
	
	public static double getMaxHealth(LivingEntity ent) {
		if (isV1_8()) {
			return ent.getMaxHealth();
		} else {
			return ent.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
		}
	}

}
