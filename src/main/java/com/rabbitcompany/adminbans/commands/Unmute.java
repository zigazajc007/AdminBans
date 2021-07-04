package com.rabbitcompany.adminbans.commands;

import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.adminbans.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Unmute implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            if(args.length != 1){
                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "unmute_syntax"));
            }else{
                String str_player = args[0];
                if(AdminBansAPI.isPlayerMuted(str_player)){
                    AdminBansAPI.unMutePlayer(str_player);
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "player_unmute").replace("{player}", str_player));
                }else{
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "is_not_muted").replace("{player}", str_player));
                }
            }
            return true;
        }

        Player player = (Player) sender;

        if(player.hasPermission("adminbans.unmute")){
            if(args.length != 1){
                player.sendMessage(Message.getMessage(player.getUniqueId(), "unmute_syntax"));
            }else{
                String str_player = args[0];
                if(AdminBansAPI.isPlayerMuted(str_player)){
                    AdminBansAPI.unMutePlayer(str_player);
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "player_unmute").replace("{player}", str_player));
                }else{
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "is_not_muted").replace("{player}", str_player));
                }
            }
        }else{
            player.sendMessage(Message.getMessage(player.getUniqueId(), "permission"));
        }

        return true;
    }
}
