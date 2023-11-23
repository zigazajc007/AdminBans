package com.rabbitcompany.adminbans.commands;

import com.rabbitcompany.adminbans.AdminBans;
import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.adminbans.utils.Message;
import com.rabbitcompany.adminbans.utils.Utils;
import org.apache.commons.lang3.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

public class Mute implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (AdminBans.getInstance().getConf().getStringList("disabled_commands").contains("mute")) {
			sender.sendMessage(Message.getMessage(UUID.randomUUID(), "disabled_command"));
			return true;
		}

		if (!(sender instanceof Player)) {

			if (args.length < 3) {
				sender.sendMessage(Message.getMessage(UUID.randomUUID(), "mute_syntax"));
				return true;
			}

			String str_player = args[0];
			String str_time = args[1];
			StringBuilder reason = new StringBuilder();
			boolean silence = false;

			for (int i = 2; i < args.length; i++) {
				if (!args[i].equals("-s")) {
					reason.append(args[i]).append(" ");
				} else {
					silence = true;
				}
			}

			long mil_year = 31556952000L;
			long mil_month = 2592000000L;
			long mil_day = 86400000L;
			long mil_hour = 3600000L;
			long mil_minute = 60000L;

			if (!NumberUtils.isNumber(Utils.stripNonDigits(str_time))) {
				sender.sendMessage(Message.getMessage(UUID.randomUUID(), "is_not_a_number").replace("{number}", str_time));
				return true;
			}

			int number = Integer.parseInt(Utils.stripNonDigits(str_time));
			long time = 0L;

			if (str_time.contains("min") || str_time.contains("minute") || str_time.contains("minutes")) {
				time = mil_minute;
			} else if (str_time.contains("h") || str_time.contains("hour") || str_time.contains("hours")) {
				time = mil_hour;
			} else if (str_time.contains("d") || str_time.contains("day") || str_time.contains("days")) {
				time = mil_day;
			} else if (str_time.contains("m") || str_time.contains("month") || str_time.contains("months")) {
				time = mil_month;
			} else if (str_time.contains("y") || str_time.contains("year") || str_time.contains("years")) {
				time = mil_year;
			}

			Date until = new Date(System.currentTimeMillis() + (number * time));

			if (silence) {
				sender.sendMessage(AdminBansAPI.mutePlayer("Console", "Console", Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, reason.toString(), AdminBansAPI.date_format.format(until)));
				return true;
			}

			Bukkit.broadcastMessage(AdminBansAPI.mutePlayer("Console", "Console", Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, reason.toString(), AdminBansAPI.date_format.format(until)));
			return true;
		}

		Player player = (Player) sender;

		if (!player.hasPermission("adminbans.mute")) {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "permission"));
			return true;
		}

		if (args.length < 3) {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "mute_syntax"));
			return true;
		}

		String str_player = args[0];
		String str_time = args[1];

		Player target_player = Bukkit.getPlayer(str_player);
		if (target_player != null) {
			if (target_player.hasPermission("adminbans.mute.exempt")) {
				player.sendMessage(Message.getMessage(player.getUniqueId(), "player_mute_exempt").replace("{player}", target_player.getName()));
				return true;
			}
		}

		StringBuilder reason = new StringBuilder();
		boolean silence = false;

		for (int i = 2; i < args.length; i++) {
			if (!args[i].equals("-s")) {
				reason.append(args[i]).append(" ");
			} else {
				silence = true;
			}
		}

		long mil_year = 31556952000L;
		long mil_month = 2592000000L;
		long mil_day = 86400000L;
		long mil_hour = 3600000L;
		long mil_minute = 60000L;

		if (!NumberUtils.isNumber(Utils.stripNonDigits(str_time))) {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "is_not_a_number").replace("{number}", str_time));
			return true;
		}

		int number = Integer.parseInt(Utils.stripNonDigits(str_time));
		long time = 0L;

		if (str_time.contains("min") || str_time.contains("minute") || str_time.contains("minutes")) {
			time = mil_minute;
		} else if (str_time.contains("h") || str_time.contains("hour") || str_time.contains("hours")) {
			time = mil_hour;
		} else if (str_time.contains("d") || str_time.contains("day") || str_time.contains("days")) {
			time = mil_day;
		} else if (str_time.contains("m") || str_time.contains("month") || str_time.contains("months")) {
			time = mil_month;
		} else if (str_time.contains("y") || str_time.contains("year") || str_time.contains("years")) {
			time = mil_year;
		}

		Date until = new Date(System.currentTimeMillis() + (number * time));

		if (silence) {
			player.sendMessage(AdminBansAPI.mutePlayer(player.getUniqueId().toString(), player.getName(), Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, reason.toString(), AdminBansAPI.date_format.format(until)));
			return true;
		}

		Bukkit.broadcastMessage(AdminBansAPI.mutePlayer(player.getUniqueId().toString(), player.getName(), Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, reason.toString(), AdminBansAPI.date_format.format(until)));
		return true;
	}
}
