package com.rabbitcompany.adminbans;

import com.rabbitcompany.adminbans.utils.BannedIP;
import com.rabbitcompany.adminbans.utils.BannedPlayer;
import com.rabbitcompany.adminbans.utils.MutedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabCompletion implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

		if (command.getName().equalsIgnoreCase("unban")) {
			List<String> completions = new ArrayList<>();

			if (args.length == 1) {
				ArrayList<BannedPlayer> bp = AdminBansAPI.getBannedPlayers();

				for (BannedPlayer bannedPlayer : bp) {
					completions.add(bannedPlayer.username_to);
				}
			}
			return completions;
		} else if (command.getName().equalsIgnoreCase("ban")) {
			List<String> completions = new ArrayList<>();

			if (args.length == 1) {

				for (Player all : Bukkit.getServer().getOnlinePlayers()) {
					completions.add(all.getName());
				}

			} else if (args.length == 2) {

				List<?> times = AdminBans.getInstance().getConf().getList("times");
				if (times != null) {
					for (Object time : times) {
						completions.add(time.toString());
					}
				} else {
					//Minutes
					completions.add("15min");
					completions.add("30min");

					//Hours
					completions.add("1h");
					completions.add("3h");
					completions.add("5h");

					//Days
					completions.add("1d");
					completions.add("5d");
					completions.add("10d");

					//Months
					completions.add("1m");
					completions.add("3m");
					completions.add("10m");

					//Years
					completions.add("1y");
					completions.add("3y");
					completions.add("10y");
				}
			} else if (args.length == 3) {
				List<?> reasons = AdminBans.getInstance().getConf().getList("reasons");
				if (reasons != null) {
					for (Object reason : reasons) {
						completions.add(reason.toString());
					}
				} else {
					completions.add("Advertising");
					completions.add("Hacking");
					completions.add("Swearing");
					completions.add("Griefing");
					completions.add("Spamming");
				}
			}

			return completions;
		} else if (command.getName().equalsIgnoreCase("kick") || command.getName().equalsIgnoreCase("warn")) {
			List<String> completions = new ArrayList<>();

			if (args.length == 1) {

				for (Player all : Bukkit.getServer().getOnlinePlayers()) {
					completions.add(all.getName());
				}

			} else if (args.length == 2) {
				List<?> reasons = AdminBans.getInstance().getConf().getList("reasons");
				if (reasons != null) {
					for (Object reason : reasons) {
						completions.add(reason.toString());
					}
				} else {
					completions.add("Advertising");
					completions.add("Hacking");
					completions.add("Swearing");
					completions.add("Griefing");
					completions.add("Spamming");
				}
			}

			return completions;
		} else if (command.getName().equalsIgnoreCase("mute")) {
			List<String> completions = new ArrayList<>();

			if (args.length == 1) {

				for (Player all : Bukkit.getServer().getOnlinePlayers()) {
					completions.add(all.getName());
				}

			} else if (args.length == 2) {

				List<?> times = AdminBans.getInstance().getConf().getList("times");
				if (times != null) {
					for (Object time : times) {
						completions.add(time.toString());
					}
				} else {
					//Minutes
					completions.add("15min");
					completions.add("30min");

					//Hours
					completions.add("1h");
					completions.add("3h");
					completions.add("5h");

					//Days
					completions.add("1d");
					completions.add("5d");
					completions.add("10d");

					//Months
					completions.add("1m");
					completions.add("3m");
					completions.add("10m");

					//Years
					completions.add("1y");
					completions.add("3y");
					completions.add("10y");
				}

			} else if (args.length == 3) {
				List<?> reasons = AdminBans.getInstance().getConf().getList("reasons");
				if (reasons != null) {
					for (Object reason : reasons) {
						completions.add(reason.toString());
					}
				} else {
					completions.add("Advertising");
					completions.add("Hacking");
					completions.add("Swearing");
					completions.add("Griefing");
					completions.add("Spamming");
				}
			}

			return completions;
		} else if (command.getName().equalsIgnoreCase("unmute")) {
			List<String> completions = new ArrayList<>();

			if (args.length == 1) {
				ArrayList<MutedPlayer> bp = AdminBansAPI.getMutedPlayers();

				for (MutedPlayer mutedPlayers : bp) {
					completions.add(mutedPlayers.username_to);
				}
			}
			return completions;
		} else if (command.getName().equalsIgnoreCase("unbanip")) {
			List<String> completions = new ArrayList<>();

			if (args.length == 1) {
				ArrayList<BannedIP> bp = AdminBansAPI.getBannedIPs();

				for (BannedIP bannedIP : bp) {
					completions.add(bannedIP.ip);
				}
			}
			return completions;
		} else if (command.getName().equalsIgnoreCase("adminbans") || command.getName().equalsIgnoreCase("abans") || command.getName().equalsIgnoreCase("ab")) {
			List<String> completions = new ArrayList<>();

			if (args.length == 1) {
				completions.add("help");
				completions.add("reload");
			}
			return completions;
		}

		return null;
	}
}
