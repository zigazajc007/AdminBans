package com.rabbitcompany.adminbans.commands;

import com.rabbitcompany.adminbans.utils.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AdminBans implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {

            if(args.length == 1){
                switch (args[0]){
                    case "help":
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "commands"));
                        break;
                    case "reload":
                        com.rabbitcompany.adminbans.AdminBans.getInstance().loadYamls();
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "reload"));
                        break;
                    default:
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "adminbans_syntax"));
                        break;
                }
            }else{
                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "adminbans_syntax"));
            }

            return true;
        }

        Player player = (Player) sender;

        if(player.hasPermission("adminbans")){
            if(args.length == 1){
                switch (args[0]){
                    case "help":
                        sender.sendMessage( Message.getMessage(UUID.randomUUID(), "commands"));
                        break;
                    case "reload":
                        if(player.hasPermission("adminbans.reload")){
                            com.rabbitcompany.adminbans.AdminBans.getInstance().loadYamls();
                            sender.sendMessage(Message.getMessage(UUID.randomUUID(), "reload"));
                        }else{
                            player.sendMessage(Message.getMessage(player.getUniqueId(),"permission"));
                        }
                        break;
                    default:
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "adminbans_syntax"));
                        break;
                }
            }else{
                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "adminbans_syntax"));
            }
        }else{
            player.sendMessage(Message.getMessage(player.getUniqueId(),"permission"));
        }

        return true;
    }
}
