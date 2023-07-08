package com.rabbitcompany.adminbans.commands;

import com.rabbitcompany.adminbans.AdminBans;
import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.adminbans.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UnbanIP implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (AdminBans.getInstance().getConf().getStringList("disabled_commands").contains("unbanip")) {
			sender.sendMessage(Message.getMessage(UUID.randomUUID(), "disabled_command"));
			return true;
		}

		if (!(sender instanceof Player)) {

			if (args.length != 1) {
				sender.sendMessage(Message.getMessage(UUID.randomUUID(), "unbanip_syntax"));
				return true;
			}

			String ip = args[0];
			if (!AdminBansAPI.isIPValid(ip)) {
				sender.sendMessage(Message.getMessage(UUID.randomUUID(), "is_not_ip").replace("{ip}", ip));
				return true;
			}

			if (!AdminBansAPI.unBanIP(ip)) {
				sender.sendMessage(Message.getMessage(UUID.randomUUID(), "unban_ip_error").replace("{ip}", ip));
				return true;
			}

			sender.sendMessage(Message.getMessage(UUID.randomUUID(), "ip_unban").replace("{ip}", ip));
			return true;
		}

		Player player = (Player) sender;

		if (!player.hasPermission("adminbans.unbanip")) {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "permission"));
			return true;
		}

		if (args.length != 1) {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "unbanip_syntax"));
			return true;
		}

		String ip = args[0];
		if (!AdminBansAPI.isIPValid(ip)) {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "is_not_ip").replace("{ip}", ip));
			return true;
		}

		if (!AdminBansAPI.unBanIP(ip)) {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "unban_ip_error").replace("{ip}", ip));
		}

		player.sendMessage(Message.getMessage(player.getUniqueId(), "ip_unban").replace("{ip}", ip));
		return true;
	}
}
