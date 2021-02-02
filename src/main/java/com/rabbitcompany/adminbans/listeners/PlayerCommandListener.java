package com.rabbitcompany.adminbans.listeners;

import com.rabbitcompany.adminbans.AdminBans;
import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.adminbans.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class PlayerCommandListener implements Listener {

    private AdminBans adminBans;

    public PlayerCommandListener(AdminBans plugin){
        adminBans = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(PlayerCommandPreprocessEvent event){
        Player p = event.getPlayer();
        if(AdminBansAPI.isPlayerMuted(p.getUniqueId(), AdminBansAPI.server_name)){
            if(adminBans.getConf().getStringList("mute_commands_restriction").contains(event.getMessage().substring(0, ' '))){
                p.sendMessage(Message.getMessage(p.getUniqueId(), "mute_message"));
                event.setCancelled(true);
            }
        }
    }
}
