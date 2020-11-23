package cx.sfy.LagAssist.minebench;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import cx.sfy.LagAssist.Main;
import cx.sfy.LagAssist.packets.Reflection;

public class SpeedTest {

	protected static DecimalFormat df = new DecimalFormat("0.0");
	private static SplittableRandom sr = new SplittableRandom();

	protected static float getDownSpeed() {
		URLConnection ftp;
		try {
			URL ftpdlur = new URL(Main.config.getString("benchmark.download.link"));
			ftp = ftpdlur.openConnection();
			ftp.connect();

			InputStream dloader = ftp.getInputStream();
			BufferedInputStream bis = new BufferedInputStream(dloader);
			
			
			long begin = System.currentTimeMillis();
			
			while (bis.read() != -1) {
				// Waste time while reading.
			}

			float taken = (System.currentTimeMillis() - begin) / 1000.0f;

			dloader.close();

			return 50.0f / taken;
		} catch (IOException e) {
			return -1;
		}

	}

	protected static float getUpSpeed() {
		URLConnection ftp;
		try {
			long dlsize = Main.config.getLong("benchmark.upload.size");

			URL ftpupur = new URL(
					Main.config.getString("benchmark.upload.link").replaceAll("%RND%", UUID.randomUUID().toString()));

			ftp = ftpupur.openConnection();
			ftp.connect();

			OutputStream of = ftp.getOutputStream();
			BufferedOutputStream bof = new BufferedOutputStream(of);
			
			
			long begin = System.currentTimeMillis();
			
			for (int i = 0; i < dlsize; i+=4) {
				int a = sr.nextInt(0, Integer.MAX_VALUE);
				of.write((a >> 0) & 0xff);
				of.write((a >> 8) & 0xff);
				of.write((a >> 16) & 0xff);
				of.write((a >> 24) & 0xff);
			}
			
			bof.flush();
			
			float taken = (System.currentTimeMillis() - begin) / 1000.0f;

			bof.close();
			of.close();
			
			return dlsize / 1000000 / taken;
		} catch (IOException e) {
			return -1;
		}

	}

	public static void pingBenchmark(CommandSender s) {

		List<InetAddress> ips = new ArrayList<InetAddress>();

		int med = 0;
		int nr = 0;
		int max = -1;
		int min = -1;

		String namemax = "";
		String namemin = "";

		for (Player p : Bukkit.getOnlinePlayers()) {
			int pping = Reflection.getPing(p);
			med += pping;
			nr++;
			if (max == -1) {
				max = pping;
				namemax = p.getName();
			} else if (max < pping) {
				max = pping;
				namemax = p.getName();
			}
			if (min == -1) {
				min = pping;
				namemin = p.getName();
			} else if (min > pping) {
				min = pping;
				namemin = p.getName();
			}

			ips.add(p.getAddress().getAddress());
		}

		s.sendMessage("");
		s.sendMessage("§c§l⬛⬛⬛⬛⬛⬛§f§l PINGTEST RESULTS §c§l⬛⬛⬛⬛⬛⬛");
		s.sendMessage("");
		s.sendMessage(" §c✸ §fLowest Ping: §e" + String.valueOf(min) + "ms  §c(" + namemin + ")");
		s.sendMessage(" §c✸ §fHighest Ping: §e" + String.valueOf(max) + "ms  §c(" + namemax + ")");
		s.sendMessage("");
		s.sendMessage(" §c✸ §fAverage Ping: §e" + df.format((double) (med / nr)) + "ms");
		s.sendMessage("");
		s.sendMessage("§c§l⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛");

	}
}
