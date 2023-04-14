package com.rabbitcompany.adminbans.commands;

import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.adminbans.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BanIP implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(!(sender instanceof Player)) {
			if(args.length == 1){
				String ip = args[0];
				if(AdminBansAPI.isIPValid(ip)){
					sender.sendMessage(AdminBansAPI.banIP(ip, AdminBansAPI.server_name));
				}else{
					sender.sendMessage(Message.getMessage(UUID.randomUUID(), "is_not_ip").replace("{ip}", ip));
				}
			}else{
				sender.sendMessage(Message.getMessage(UUID.randomUUID(), "banip_syntax"));
			}
			return true;
		}

		Player player = (Player) sender;

		if(player.hasPermission("adminbans.banip")){
			if(args.length == 1){
				String ip = args[0];
				if(AdminBansAPI.isIPValid(ip)){
					player.sendMessage(AdminBansAPI.banIP(ip, AdminBansAPI.server_name));
				}else{
					player.sendMessage(Message.getMessage(player.getUniqueId(), "is_not_ip").replace("{ip}", ip));
				}
			}else{
				player.sendMessage(Message.getMessage(player.getUniqueId(), "banip_syntax"));
			}
		}else{
			player.sendMessage(Message.getMessage(player.getUniqueId(), "permission"));
		}

		return true;
	}

}
