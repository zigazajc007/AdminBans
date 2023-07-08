package com.rabbitcompany.adminbans.commands;

import com.rabbitcompany.adminbans.AdminBans;
import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.adminbans.utils.BannedPlayer;
import com.rabbitcompany.adminbans.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class BanList implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (AdminBans.getInstance().getConf().getStringList("disabled_commands").contains("banlist")) {
			sender.sendMessage(Message.getMessage(UUID.randomUUID(), "disabled_command"));
			return true;
		}

		if (!(sender instanceof Player)) {
			ArrayList<BannedPlayer> bannedPlayers = AdminBansAPI.getBannedPlayers();
			sender.sendMessage(Message.getMessage(UUID.randomUUID(), "banned_players"));
			for (BannedPlayer bannedPlayer : bannedPlayers) {
				sender.sendMessage(Message.chat("&a- &c" + bannedPlayer.username_to));
			}
			return true;
		}

		Player player = (Player) sender;

		if (!player.hasPermission("adminbans.banlist")) {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "permission"));
			return true;
		}

		ArrayList<BannedPlayer> bannedPlayers = AdminBansAPI.getBannedPlayers();
		player.sendMessage(Message.getMessage(player.getUniqueId(), "banned_players"));
		for (BannedPlayer bannedPlayer : bannedPlayers) {
			player.sendMessage(Message.chat("&a- &c" + bannedPlayer.username_to));
		}

		return true;
	}
}
