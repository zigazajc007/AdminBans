package com.rabbitcompany.adminbans.listeners;

import com.rabbitcompany.adminbans.AdminBans;
import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.adminbans.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerMessageListener implements Listener {

    private AdminBans adminBans;

    public PlayerMessageListener(AdminBans plugin){
        adminBans = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event){
        Player p = event.getPlayer();
        if(AdminBansAPI.isPlayerMuted(p.getUniqueId())){
            p.sendMessage(Message.getMessage(p.getUniqueId(), "mute_message"));
            event.setCancelled(true);
        }
    }

}
