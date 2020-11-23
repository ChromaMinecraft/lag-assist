package cx.sfy.LagAssist.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.bukkit.block.Hopper;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import cx.sfy.LagAssist.Main;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;

@SuppressWarnings("deprecation")
public class V1_8 {

	private static Method hoppername = null;
	private static boolean checked = false;

	public static void sendActionbar(Player player, String s) {
		IChatBaseComponent cbc = ChatSerializer.a("{\"text\": \"" + s + "\"}");
		PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte) 2);
		CraftPlayer cfp = (CraftPlayer) player;
		cfp.getHandle().playerConnection.sendPacket(ppoc);
	}

	public static String getHopperName(Hopper h) {

		Class<?> hoppers = h.getClass();

		if (checked == false && hoppername == null) {
			checked = true;
			try {
				hoppername = hoppers.getMethod("getCustomName");
			} catch (Exception e) {
				Main.sendDebug("Hopper getCustomName not found! Falling back to getInventory().getName()", 1);
			}
		}

		if (hoppername == null) {
			return h.getInventory().getName();
		}

		try {
			return (String) hoppername.invoke(h);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			return "container.hopper";
		}
	}

	public static Object setUnbreakable(ItemMeta imeta, boolean unbreakable) {
		imeta.spigot().setUnbreakable(unbreakable);

		return null;
	}

	public static boolean isUnbreakable(ItemMeta imeta) {
		return imeta.spigot().isUnbreakable();
	}

}
