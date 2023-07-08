package com.rabbitcompany.adminbans.commands;

import com.rabbitcompany.adminbans.AdminBans;
import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.adminbans.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Warn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (AdminBans.getInstance().getConf().getStringList("disabled_commands").contains("warn")) {
			sender.sendMessage(Message.getMessage(UUID.randomUUID(), "disabled_command"));
			return true;
		}

		if (!(sender instanceof Player)) {

			if (args.length < 2) {
				sender.sendMessage(Message.getMessage(UUID.randomUUID(), "warn_syntax"));
				return true;
			}

			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage(Message.getMessage(UUID.randomUUID(), "is_not_a_player").replace("{player}", args[0]));
				return true;
			}

			if (!target.isOnline()) {
				sender.sendMessage(Message.getMessage(UUID.randomUUID(), "is_not_online").replace("{player}", target.getName()));
				return true;
			}

			StringBuilder message = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				message.append(args[i]).append(" ");
			}

			Bukkit.broadcastMessage(AdminBansAPI.warnPlayer("Console", "Console", target.getUniqueId().toString(), target.getName(), message.toString()));
			return true;
		}

		Player player = (Player) sender;

		if (!player.hasPermission("adminbans.warn")) {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "permission"));
			return true;
		}

		if (args.length < 2) {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "warn_syntax"));
			return true;
		}

		Player target = Bukkit.getPlayer(args[0]);
		if (target == null) {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "is_not_a_player").replace("{player}", args[0]));
			return true;
		}

		if (target.hasPermission("adminbans.warn.exempt")) {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "player_warn_exempt").replace("{player}", target.getName()));
			return true;
		}

		if (!target.isOnline()) {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "is_not_online").replace("{player}", target.getName()));
			return true;
		}

		StringBuilder message = new StringBuilder();
		for (int i = 1; i < args.length; i++) {
			message.append(args[i]).append(" ");
		}

		Bukkit.broadcastMessage(AdminBansAPI.warnPlayer(player.getUniqueId().toString(), player.getName(), target.getUniqueId().toString(), target.getName(), message.toString()));
		return true;
	}
}
