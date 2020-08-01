package com.rabbitcompany.adminbans.commands;

import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.adminbans.utils.Message;
import com.rabbitcompany.adminbans.utils.Utils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Date;
import java.util.UUID;

public class Ban implements CommandExecutor {
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)) {
            if(args.length == 1) {
                String str_player = args[0];
                Date until = new Date(Long.MAX_VALUE);
                Bukkit.broadcastMessage(AdminBansAPI.banPlayer("Console", "Console", Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, null, AdminBansAPI.date_format.format(until)));
            }else if(args.length == 2){
                String str_player = args[0];
                String str_time = args[1];

                long mil_year = 31556952000L;
                long mil_month = 2592000000L;
                long mil_day = 86400000L;
                long mil_hour = 3600000L;
                long mil_minute = 60000L;

                if(NumberUtils.isNumber(Utils.stripNonDigits(str_time))){
                    int number = Integer.parseInt(Utils.stripNonDigits(str_time));
                    long time = 0L;

                    if(str_time.contains("min") || str_time.contains("minute") || str_time.contains("minutes")){
                        time = mil_minute;
                    }else if(str_time.contains("h") || str_time.contains("hour") || str_time.contains("hours")){
                        time = mil_hour;
                    }else if(str_time.contains("d") || str_time.contains("day") || str_time.contains("days")){
                        time = mil_day;
                    }else if(str_time.contains("m") || str_time.contains("month") || str_time.contains("months")){
                        time = mil_month;
                    }else if(str_time.contains("y") || str_time.contains("year") || str_time.contains("years")){
                        time = mil_year;
                    }

                    Date until = new Date(System.currentTimeMillis() + (number * time));

                    Bukkit.broadcastMessage(AdminBansAPI.banPlayer("Console", "Console", Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, null, AdminBansAPI.date_format.format(until)));

                }else{
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "is_not_a_number").replace("{number}", str_time));
                }
            }else if(args.length >= 3){
                String str_player = args[0];
                String str_time = args[1];
                StringBuilder reason = new StringBuilder();
                boolean silence = false;

                for (int i = 2; i < args.length; i++){
                    if(!args[i].equals("-s")){
                        reason.append(args[i]).append(" ");
                    }else{
                        silence = true;
                    }
                }

                long mil_year = 31556952000L;
                long mil_month = 2592000000L;
                long mil_day = 86400000L;
                long mil_hour = 3600000L;
                long mil_minute = 60000L;

                if(NumberUtils.isNumber(Utils.stripNonDigits(str_time))){
                    int number = Integer.parseInt(Utils.stripNonDigits(str_time));
                    long time = 0L;

                    if(str_time.contains("min") || str_time.contains("minute") || str_time.contains("minutes")){
                        time = mil_minute;
                    }else if(str_time.contains("h") || str_time.contains("hour") || str_time.contains("hours")){
                        time = mil_hour;
                    }else if(str_time.contains("d") || str_time.contains("day") || str_time.contains("days")){
                        time = mil_day;
                    }else if(str_time.contains("m") || str_time.contains("month") || str_time.contains("months")){
                        time = mil_month;
                    }else if(str_time.contains("y") || str_time.contains("year") || str_time.contains("years")){
                        time = mil_year;
                    }

                    Date until = new Date(System.currentTimeMillis() + (number * time));

                    if(silence){
                        sender.sendMessage(AdminBansAPI.banPlayer("Console", "Console", Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, reason.toString(), AdminBansAPI.date_format.format(until)));
                    }else{
                        Bukkit.broadcastMessage(AdminBansAPI.banPlayer("Console", "Console", Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, reason.toString(), AdminBansAPI.date_format.format(until)));
                    }

                }else{
                    sender.sendMessage(Message.getMessage(UUID.randomUUID(), "is_not_a_number").replace("{number}", str_time));
                }
            }else{
                sender.sendMessage(Message.getMessage(UUID.randomUUID(), "ban_syntax"));
            }
            return true;
        }

        Player player = (Player) sender;

        if(player.hasPermission("adminbans.ban")){
            if(args.length == 1){
                String str_player = args[0];

                Player target_player = Bukkit.getPlayer(str_player);

                if(target_player != null){
                    if(target_player.hasPermission("adminbans.ban.exempt")){
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "player_ban_exempt").replace("{player}", target_player.getName()));
                        return true;
                    }
                }

                Date until = new Date(Long.MAX_VALUE);
                Bukkit.broadcastMessage(AdminBansAPI.banPlayer(player.getUniqueId().toString(), player.getName(), Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, null, AdminBansAPI.date_format.format(until)));
            }else if(args.length == 2){
                String str_player = args[0];
                String str_time = args[1];

                Player target_player = Bukkit.getPlayer(str_player);

                if(target_player != null){
                    if(target_player.hasPermission("adminbans.ban.exempt")){
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "player_ban_exempt").replace("{player}", target_player.getName()));
                        return true;
                    }
                }

                long mil_year = 31556952000L;
                long mil_month = 2592000000L;
                long mil_day = 86400000L;
                long mil_hour = 3600000L;
                long mil_minute = 60000L;

                if(NumberUtils.isNumber(Utils.stripNonDigits(str_time))){
                    int number = Integer.parseInt(Utils.stripNonDigits(str_time));
                    long time = 0L;

                    if(str_time.contains("min") || str_time.contains("minute") || str_time.contains("minutes")){
                        time = mil_minute;
                    }else if(str_time.contains("h") || str_time.contains("hour") || str_time.contains("hours")){
                        time = mil_hour;
                    }else if(str_time.contains("d") || str_time.contains("day") || str_time.contains("days")){
                        time = mil_day;
                    }else if(str_time.contains("m") || str_time.contains("month") || str_time.contains("months")){
                        time = mil_month;
                    }else if(str_time.contains("y") || str_time.contains("year") || str_time.contains("years")){
                        time = mil_year;
                    }

                    Date until = new Date(System.currentTimeMillis() + (number * time));

                    Bukkit.broadcastMessage(AdminBansAPI.banPlayer(player.getUniqueId().toString(), player.getName(), Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, null, AdminBansAPI.date_format.format(until)));

                }else{
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "is_not_a_number").replace("{number}", str_time));
                }
            }else if(args.length >= 3){
                String str_player = args[0];
                String str_time = args[1];

                Player target_player = Bukkit.getPlayer(str_player);

                if(target_player != null){
                    if(target_player.hasPermission("adminbans.ban.exempt")){
                        player.sendMessage(Message.getMessage(player.getUniqueId(), "player_ban_exempt").replace("{player}", target_player.getName()));
                        return true;
                    }
                }

                StringBuilder reason = new StringBuilder();
                boolean silence = false;

                for (int i = 2; i < args.length; i++) {
                    if (!args[i].equals("-s")) {
                        reason.append(args[i]).append(" ");
                    } else {
                        silence = true;
                    }
                }

                long mil_year = 31556952000L;
                long mil_month = 2592000000L;
                long mil_day = 86400000L;
                long mil_hour = 3600000L;
                long mil_minute = 60000L;

                if (NumberUtils.isNumber(Utils.stripNonDigits(str_time))) {
                    int number = Integer.parseInt(Utils.stripNonDigits(str_time));
                    long time = 0L;

                    if (str_time.contains("min") || str_time.contains("minute") || str_time.contains("minutes")) {
                        time = mil_minute;
                    } else if (str_time.contains("h") || str_time.contains("hour") || str_time.contains("hours")) {
                        time = mil_hour;
                    } else if (str_time.contains("d") || str_time.contains("day") || str_time.contains("days")) {
                        time = mil_day;
                    } else if (str_time.contains("m") || str_time.contains("month") || str_time.contains("months")) {
                        time = mil_month;
                    } else if (str_time.contains("y") || str_time.contains("year") || str_time.contains("years")) {
                        time = mil_year;
                    }

                    Date until = new Date(System.currentTimeMillis() + (number * time));

                    if (silence) {
                        player.sendMessage(AdminBansAPI.banPlayer(player.getUniqueId().toString(), player.getName(), Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, reason.toString(), AdminBansAPI.date_format.format(until)));
                    } else {
                        Bukkit.broadcastMessage(AdminBansAPI.banPlayer(player.getUniqueId().toString(), player.getName(), Bukkit.getOfflinePlayer(str_player).getUniqueId().toString(), str_player, reason.toString(), AdminBansAPI.date_format.format(until)));
                    }

                } else {
                    player.sendMessage(Message.getMessage(player.getUniqueId(), "is_not_a_number").replace("{number}", str_time));
                }
            }else{
                player.sendMessage(Message.getMessage(player.getUniqueId(), "ban_syntax"));
            }
        }else{
            player.sendMessage(Message.getMessage(player.getUniqueId(), "permission"));
        }

        return true;
    }
}
