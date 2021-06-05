package cx.sfy.LagAssist.client;

import java.util.UUID;

import cx.sfy.LagAssist.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import cx.sfy.LagAssist.gui.ClientGUI;
import cx.sfy.LagAssist.gui.ClientGUI.ToggleState;
import cx.sfy.LagAssist.packets.Reflection;
import cx.sfy.LagAssist.utils.VersionMgr;
import cx.sfy.LagAssist.utils.WorldMgr;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class ClientPacket {

	public static boolean hidePacket(Player p, ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
		if (!ClientMain.enabled) {
			return false;
		}
		if (WorldMgr.isBlacklisted(p.getWorld())) {
			return false;
		}
		try {
			String name = msg.getClass().getSimpleName().toLowerCase();
			if (name.equals("packetplayoutspawnentity")
					&& (ClientGUI.isOn(ToggleState.TNT, p) || ClientGUI.isOn(ToggleState.SAND, p))) {

				Entity ent;

				if (VersionMgr.isV1_8()) {
					int x = ((int) Reflection.getFieldValue(msg, "b")) / 32;
					int y = ((int) Reflection.getFieldValue(msg, "c")) / 32;
					int z = ((int) Reflection.getFieldValue(msg, "d")) / 32;
					Location loc = new Location(p.getWorld(), x, y, z);
					ent = Bukkit.getScheduler().callSyncMethod(Main.p, () -> Reflection.getEntity(loc)).get();
				} else if (VersionMgr.isV1_13() || VersionMgr.isV1_14()) {
					UUID u = (UUID) Reflection.getFieldValue(msg, "b");
					ent = Bukkit.getScheduler().callSyncMethod(Main.p, () -> Bukkit.getEntity(u)).get();
				} else {
					double x = ((double) Reflection.getFieldValue(msg, "c"));
					double y = ((double) Reflection.getFieldValue(msg, "d"));
					double z = ((double) Reflection.getFieldValue(msg, "e"));
					Location loc = new Location(p.getWorld(), x, y, z);
					ent = Bukkit.getScheduler().callSyncMethod(Main.p, () -> Reflection.getEntity(loc)).get();
				}

				if (ent == null) {
					return false;
				}

				String type = ent.getType().toString();
				if (type == "PRIMED_TNT") {
					return ClientGUI.isOn(ToggleState.TNT, p);
				} else if (type == "FALLING_BLOCK") {
					return ClientGUI.isOn(ToggleState.SAND, p);
				}
			} else if (name.equals("packetplayoutworldparticles")) {
				return ClientGUI.isOn(ToggleState.PARTICLES, p);
			} else if (name.equals("packetplayoutblockaction")) {
				Object block = Reflection.getFieldValue(msg, "d");
				String type = block.getClass().getSimpleName().toLowerCase();
				if (type.equals("blockpiston")) {
					return ClientGUI.isOn(ToggleState.PISTONS, p);
				}
			} else if (name.equals("packetplayoutentitystatus")) {
				// int ent = (int) Reflection.getFieldValue(msg, "a");
				byte status = (byte) Reflection.getFieldValue(msg, "b");

				if (status == 3) {
					return true;
				}
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

}
