package com.rabbitcompany.adminbans.utils;

import com.rabbitcompany.adminbans.AdminBans;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.UUID;

public class Message {

    public static String chat(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String getMessage(UUID player, String config){
        String message;

        message = AdminBans.getInstance().getMess().getString(config);

        if(message != null){
            if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
                message = PlaceholderAPI.setPlaceholders(Bukkit.getPlayer(player), message);
            }
            return chat(message);
        }else{
            return chat("&cValue: &6" + config + "&c is missing in Messages file! Please add it or delete Messages file.");
        }
    }
}
