package com.rabbitcompany.adminbans;

import com.rabbitcompany.adminbans.utils.BannedPlayer;
import com.rabbitcompany.adminbans.utils.Message;
import com.rabbitcompany.adminbans.utils.MutedPlayer;
import com.rabbitcompany.adminbans.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminBansAPI {

    public static SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static boolean isPlayerBanned(String player){
        String query = "SELECT * FROM adminbans_banned_players WHERE username_to = '" + player + "' ORDER BY until DESC;";
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
        String query = "SELECT * FROM adminbans_banned_players WHERE uuid_to = '" + uuid.toString() + "' ORDER BY until DESC;";
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
        String query = "SELECT * FROM adminbans_muted_players WHERE username_to = '" + player + "' ORDER BY until DESC;";
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
        String query = "SELECT * FROM adminbans_muted_players WHERE uuid_to = '" + uuid + "' ORDER BY until DESC;";
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
                AdminBans.mySQL.update("INSERT INTO adminbans_banned_players(uuid_from, username_from, uuid_to, username_to, reason, until) VALUES ('" + uuid_from + "', '" + username_from + "', '" + uuid_to + "', '" + username_to + "', '" + reason + "', '" + until + "');");
                Player target_uuid = Bukkit.getPlayer(UUID.fromString(uuid_to));
                Player target_name = Bukkit.getPlayer(username_to);
                if(target_uuid != null){
                    if(target_uuid.isOnline()){
                        target_uuid.kickPlayer(Utils.banReasonMessage(target_uuid.getUniqueId(), reason, until));
                    }
                }else if(target_name != null){
                    if(target_name.isOnline()){
                        target_name.kickPlayer(Utils.banReasonMessage(target_name.getUniqueId(), reason, until));
                    }
                }
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
                AdminBans.mySQL.update("INSERT INTO adminbans_muted_players(uuid_from, username_from, uuid_to, username_to, reason, until) VALUES ('" + uuid_from + "', '" + username_from + "', '" + uuid_to + "', '" + username_to + "', '" + reason + "', '" + until + "');");
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
                AdminBans.mySQL.update("INSERT INTO adminbans_kicked_players(uuid_from, username_from, uuid_to, username_to, reason) VALUES ('" + uuid_from + "', '" + username_from + "', '" + uuid_to + "', '" + username_to + "', '" + reason + "');");
                Player target_uuid = Bukkit.getPlayer(UUID.fromString(uuid_to));
                Player target_name = Bukkit.getPlayer(username_to);
                if(target_uuid != null){
                    if(target_uuid.isOnline()){
                        target_uuid.kickPlayer(Message.chat(Message.getMessage(target_uuid.getUniqueId(), "kick_message").replace("{reason}", reason).replace("{prefix}", Message.getMessage(target_uuid.getUniqueId(), "prefix"))));
                    }
                }else if(target_name != null){
                    if(target_name.isOnline()){
                        target_name.kickPlayer(Message.chat(Message.getMessage(target_name.getUniqueId(), "prefix") + Message.getMessage(target_name.getUniqueId(), "kick_message").replace("{reason}", reason).replace("{prefix}", Message.getMessage(target_name.getUniqueId(), "prefix"))));
                    }
                }
                return Message.getMessage(UUID.randomUUID(), "player_kick").replace("{player}", username_to).replace("{reason}", reason);
            } catch (SQLException ignored) {
                return Message.getMessage(UUID.randomUUID(), "kick_error").replace("{player}", username_to);
            }
        }else{
            return Message.getMessage(UUID.randomUUID(), "mysql_not_connected");
        }
    }

    public static String warnPlayer(String uuid_from, String username_from, String uuid_to, String username_to, String reason){
        if(AdminBans.conn != null) {
            try {
                AdminBans.mySQL.update("INSERT INTO adminbans_warned_players(uuid_from, username_from, uuid_to, username_to, reason) VALUES ('" + uuid_from + "', '" + username_from + "', '" + uuid_to + "', '" + username_to + "', '" + reason + "');");
                return Message.getMessage(UUID.randomUUID(), "player_warn").replace("{player}", username_to).replace("{reason}", reason);
            } catch (SQLException ignored) {
                return Message.getMessage(UUID.randomUUID(), "warn_error").replace("{player}", username_to);
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
                    AdminBans.mySQL.update("UPDATE adminbans_banned_players SET until = '" + date_format.format(until) + "' WHERE username_to = '" + player + "';");
                    return true;
                } catch (SQLException ignored) { }
            }
        }
        return false;
    }

    public static boolean unBanPlayer(UUID player){
        if(AdminBans.conn != null) {
            if(isPlayerBanned(player)){
                try {
                    Date until = new Date(System.currentTimeMillis());
                    AdminBans.mySQL.update("UPDATE adminbans_banned_players SET until = '" + date_format.format(until) + "' WHERE uuid_to = '" + player + "';");
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
                    AdminBans.mySQL.update("UPDATE adminbans_muted_players SET until = '" + date_format.format(until) + "' WHERE username_to = '" + player + "';");
                    return true;
                } catch (SQLException ignored) { }
            }
        }
        return false;
    }

    public static boolean unMutePlayer(UUID player){
        if(AdminBans.conn != null) {
            if(isPlayerMuted(player)){
                try {
                    Date until = new Date(System.currentTimeMillis());
                    AdminBans.mySQL.update("UPDATE adminbans_muted_players SET until = '" + date_format.format(until) + "' WHERE uuid_to = '" + player + "';");
                    return true;
                } catch (SQLException ignored) { }
            }
        }
        return false;
    }

    public static int getPlayerWarnsCount(String player){
        String query = "SELECT COUNT(*) AS warn_count FROM adminbans_warned_players WHERE uuid_to = '" + player + "';";
            AtomicInteger getWarnCount = new AtomicInteger(0);
        try {
            AdminBans.mySQL.query(query, results -> {
                if (results.next()) {
                    getWarnCount.set(results.getInt("warn_count"));
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getWarnCount.get();
    }

    public static int getPlayerWarnsCount(UUID uuid){
        String query = "SELECT COUNT(*) AS warn_count FROM adminbans_warned_players WHERE uuid_to = '" + uuid + "';";
        AtomicInteger getWarnCount = new AtomicInteger(0);
        try {
            AdminBans.mySQL.query(query, results -> {
                if (results.next()) {
                    getWarnCount.set(results.getInt("warn_count"));
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return getWarnCount.get();
    }

    public static ArrayList<BannedPlayer> getBannedPlayers(){
        ArrayList<BannedPlayer> banned_players = new ArrayList<>();

        String query = "SELECT * FROM adminbans_banned_players ORDER BY until DESC;";

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

        String query = "SELECT * FROM adminbans_muted_players ORDER BY until DESC;";

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
