package com.rabbitcompany.adminbans.commands;

import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.adminbans.utils.Message;
import org.bukkit.BanEntry;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Date;
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
                    case "import":
                        sender.sendMessage(Message.chat("&cPlease use /adminbans import <bukkit>"));
                        break;
                    default:
                        sender.sendMessage(Message.getMessage(UUID.randomUUID(), "adminbans_syntax"));
                        break;
                }
            }else if(args.length == 2 && args[0].equals("import")){
                switch (args[1]){
                    case "bukkit":
                        sender.sendMessage(Message.chat("&aImporting Bukkit bans..."));
                        sender.sendMessage(Message.chat("&aBanned accounts:"));
                        for (BanEntry banned : Bukkit.getBanList(BanList.Type.NAME).getBanEntries()) {
                            if(banned.getExpiration() != null){
                                AdminBansAPI.banPlayer("Console", "Console", Bukkit.getOfflinePlayer(banned.getTarget()).getUniqueId().toString(), banned.getTarget(), banned.getReason(), AdminBansAPI.date_format.format(banned.getExpiration()));
                            }else{
                                Date until = new Date(System.currentTimeMillis() + 315569520000L);
                                AdminBansAPI.banPlayer("Console", "Console", Bukkit.getOfflinePlayer(banned.getTarget()).getUniqueId().toString(), banned.getTarget(), banned.getReason(), AdminBansAPI.date_format.format(until));
                            }
                            sender.sendMessage(Message.chat("&a     - " + banned.getTarget()));
                        }
                        sender.sendMessage(Message.chat("&aBanned IPs:"));
                        for (BanEntry banned : Bukkit.getBanList(BanList.Type.IP).getBanEntries()){
                            AdminBansAPI.banIP(banned.getTarget());
                            sender.sendMessage(Message.chat("&a     - " + banned.getTarget()));
                        }
                        sender.sendMessage(Message.chat("&aImporting completed!"));
                        break;
                    default:
                        sender.sendMessage(Message.chat("&cAdminBans don't support &a" + args[1] + "&c ban plugin."));
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
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "commands"));
                        break;
                    case "reload":
                        if(player.hasPermission("adminbans.reload")){
                            com.rabbitcompany.adminbans.AdminBans.getInstance().loadYamls();
                            player.sendMessage(Message.getMessage(player.getUniqueId(), "reload"));
                        }else{
                            player.sendMessage(Message.getMessage(player.getUniqueId(),"permission"));
                        }
                        break;
                    default:
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "adminbans_syntax"));
                        break;
                }
            }else{
                player.sendMessage(Message.getMessage(player.getUniqueId(), "adminbans_syntax"));
            }
        }else{
            player.sendMessage(Message.getMessage(player.getUniqueId(),"permission"));
        }

        return true;
    }
}
