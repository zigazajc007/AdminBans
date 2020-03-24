package com.rabbitcompany.adminbans;

import com.rabbitcompany.adminbans.utils.BannedPlayer;
import com.rabbitcompany.adminbans.utils.Message;
import com.rabbitcompany.adminbans.utils.MutedPlayer;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public class AdminBansAPI {

    public static SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static boolean isPlayerBanned(String player){
        String query = "SELECT * FROM admin_gui_banned_players WHERE username_to = '" + player + "' ORDER BY until DESC;";
        AtomicBoolean isPlayerBanned = new AtomicBoolean(false);
        try {
            AdminBans.mySQL.query(query, results -> {
                if (results.next()) {
                    Timestamp until = results.getTimestamp("until");
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    if(now.before(until)){
                        isPlayerBanned.set(true);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isPlayerBanned.get();
    }

    public static boolean isPlayerBanned(UUID uuid){
        String query = "SELECT * FROM admin_gui_banned_players WHERE uuid_to = '" + uuid.toString() + "' ORDER BY until DESC;";
        AtomicBoolean isPlayerBanned = new AtomicBoolean(false);
        try {
            AdminBans.mySQL.query(query, results -> {
                if (results.next()) {
                    Timestamp until = results.getTimestamp("until");
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    if(now.before(until)){
                        isPlayerBanned.set(true);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isPlayerBanned.get();
    }

    public static boolean isPlayerMuted(String player){
        String query = "SELECT * FROM admin_gui_muted_players WHERE username_to = '" + player + "' ORDER BY until DESC;";
        AtomicBoolean isPlayerMuted = new AtomicBoolean(false);
        try {
            AdminBans.mySQL.query(query, results -> {
                if (results.next()) {
                    Timestamp until = results.getTimestamp("until");
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    if(now.before(until)){
                        isPlayerMuted.set(true);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isPlayerMuted.get();
    }

    public static boolean isPlayerMuted(UUID uuid){
        String query = "SELECT * FROM admin_gui_muted_players WHERE uuid_to = '" + uuid + "' ORDER BY until DESC;";
        AtomicBoolean isPlayerMuted = new AtomicBoolean(false);
        try {
            AdminBans.mySQL.query(query, results -> {
                if (results.next()) {
                    Timestamp until = results.getTimestamp("until");
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    if(now.before(until)){
                        isPlayerMuted.set(true);
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isPlayerMuted.get();
    }

    public static String banPlayer(String uuid_from, String username_from, String uuid_to, String username_to, String reason, String until){
        if(AdminBans.conn != null) {
            try {
                AdminBans.mySQL.update("INSERT INTO admin_gui_banned_players(uuid_from, username_from, uuid_to, username_to, reason, until) VALUES ('" + uuid_from + "', '" + username_from + "', '" + uuid_to + "', '" + username_to + "', '" + reason + "', '" + until + "');");
                return Message.getMessage(UUID.randomUUID(), "player_ban").replace("{player}", username_to).replace("{reason}", reason);
            } catch (SQLException ignored) {
                return Message.getMessage(UUID.randomUUID(), "ban_error").replace("{player}", username_to);
            }
        }else{
            return Message.getMessage(UUID.randomUUID(), "mysql_not_connected");
        }
    }

    public static String mutePlayer(String uuid_from, String username_from, String uuid_to, String username_to, String reason, String until){
        if(AdminBans.conn != null) {
            try {
                AdminBans.mySQL.update("INSERT INTO admin_gui_muted_players(uuid_from, username_from, uuid_to, username_to, reason, until) VALUES ('" + uuid_from + "', '" + username_from + "', '" + uuid_to + "', '" + username_to + "', '" + reason + "', '" + until + "');");
                return Message.getMessage(UUID.randomUUID(), "player_mute").replace("{player}", username_to).replace("{reason}", reason);
            } catch (SQLException ignored) {
                return Message.getMessage(UUID.randomUUID(), "mute_error").replace("{player}", username_to);
            }
        }else{
            return Message.getMessage(UUID.randomUUID(), "mysql_not_connected");
        }
    }

    public static String kickPlayer(String uuid_from, String username_from, String uuid_to, String username_to, String reason){
        if(AdminBans.conn != null) {
            try {
                AdminBans.mySQL.update("INSERT INTO admin_gui_kicked_players(uuid_from, username_from, uuid_to, username_to, reason) VALUES ('" + uuid_from + "', '" + username_from + "', '" + uuid_to + "', '" + username_to + "', '" + reason + "');");
                return Message.getMessage(UUID.randomUUID(), "player_mute").replace("{player}", username_to).replace("{reason}", reason);
            } catch (SQLException ignored) {
                return Message.getMessage(UUID.randomUUID(), "kick_error").replace("{player}", username_to);
            }
        }else{
            return Message.getMessage(UUID.randomUUID(), "mysql_not_connected");
        }
    }

    public static boolean unBanPlayer(String player){
        if(AdminBans.conn != null) {
            if(isPlayerBanned(player)){
                try {
                    Date until = new Date(System.currentTimeMillis());
                    AdminBans.mySQL.update("UPDATE admin_gui_banned_players SET until = '" + date_format.format(until) + "' WHERE username_to = '" + player + "';");
                    return true;
                } catch (SQLException ignored) { }
            }
        }
        return false;
    }

    public static boolean unMutePlayer(String player){
        if(AdminBans.conn != null) {
            if(isPlayerMuted(player)){
                try {
                    Date until = new Date(System.currentTimeMillis());
                    AdminBans.mySQL.update("UPDATE admin_gui_muted_players SET until = '" + date_format.format(until) + "' WHERE username_to = '" + player + "';");
                    return true;
                } catch (SQLException ignored) { }
            }
        }
        return false;
    }

    public static ArrayList<BannedPlayer> getBannedPlayers(){
        ArrayList<BannedPlayer> banned_players = new ArrayList<>();

        String query = "SELECT * FROM admin_gui_banned_players ORDER BY until DESC;";

        try {
            AdminBans.mySQL.query(query, results -> {
                while (results.next()){
                    Timestamp until = results.getTimestamp("until");
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    if(now.before(until)){
                        banned_players.add(new BannedPlayer(results.getString("uuid_from"), results.getString("username_from"), results.getString("uuid_to"), results.getString("username_to"), results.getString("reason"), results.getTimestamp("until"), results.getTimestamp("created")));
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return banned_players;
    }

    public static ArrayList<MutedPlayer> getMutedPlayers(){
        ArrayList<MutedPlayer> mutedPlayers = new ArrayList<>();

        String query = "SELECT * FROM admin_gui_muted_players ORDER BY until DESC;";

        try {
            AdminBans.mySQL.query(query, results -> {
                while (results.next()){
                    Timestamp until = results.getTimestamp("until");
                    Timestamp now = new Timestamp(System.currentTimeMillis());
                    if(now.before(until)){
                        mutedPlayers.add(new MutedPlayer(results.getString("uuid_from"), results.getString("username_from"), results.getString("uuid_to"), results.getString("username_to"), results.getString("reason"), results.getTimestamp("until"), results.getTimestamp("created")));
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return mutedPlayers;
    }
}
