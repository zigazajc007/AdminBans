package com.rabbitcompany.adminbans.commands;

import com.rabbitcompany.adminbans.AdminBans;
import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.adminbans.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Unban implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (AdminBans.getInstance().getConf().getStringList("disabled_commands").contains("unban")) {
			sender.sendMessage(Message.getMessage(UUID.randomUUID(), "disabled_command"));
			return true;
		}

		if (!(sender instanceof Player)) {
			if (args.length != 1) {
				sender.sendMessage(Message.getMessage(UUID.randomUUID(), "unban_syntax"));
			} else {
				String str_player = args[0];
				if (AdminBansAPI.isPlayerBanned(str_player)) {
					AdminBansAPI.unBanPlayer(str_player);
					sender.sendMessage(Message.getMessage(UUID.randomUUID(), "player_unban").replace("{player}", str_player));
				} else {
					sender.sendMessage(Message.getMessage(UUID.randomUUID(), "is_not_banned").replace("{player}", str_player));
				}
			}
			return true;
		}

		Player player = (Player) sender;

		if (player.hasPermission("adminbans.unban")) {
			if (args.length != 1) {
				player.sendMessage(Message.getMessage(player.getUniqueId(), "unban_syntax"));
			} else {
				String str_player = args[0];
				if (AdminBansAPI.isPlayerBanned(str_player)) {
					AdminBansAPI.unBanPlayer(str_player);
					player.sendMessage(Message.getMessage(player.getUniqueId(), "player_unban").replace("{player}", str_player));
				} else {
					player.sendMessage(Message.getMessage(player.getUniqueId(), "is_not_banned").replace("{player}", str_player));
				}
			}
		} else {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "permission"));
		}

		return true;
	}
}
