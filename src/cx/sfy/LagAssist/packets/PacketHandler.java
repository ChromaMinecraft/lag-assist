package cx.sfy.LagAssist.packets;

import org.bukkit.entity.Player;

import cx.sfy.LagAssist.client.ClientPacket;
import cx.sfy.LagAssist.safety.SafetyAnticrash;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class PacketHandler extends ChannelDuplexHandler {
	private Player p;

	public PacketHandler(final Player p) {
		this.p = p;
	}

	// OUTPUT MANGLER
	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		if (SafetyAnticrash.isDropped(ctx.channel())) {
			return;
		}
		
		if (ClientPacket.hidePacket(p, ctx, msg, promise)) {
			return;
		}
		super.write(ctx, msg, promise);
	}

	// INPUT MANGLER
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (SafetyAnticrash.isDropped(ctx.channel())) {
			return;
		}
		
		if (SafetyAnticrash.isBlocked(p, msg, ctx.channel())) {
			return;
		}
		
		super.channelRead(ctx, msg);
	}
}
