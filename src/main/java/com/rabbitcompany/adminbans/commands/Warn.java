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

			boolean silence = false;
			StringBuilder message = new StringBuilder();
			for (int i = 1; i < args.length; i++) {
				if (!args[i].equals("-s")) {
					message.append(args[i]).append(" ");
				}else{
					silence = true;
				}
			}

			String str_player = args[0];

			if(silence){
				sender.sendMessage(AdminBansAPI.warnPlayer("Console", "Console", Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, message.toString()));
				return true;
			}

			Bukkit.broadcastMessage(AdminBansAPI.warnPlayer("Console", "Console", Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, message.toString()));
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

		String str_player = args[0];

		Player target = Bukkit.getPlayer(str_player);
		if (target != null && target.hasPermission("adminbans.warn.exempt")) {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "player_warn_exempt").replace("{player}", str_player));
			return true;
		}
		
		boolean silence = false;
		StringBuilder message = new StringBuilder();
		for (int i = 1; i < args.length; i++) {
			if (!args[i].equals("-s")) {
				message.append(args[i]).append(" ");
			}else{
				silence = true;
			}
		}

		if(silence){
			player.sendMessage(AdminBansAPI.warnPlayer(player.getUniqueId().toString(), player.getName(), Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, message.toString()));
			return true;
		}

		Bukkit.broadcastMessage(AdminBansAPI.warnPlayer(player.getUniqueId().toString(), player.getName(), Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, message.toString()));
		return true;
	}
}
