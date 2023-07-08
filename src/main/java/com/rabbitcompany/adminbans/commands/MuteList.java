package com.rabbitcompany.adminbans.commands;

import com.rabbitcompany.adminbans.AdminBans;
import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.adminbans.utils.Message;
import com.rabbitcompany.adminbans.utils.MutedPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class MuteList implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (AdminBans.getInstance().getConf().getStringList("disabled_commands").contains("mutelist")) {
			sender.sendMessage(Message.getMessage(UUID.randomUUID(), "disabled_command"));
			return true;
		}

		if (!(sender instanceof Player)) {
			ArrayList<MutedPlayer> mutedPlayers = AdminBansAPI.getMutedPlayers();
			sender.sendMessage(Message.getMessage(UUID.randomUUID(), "muted_players"));
			for (MutedPlayer mutedPlayer : mutedPlayers) {
				sender.sendMessage(Message.chat("&a- &c" + mutedPlayer.username_to));
			}
			return true;
		}

		Player player = (Player) sender;

		if (!player.hasPermission("adminbans.mutelist")) {
			player.sendMessage(Message.getMessage(player.getUniqueId(), "permission"));
			return true;
		}

		ArrayList<MutedPlayer> mutedPlayers = AdminBansAPI.getMutedPlayers();
		player.sendMessage(Message.getMessage(player.getUniqueId(), "muted_players"));
		for (MutedPlayer mutedPlayer : mutedPlayers) {
			player.sendMessage(Message.chat("&a- &c" + mutedPlayer.username_to));
		}
		return true;
	}
}
