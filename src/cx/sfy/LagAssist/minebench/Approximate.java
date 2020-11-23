package cx.sfy.LagAssist.minebench;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import cx.sfy.LagAssist.Main;

public class Approximate {

	private static boolean inuse = false;
	private static List<CommandSender> receivers = new ArrayList<CommandSender>();

	public static int approximatePlayers(BenchResponse br) {
		int mem = SpecsGetter.MaxRam();

		int singlescore = br.getSinglethread();
		int multiscore = br.getMultithread();

		if (singlescore == -1 || multiscore == -1) {
			return -1;
		}

		return Math.min(getMaxSTH(singlescore), getMaxMemo(mem));

	}

	private static int getMaxMemo(int mem) {
		int plgmnt = Bukkit.getPluginManager().getPlugins().length;
		int remmem = (int) (mem - plgmnt * 3.2f);
		return remmem / 50;
	}

	private static int getMaxSTH(int sthread) {
		String vers = Bukkit.getVersion();

		int max = 0;

		if (vers.contains("1.8")) {
			max = sthread / 10;
		} else if (vers.contains("1.9")) {
			max = sthread / 13;
		} else if (vers.contains("1.10")) {
			max = sthread / 14;
		} else if (vers.contains("1.11")) {
			max = sthread / 16;
		} else if (vers.contains("1.12")) {
			max = sthread / 18;
		} else if (vers.contains("1.13")) {
			max = sthread / 25;
		} else {
			max = sthread / 37;
		}

		return max;
	}

	public static void showBenchmark(CommandSender s) {
		if (!receivers.contains(s)) {
			receivers.add(s);
		}
		if (inuse) {
			s.sendMessage(
					Main.PREFIX + "You have been added to the receivers list. Please wait for the benchmark to finish.");
			return;
		}
		s.sendMessage(
				Main.PREFIX + "We are getting the benchmark results. This may take a while depending on your download speed. Please wait....");
		Bukkit.getScheduler().runTaskAsynchronously(Main.p, new Runnable() {
			@Override
			public void run() {

				inuse = true;

				BenchResponse br = SpecsGetter.getBenchmark();

				int perthread = approximatePlayers(br);
				int multithread = perthread * SpecsGetter.threadCount();

				String cpuname = SpecsGetter.getCPU(SpecsGetter.getOS());

				String singleapprox = String.valueOf(perthread * 4 / 5) + "-" + String.valueOf(perthread * 6 / 5);
				String multiapprox = String.valueOf(multithread * 4 / 5) + "-" + String.valueOf(multithread * 6 / 5);

				int tries = 0;

				float dspeed;
				float upspeed;

				do {
					dspeed = SpeedTest.getDownSpeed();
					tries++;
				} while (dspeed == -1 && tries < 10);

				do {
					upspeed = SpeedTest.getUpSpeed();
					tries++;
				} while (upspeed == -1 && tries < 10);

				String MBDL = SpeedTest.df.format(dspeed);
				String MIBDL = SpeedTest.df.format(dspeed * 8.0f);
				String MBUP = SpeedTest.df.format(upspeed);
				String MIBUP = SpeedTest.df.format(upspeed * 8.0f);

				Bukkit.getScheduler().runTask(Main.p, new Runnable() {
					@Override
					public void run() {
						for (CommandSender cs : receivers) {
							cs.sendMessage("");
							cs.sendMessage("§c§l⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛§f§l BENCHMARK RESULTS §c§l⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛");
							cs.sendMessage("");
							cs.sendMessage("  §c✸ §fCPU Name: §e" + cpuname);
							cs.sendMessage("");
							cs.sendMessage("  §c✸ §fCPU Score (SINGLE): §e" + br.getStringifiedSth());
							cs.sendMessage("  §c✸ §fCPU Score (MULTI): §e" + br.getStringifiedMth());
							cs.sendMessage("");
							cs.sendMessage("  §c✸ §fDownload Speed: §e" + MIBDL + " Mib/s  (" + MBDL + "MB/s)");
							cs.sendMessage("  §c✸ §fUpload Speed: §e" + MIBUP + " Mib/s  (" + MBUP + " MB/s)");
							cs.sendMessage("");
							cs.sendMessage("  §c✸ §fMax Players (SINGLE): §e" + singleapprox);
							cs.sendMessage("  §c✸ §fMax Players (GLOBAL): §e" + multiapprox);
							cs.sendMessage("");
							cs.sendMessage("§c§l⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛⬛");
						}
						receivers.clear();
						inuse = false;
					}
				});
			}

		});
	}

}
