package com.rabbitcompany.adminbans;

import com.rabbitcompany.adminbans.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminBansAPI {

    public static String server_name = AdminBans.getInstance().getConf().getString("server_name");
    public static SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static boolean isIPValid(final String ip) {
        String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";

        return ip.matches(PATTERN);
    }

    public static boolean isPlayerBanned(String player){
        String query = "SELECT * FROM adminbans_banned_players WHERE username_to = '" + player + "' ORDER BY until DESC;";
        AtomicBoolean isPlayerBanned = new AtomicBoolean(false);

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Timestamp until = rs.getTimestamp("until");
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if(now.before(until) || until.getTime() == Long.MAX_VALUE){
                    isPlayerBanned.set(true);
                }
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return isPlayerBanned.get();
    }

    public static boolean isPlayerBanned(String player, String server){
        String query = "SELECT * FROM adminbans_banned_players WHERE username_to = '" + player + "' AND server = '" + server + "' ORDER BY until DESC;";
        AtomicBoolean isPlayerBanned = new AtomicBoolean(false);

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Timestamp until = rs.getTimestamp("until");
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if(now.before(until) || until.getTime() == Long.MAX_VALUE){
                    isPlayerBanned.set(true);
                }
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return isPlayerBanned.get();
    }

    public static boolean isPlayerBanned(UUID uuid){
        String query = "SELECT * FROM adminbans_banned_players WHERE uuid_to = '" + uuid.toString() + "' ORDER BY until DESC;";
        AtomicBoolean isPlayerBanned = new AtomicBoolean(false);

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Timestamp until = rs.getTimestamp("until");
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if(now.before(until) || until.getTime() == Long.MAX_VALUE){
                    isPlayerBanned.set(true);
                }
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return isPlayerBanned.get();
    }

    public static boolean isPlayerBanned(UUID uuid, String server){
        String query = "SELECT * FROM adminbans_banned_players WHERE uuid_to = '" + uuid.toString() + "' AND server = '" + server + "' ORDER BY until DESC;";
        AtomicBoolean isPlayerBanned = new AtomicBoolean(false);

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Timestamp until = rs.getTimestamp("until");
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if(now.before(until) || until.getTime() == Long.MAX_VALUE){
                    isPlayerBanned.set(true);
                }
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return isPlayerBanned.get();
    }

    public static boolean isIPBanned(String ip){
        String query = "SELECT * FROM adminbans_banned_ips WHERE ip = '" + ip + "';";
        AtomicBoolean isIPBanned = new AtomicBoolean(false);

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                isIPBanned.set(true);
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return isIPBanned.get();
    }

    public static boolean isIPBanned(String ip, String server){
        String query = "SELECT * FROM adminbans_banned_ips WHERE ip = '" + ip + "' AND server = '" + server + "';";
        AtomicBoolean isIPBanned = new AtomicBoolean(false);

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                isIPBanned.set(true);
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return isIPBanned.get();
    }

    public static boolean isPlayerMuted(String player){
        String query = "SELECT * FROM adminbans_muted_players WHERE username_to = '" + player + "' ORDER BY until DESC;";
        AtomicBoolean isPlayerMuted = new AtomicBoolean(false);

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Timestamp until = rs.getTimestamp("until");
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if(now.before(until)){
                    isPlayerMuted.set(true);
                }
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return isPlayerMuted.get();
    }

    public static boolean isPlayerMuted(String player, String server){
        String query = "SELECT * FROM adminbans_muted_players WHERE username_to = '" + player + "' AND server = '" + server + "' ORDER BY until DESC;";
        AtomicBoolean isPlayerMuted = new AtomicBoolean(false);

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Timestamp until = rs.getTimestamp("until");
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if(now.before(until)){
                    isPlayerMuted.set(true);
                }
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return isPlayerMuted.get();
    }

    public static boolean isPlayerMuted(UUID uuid){
        String query = "SELECT * FROM adminbans_muted_players WHERE uuid_to = '" + uuid + "' ORDER BY until DESC;";
        AtomicBoolean isPlayerMuted = new AtomicBoolean(false);

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Timestamp until = rs.getTimestamp("until");
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if(now.before(until)){
                    isPlayerMuted.set(true);
                }
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return isPlayerMuted.get();
    }

    public static boolean isPlayerMuted(UUID uuid, String server){
        String query = "SELECT * FROM adminbans_muted_players WHERE uuid_to = '" + uuid + "' AND server = '" + server + "' ORDER BY until DESC;";
        AtomicBoolean isPlayerMuted = new AtomicBoolean(false);

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                Timestamp until = rs.getTimestamp("until");
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if(now.before(until)){
                    isPlayerMuted.set(true);
                }
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return isPlayerMuted.get();
    }

    public static String banPlayer(String uuid_from, String username_from, String uuid_to, String username_to, String reason, String until){

        if(until == null) until = "9999-12-31 23:59:59";

            try {
                Connection conn = AdminBans.hikari.getConnection();
                conn.createStatement().executeUpdate("INSERT INTO adminbans_banned_players(uuid_from, username_from, uuid_to, username_to, reason, until, server) VALUES ('" + uuid_from + "', '" + username_from + "', '" + uuid_to + "', '" + username_to + "', '" + reason + "', '" + until + "', '" + server_name + "');");
                conn.close();
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
                if(reason == null){
                    return Message.getMessage(UUID.randomUUID(), "player_ban_no_reason").replace("{player}", username_to);
                }else{
                    return Message.getMessage(UUID.randomUUID(), "player_ban").replace("{player}", username_to).replace("{reason}", reason);
                }
            } catch (SQLException ignored) {
                return Message.getMessage(UUID.randomUUID(), "ban_error").replace("{player}", username_to);
            }
    }

    public static String banIP(String ip){
            try {
                Connection conn = AdminBans.hikari.getConnection();
                conn.createStatement().executeUpdate("INSERT INTO adminbans_banned_ips(ip) VALUES ('" + ip + "');");
                conn.close();
                for(Player target : Bukkit.getOnlinePlayers()){
                    if(target != null){
                        if(target.isOnline()){
                            if(target.getAddress().getHostName().equals(ip)){
                                target.kickPlayer(Message.getMessage(target.getUniqueId(), "ip_ban_message"));
                            }
                        }
                    }
                }
                return Message.getMessage(UUID.randomUUID(), "ip_ban").replace("{ip}", ip);
            } catch (SQLException ignored) {
                return Message.getMessage(UUID.randomUUID(), "ban_ip_error").replace("{ip}", ip);
            }
    }

    public static String banIP(String ip, String server){
            try {
                Connection conn = AdminBans.hikari.getConnection();
                conn.createStatement().executeUpdate("INSERT INTO adminbans_banned_ips(ip, server) VALUES ('" + ip + "', '" + server + "');");
                conn.close();
                if(server_name.equals(server)){
                    for(Player target : Bukkit.getOnlinePlayers()){
                        if(target != null){
                            if(target.isOnline()){
                                if(target.getAddress().getHostName().equals(ip)){
                                    target.kickPlayer(Message.getMessage(target.getUniqueId(), "ip_ban_message"));
                                }
                            }
                        }
                    }
                }
                return Message.getMessage(UUID.randomUUID(), "ip_ban").replace("{ip}", ip);
            } catch (SQLException ignored) {
                return Message.getMessage(UUID.randomUUID(), "ban_ip_error").replace("{ip}", ip);
            }
    }

    public static String mutePlayer(String uuid_from, String username_from, String uuid_to, String username_to, String reason, String until){
        if(until == null) until = "9999-12-31 23:59:59";
            try {
                Connection conn = AdminBans.hikari.getConnection();
                conn.createStatement().executeUpdate("INSERT INTO adminbans_muted_players(uuid_from, username_from, uuid_to, username_to, reason, until, server) VALUES ('" + uuid_from + "', '" + username_from + "', '" + uuid_to + "', '" + username_to + "', '" + reason + "', '" + until + "', '" + server_name + "');");
                conn.close();
                return Message.getMessage(UUID.randomUUID(), "player_mute").replace("{player}", username_to).replace("{reason}", reason);
            } catch (SQLException ignored) {
                return Message.getMessage(UUID.randomUUID(), "mute_error").replace("{player}", username_to);
            }
    }

    public static String kickPlayer(String uuid_from, String username_from, String uuid_to, String username_to, String reason){
            try {
                Connection conn = AdminBans.hikari.getConnection();
                conn.createStatement().executeUpdate("INSERT INTO adminbans_kicked_players(uuid_from, username_from, uuid_to, username_to, reason, server) VALUES ('" + uuid_from + "', '" + username_from + "', '" + uuid_to + "', '" + username_to + "', '" + reason + "', '" + server_name + "');");
                conn.close();
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
    }

    public static String warnPlayer(String uuid_from, String username_from, String uuid_to, String username_to, String reason){
            try {
                Connection conn = AdminBans.hikari.getConnection();
                conn.createStatement().executeUpdate("INSERT INTO adminbans_warned_players(uuid_from, username_from, uuid_to, username_to, reason, server) VALUES ('" + uuid_from + "', '" + username_from + "', '" + uuid_to + "', '" + username_to + "', '" + reason + "', '" + server_name + "');");
                conn.close();
                return Message.getMessage(UUID.randomUUID(), "player_warn").replace("{player}", username_to).replace("{reason}", reason);
            } catch (SQLException ignored) {
                return Message.getMessage(UUID.randomUUID(), "warn_error").replace("{player}", username_to);
            }
    }

    public static boolean unBanPlayer(String player){
            if(isPlayerBanned(player)){
                try {
                    Date until = new Date(System.currentTimeMillis());
                    Connection conn = AdminBans.hikari.getConnection();
                    conn.createStatement().executeUpdate("UPDATE adminbans_banned_players SET until = '" + date_format.format(until) + "' WHERE username_to = '" + player + "';");
                    conn.close();
                    return true;
                } catch (SQLException ignored) { }
            }
        return false;
    }

    public static boolean unBanPlayer(UUID player){
            if(isPlayerBanned(player)){
                try {
                    Date until = new Date(System.currentTimeMillis());
                    Connection conn = AdminBans.hikari.getConnection();
                    conn.createStatement().executeUpdate("UPDATE adminbans_banned_players SET until = '" + date_format.format(until) + "' WHERE uuid_to = '" + player + "';");
                    conn.close();
                    return true;
                } catch (SQLException ignored) { }
            }
        return false;
    }

    public static boolean unBanIP(String ip){
            if(isIPBanned(ip)){
                try {
                    Connection conn = AdminBans.hikari.getConnection();
                    conn.createStatement().executeUpdate("DELETE FROM adminbans_banned_ips WHERE ip = '" + ip + "';");
                    conn.close();
                    return true;
                } catch (SQLException ignored) { }
            }
        return false;
    }

    public static boolean unBanIP(String ip, String server){
            if(isIPBanned(ip, server)){
                try {
                    Connection conn = AdminBans.hikari.getConnection();
                    conn.createStatement().executeUpdate("DELETE FROM adminbans_banned_ips WHERE ip = '" + ip + "' AND server = '" + server + "';");
                    conn.close();
                    return true;
                } catch (SQLException ignored) { }
            }
        return false;
    }

    public static boolean unMutePlayer(String player){
            if(isPlayerMuted(player)){
                try {
                    Date until = new Date(System.currentTimeMillis());
                    Connection conn = AdminBans.hikari.getConnection();
                    conn.createStatement().executeUpdate("UPDATE adminbans_muted_players SET until = '" + date_format.format(until) + "' WHERE username_to = '" + player + "';");
                    conn.close();
                    return true;
                } catch (SQLException ignored) { }
            }
        return false;
    }

    public static boolean unMutePlayer(UUID player){
            if(isPlayerMuted(player)){
                try {
                    Date until = new Date(System.currentTimeMillis());
                    Connection conn = AdminBans.hikari.getConnection();
                    conn.createStatement().executeUpdate("UPDATE adminbans_muted_players SET until = '" + date_format.format(until) + "' WHERE uuid_to = '" + player + "';");
                    conn.close();
                    return true;
                } catch (SQLException ignored) { }
            }
        return false;
    }

    public static int getPlayerWarnsCount(String player){
        String query = "SELECT COUNT(*) AS warn_count FROM adminbans_warned_players WHERE uuid_to = '" + player + "';";
        AtomicInteger getWarnCount = new AtomicInteger(0);

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                getWarnCount.set(rs.getInt("warn_count"));
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return getWarnCount.get();
    }

    public static int getPlayerWarnsCount(String player, String server){
        String query = "SELECT COUNT(*) AS warn_count FROM adminbans_warned_players WHERE uuid_to = '" + player + "' AND server = '" + server + "';";
        AtomicInteger getWarnCount = new AtomicInteger(0);

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                getWarnCount.set(rs.getInt("warn_count"));
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return getWarnCount.get();
    }

    public static int getPlayerWarnsCount(UUID uuid){
        String query = "SELECT COUNT(*) AS warn_count FROM adminbans_warned_players WHERE uuid_to = '" + uuid + "';";
        AtomicInteger getWarnCount = new AtomicInteger(0);

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                getWarnCount.set(rs.getInt("warn_count"));
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return getWarnCount.get();
    }

    public static int getPlayerWarnsCount(UUID uuid, String server){
        String query = "SELECT COUNT(*) AS warn_count FROM adminbans_warned_players WHERE uuid_to = '" + uuid + "' AND server = '" + server + "';";
        AtomicInteger getWarnCount = new AtomicInteger(0);

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                getWarnCount.set(rs.getInt("warn_count"));
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return getWarnCount.get();
    }

    public static ArrayList<BannedPlayer> getBannedPlayers(){
        ArrayList<BannedPlayer> banned_players = new ArrayList<>();

        String query = "SELECT * FROM adminbans_banned_players ORDER BY until DESC;";

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Timestamp until = rs.getTimestamp("until");
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if(now.before(until) || until.getTime() == Long.MAX_VALUE){
                    banned_players.add(new BannedPlayer(rs.getString("uuid_from"), rs.getString("username_from"), rs.getString("uuid_to"), rs.getString("username_to"), rs.getString("reason"), rs.getTimestamp("until"), rs.getString("server"), rs.getTimestamp("created")));
                }
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return banned_players;
    }

    public static ArrayList<BannedPlayer> getBannedPlayers(String server){
        ArrayList<BannedPlayer> banned_players = new ArrayList<>();

        String query = "SELECT * FROM adminbans_banned_players WHERE server = '" + server + "' ORDER BY until DESC;";

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Timestamp until = rs.getTimestamp("until");
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if(now.before(until) || until.getTime() == Long.MAX_VALUE){
                    banned_players.add(new BannedPlayer(rs.getString("uuid_from"), rs.getString("username_from"), rs.getString("uuid_to"), rs.getString("username_to"), rs.getString("reason"), rs.getTimestamp("until"), rs.getString("server"), rs.getTimestamp("created")));
                }
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return banned_players;
    }

    public static ArrayList<BannedIP> getBannedIPs(){
        ArrayList<BannedIP> banned_ips = new ArrayList<>();

        String query = "SELECT * FROM adminbans_banned_ips;";

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                banned_ips.add(new BannedIP(rs.getString("ip"), rs.getString("server")));
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return banned_ips;
    }

    public static ArrayList<BannedIP> getBannedIPs(String server){
        ArrayList<BannedIP> banned_ips = new ArrayList<>();

        String query = "SELECT * FROM adminbans_banned_ips WHERE server = '" + server + "';";

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                banned_ips.add(new BannedIP(rs.getString("ip"), rs.getString("server")));
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return banned_ips;
    }

    public static ArrayList<MutedPlayer> getMutedPlayers(){
        ArrayList<MutedPlayer> mutedPlayers = new ArrayList<>();

        String query = "SELECT * FROM adminbans_muted_players ORDER BY until DESC;";

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Timestamp until = rs.getTimestamp("until");
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if(now.before(until)){
                    mutedPlayers.add(new MutedPlayer(rs.getString("uuid_from"), rs.getString("username_from"), rs.getString("uuid_to"), rs.getString("username_to"), rs.getString("reason"), rs.getTimestamp("until"), rs.getString("server"), rs.getTimestamp("created")));
                }
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return mutedPlayers;
    }

    public static ArrayList<MutedPlayer> getMutedPlayers(String server){
        ArrayList<MutedPlayer> mutedPlayers = new ArrayList<>();

        String query = "SELECT * FROM adminbans_muted_players WHERE server = '" + server + "' ORDER BY until DESC;";

        try {
            Connection conn = AdminBans.hikari.getConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Timestamp until = rs.getTimestamp("until");
                Timestamp now = new Timestamp(System.currentTimeMillis());
                if(now.before(until)){
                    mutedPlayers.add(new MutedPlayer(rs.getString("uuid_from"), rs.getString("username_from"), rs.getString("uuid_to"), rs.getString("username_to"), rs.getString("reason"), rs.getTimestamp("until"), rs.getString("server"), rs.getTimestamp("created")));
                }
            }
            conn.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return mutedPlayers;
    }
}
