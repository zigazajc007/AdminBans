package com.rabbitcompany.adminbans.listeners;

import com.rabbitcompany.adminbans.AdminBans;
import com.rabbitcompany.adminbans.AdminBansAPI;
import com.rabbitcompany.adminbans.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class PlayerLoginListener implements Listener {

    private AdminBans adminBans;

    public PlayerLoginListener(AdminBans plugin){
        adminBans = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerLogin(PlayerLoginEvent event){
        if(event.getResult() == PlayerLoginEvent.Result.ALLOWED){

            if(AdminBans.conn != null){
                if(AdminBansAPI.isPlayerBanned(event.getPlayer().getUniqueId(), AdminBansAPI.server_name)){
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Utils.playerBannedMessage(event.getPlayer().getUniqueId()));
                }else if(AdminBansAPI.isIPBanned(event.getAddress().toString().replace("/", ""))){
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, Utils.banIPMessage());
                }
            }

        }
    }

}
