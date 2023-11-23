package com.rabbitcompany.adminbans;

import com.rabbitcompany.adminbans.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AdminBansAPI {

	public static String server_name = AdminBans.getInstance().getConf().getString("server_name");
	public static SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static ArrayList<BannedPlayer> bannedPlayers = new ArrayList<>();
	private static ArrayList<BannedIP> bannedIPs = new ArrayList<>();
	private static ArrayList<MutedPlayer> mutedPlayers = new ArrayList<>();

	public static boolean isIPValid(final String ip) {
		String PATTERN = "^((0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)\\.){3}(0|1\\d?\\d?|2[0-4]?\\d?|25[0-5]?|[3-9]\\d?)$";
		return ip.matches(PATTERN);
	}

	public static boolean isPlayerBanned(String player) {
		for (BannedPlayer bannedPlayer : getBannedPlayers()) {
			if (bannedPlayer.username_to.equals(player)) return true;
		}
		return false;
	}

	public static boolean isPlayerBanned(String player, String server) {
		for (BannedPlayer bannedPlayer : getBannedPlayers(server)) {
			if (bannedPlayer.username_to.equals(player)) return true;
		}
		return false;
	}

	public static boolean isPlayerBanned(UUID uuid) {
		for (BannedPlayer bannedPlayer : getBannedPlayers()) {
			if (bannedPlayer.uuid_to.equals(uuid.toString())) return true;
		}
		return false;
	}

	public static boolean isPlayerBanned(UUID uuid, String server) {
		for (BannedPlayer bannedPlayer : getBannedPlayers(server)) {
			if (bannedPlayer.uuid_to.equals(uuid.toString())) return true;
		}
		return false;
	}

	public static boolean isIPBanned(String ip) {
		return getBannedIPs().contains(new BannedIP(ip, "Global"));
	}

	public static boolean isIPBanned(String ip, String server) {
		return getBannedIPs(server).contains(new BannedIP(ip, server));
	}

	public static boolean isPlayerMuted(String player) {
		for (MutedPlayer mutedPlayer : getMutedPlayers()) {
			if (mutedPlayer.username_to.equals(player)) return true;
		}
		return false;
	}

	public static boolean isPlayerMuted(String player, String server) {
		for (MutedPlayer mutedPlayer : getMutedPlayers(server)) {
			if (mutedPlayer.username_to.equals(player)) return true;
		}
		return false;
	}

	public static boolean isPlayerMuted(UUID uuid) {
		for (MutedPlayer mutedPlayer : getMutedPlayers()) {
			if (mutedPlayer.uuid_to.equals(uuid.toString())) return true;
		}
		return false;
	}

	public static boolean isPlayerMuted(UUID uuid, String server) {
		for (MutedPlayer mutedPlayer : getMutedPlayers(server)) {
			if (mutedPlayer.uuid_to.equals(uuid.toString())) return true;
		}
		return false;
	}

	public static String banPlayer(String uuid_from, String username_from, String uuid_to, String username_to, String reason, String until) {

		if (until == null) until = "9999-12-31 23:59:59";

		try {
			String query = "INSERT INTO adminbans_banned_players(uuid_from, username_from, uuid_to, username_to, reason, until, server, created) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
			Connection conn = AdminBans.hikari.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, uuid_from);
			ps.setString(2, username_from);
			ps.setString(3, uuid_to);
			ps.setString(4, username_to);
			ps.setString(5, reason);
			ps.setString(6, until);
			ps.setString(7, server_name);
			ps.setString(8, date_format.format(new Date()));
			ps.executeUpdate();
			ps.close();
			conn.close();
			Player target_uuid = Bukkit.getPlayer(UUID.fromString(uuid_to));
			Player target_name = Bukkit.getPlayer(username_to);
			if (target_uuid != null && target_uuid.isOnline()) {
				target_uuid.kickPlayer(Utils.banReasonMessage(target_uuid.getUniqueId(), reason, until));
			} else if (target_name != null && target_name.isOnline()) {
				target_name.kickPlayer(Utils.banReasonMessage(target_name.getUniqueId(), reason, until));
			}
			if (reason == null || reason.isEmpty()) {
				return Message.getMessage(UUID.randomUUID(), "player_ban_no_reason").replace("{player}", username_to);
			} else {
				return Message.getMessage(UUID.randomUUID(), "player_ban").replace("{player}", username_to).replace("{reason}", Message.chat(reason));
			}
		} catch (SQLException ignored) {
			return Message.getMessage(UUID.randomUUID(), "ban_error").replace("{player}", username_to);
		}
	}

	public static String banIP(String ip) {
		try {
			String query = "INSERT INTO adminbans_banned_ips(ip) VALUES (?);";
			Connection conn = AdminBans.hikari.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, ip);
			ps.executeUpdate();
			ps.close();
			conn.close();
			for (Player target : Bukkit.getOnlinePlayers()) {
				if (target != null && target.isOnline()) {
					if (target.getAddress().getHostName().equals(ip)) {
						target.kickPlayer(Message.getMessage(target.getUniqueId(), "ip_ban_message"));
					}
				}
			}
			return Message.getMessage(UUID.randomUUID(), "ip_ban").replace("{ip}", ip);
		} catch (SQLException ignored) {
			return Message.getMessage(UUID.randomUUID(), "ban_ip_error").replace("{ip}", ip);
		}
	}

	public static String banIP(String ip, String server) {
		try {
			String query = "INSERT INTO adminbans_banned_ips(ip, server) VALUES (?, ?);";
			Connection conn = AdminBans.hikari.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, ip);
			ps.setString(2, server);
			ps.executeUpdate();
			ps.close();
			conn.close();
			if (server_name.equals(server)) {
				for (Player target : Bukkit.getOnlinePlayers()) {
					if (target != null && target.isOnline()) {
						if (target.getAddress().getHostName().equals(ip)) {
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

	public static String mutePlayer(String uuid_from, String username_from, String uuid_to, String username_to, String reason, String until) {
		if (until == null) until = "9999-12-31 23:59:59";
		try {
			String query = "INSERT INTO adminbans_muted_players(uuid_from, username_from, uuid_to, username_to, reason, until, server, created) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";
			Connection conn = AdminBans.hikari.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, uuid_from);
			ps.setString(2, username_from);
			ps.setString(3, uuid_to);
			ps.setString(4, username_to);
			ps.setString(5, reason);
			ps.setString(6, until);
			ps.setString(7, server_name);
			ps.setString(8, date_format.format(new Date()));
			ps.executeUpdate();
			ps.close();
			conn.close();
			return Message.getMessage(UUID.randomUUID(), "player_mute").replace("{player}", username_to).replace("{reason}", Message.chat(reason));
		} catch (SQLException ignored) {
			return Message.getMessage(UUID.randomUUID(), "mute_error").replace("{player}", username_to);
		}
	}

	public static String kickPlayer(String uuid_from, String username_from, String uuid_to, String username_to, String reason) {
		try {
			String query = "INSERT INTO adminbans_kicked_players(uuid_from, username_from, uuid_to, username_to, reason, server, created) VALUES (?, ?, ?, ?, ?, ?, ?);";
			Connection conn = AdminBans.hikari.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, uuid_from);
			ps.setString(2, username_from);
			ps.setString(3, uuid_to);
			ps.setString(4, username_to);
			ps.setString(5, reason);
			ps.setString(6, server_name);
			ps.setString(7, date_format.format(new Date()));
			ps.close();
			conn.close();
			Player target_uuid = Bukkit.getPlayer(UUID.fromString(uuid_to));
			Player target_name = Bukkit.getPlayer(username_to);
			if (target_uuid != null && target_uuid.isOnline()) {
				target_uuid.kickPlayer(Message.chat(Message.getMessage(target_uuid.getUniqueId(), "kick_message").replace("{reason}", reason).replace("{prefix}", Message.getMessage(target_uuid.getUniqueId(), "prefix"))));
			} else if (target_name != null && target_name.isOnline()) {
				target_name.kickPlayer(Message.chat(Message.getMessage(target_name.getUniqueId(), "prefix") + Message.getMessage(target_name.getUniqueId(), "kick_message").replace("{reason}", reason).replace("{prefix}", Message.getMessage(target_name.getUniqueId(), "prefix"))));
			}
			return Message.getMessage(UUID.randomUUID(), "player_kick").replace("{player}", username_to).replace("{reason}", Message.chat(reason));
		} catch (SQLException ignored) {
			return Message.getMessage(UUID.randomUUID(), "kick_error").replace("{player}", username_to);
		}
	}

	public static String warnPlayer(String uuid_from, String username_from, String uuid_to, String username_to, String reason) {
		try {
			String query = "INSERT INTO adminbans_warned_players(uuid_from, username_from, uuid_to, username_to, reason, server, created) VALUES (?, ?, ?, ?, ?, ?, ?);";
			Connection conn = AdminBans.hikari.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, uuid_from);
			ps.setString(2, username_from);
			ps.setString(3, uuid_to);
			ps.setString(4, username_to);
			ps.setString(5, reason);
			ps.setString(6, server_name);
			ps.setString(7, date_format.format(new Date()));
			ps.executeUpdate();
			ps.close();
			conn.close();
			return Message.getMessage(UUID.randomUUID(), "player_warn").replace("{player}", username_to).replace("{reason}", Message.chat(reason));
		} catch (SQLException ignored) {
			return Message.getMessage(UUID.randomUUID(), "warn_error").replace("{player}", username_to);
		}
	}

	public static boolean unBanPlayer(String player) {
		if (isPlayerBanned(player)) {
			try {
				String query = "UPDATE adminbans_banned_players SET until = ? WHERE username_to = ?;";
				Date until = new Date(System.currentTimeMillis());
				Connection conn = AdminBans.hikari.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, date_format.format(until));
				ps.setString(2, player);
				ps.executeUpdate();
				ps.close();
				conn.close();
				return true;
			} catch (SQLException ignored) {
			}
		}
		return false;
	}

	public static boolean unBanPlayer(UUID player) {
		if (isPlayerBanned(player)) {
			try {
				String query = "UPDATE adminbans_banned_players SET until = ? WHERE uuid_to = ?;";
				Date until = new Date(System.currentTimeMillis());
				Connection conn = AdminBans.hikari.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, date_format.format(until));
				ps.setString(2, player.toString());
				ps.executeUpdate();
				ps.close();
				conn.close();
				return true;
			} catch (SQLException ignored) {
			}
		}
		return false;
	}

	public static boolean unBanIP(String ip) {
		if (isIPBanned(ip)) {
			try {
				String query = "DELETE FROM adminbans_banned_ips WHERE ip = ?;";
				Connection conn = AdminBans.hikari.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, ip);
				ps.executeUpdate();
				ps.close();
				conn.close();
				return true;
			} catch (SQLException ignored) {}
		}
		return false;
	}

	public static boolean unBanIP(String ip, String server) {
		if (isIPBanned(ip, server)) {
			try {
				String query = "DELETE FROM adminbans_banned_ips WHERE ip = ? AND server = ?;";
				Connection conn = AdminBans.hikari.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, ip);
				ps.setString(2, server);
				ps.executeUpdate();
				ps.close();
				conn.close();
				return true;
			} catch (SQLException ignored) {}
		}
		return false;
	}

	public static boolean unMutePlayer(String player) {
		if (isPlayerMuted(player)) {
			try {
				String query = "UPDATE adminbans_muted_players SET until = ? WHERE username_to = ?;";
				Date until = new Date(System.currentTimeMillis());
				Connection conn = AdminBans.hikari.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, date_format.format(until));
				ps.setString(2, player);
				ps.executeUpdate();
				ps.close();
				conn.close();
				return true;
			} catch (SQLException ignored) {}
		}
		return false;
	}

	public static boolean unMutePlayer(UUID player) {
		if (isPlayerMuted(player)) {
			try {
				String query = "UPDATE adminbans_muted_players SET until = ? WHERE uuid_to = ?;";
				Date until = new Date(System.currentTimeMillis());
				Connection conn = AdminBans.hikari.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ps.setString(1, date_format.format(until));
				ps.setString(2, player.toString());
				ps.executeUpdate();
				ps.close();
				conn.close();
				return true;
			} catch (SQLException ignored) {}
		}
		return false;
	}

	public static int getPlayerWarnsCount(String player) {
		AtomicInteger getWarnCount = new AtomicInteger(0);

		try {
			String query = "SELECT COUNT(*) AS warn_count FROM adminbans_warned_players WHERE uuid_to = ?;";
			Connection conn = AdminBans.hikari.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, player);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				getWarnCount.set(rs.getInt("warn_count"));
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException ignored) {}

		return getWarnCount.get();
	}

	public static int getPlayerWarnsCount(String player, String server) {
		AtomicInteger getWarnCount = new AtomicInteger(0);

		try {
			String query = "SELECT COUNT(*) AS warn_count FROM adminbans_warned_players WHERE uuid_to = ? AND server = ?;";
			Connection conn = AdminBans.hikari.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, player);
			ps.setString(2, server);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				getWarnCount.set(rs.getInt("warn_count"));
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException ignored) {}

		return getWarnCount.get();
	}

	public static int getPlayerWarnsCount(UUID uuid) {
		AtomicInteger getWarnCount = new AtomicInteger(0);

		try {
			String query = "SELECT COUNT(*) AS warn_count FROM adminbans_warned_players WHERE uuid_to = ?;";
			Connection conn = AdminBans.hikari.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				getWarnCount.set(rs.getInt("warn_count"));
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException ignored) {}

		return getWarnCount.get();
	}

	public static int getPlayerWarnsCount(UUID uuid, String server) {
		AtomicInteger getWarnCount = new AtomicInteger(0);

		try {
			String query = "SELECT COUNT(*) AS warn_count FROM adminbans_warned_players WHERE uuid_to = ? AND server = ?;";
			Connection conn = AdminBans.hikari.getConnection();
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1, uuid.toString());
			ps.setString(2, server);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				getWarnCount.set(rs.getInt("warn_count"));
			}
			rs.close();
			ps.close();
			conn.close();
		} catch (SQLException ignored) {}

		return getWarnCount.get();
	}

	public static ArrayList<BannedPlayer> getBannedPlayers() {
		ArrayList<BannedPlayer> tempBannedPlayers = new ArrayList<>();
		for (BannedPlayer bannedPlayer : bannedPlayers) {
			if (new Date(System.currentTimeMillis()).before(bannedPlayer.until) || bannedPlayer.until.getTime() == Long.MAX_VALUE)
				tempBannedPlayers.add(bannedPlayer);
		}
		return tempBannedPlayers;
	}

	public static ArrayList<BannedPlayer> getBannedPlayers(String server) {
		ArrayList<BannedPlayer> tempBannedPlayers = new ArrayList<>();
		for (BannedPlayer bannedPlayer : bannedPlayers) {
			if ((new Date(System.currentTimeMillis()).before(bannedPlayer.until) || bannedPlayer.until.getTime() == Long.MAX_VALUE) && bannedPlayer.server.equals(server))
				tempBannedPlayers.add(bannedPlayer);
		}
		return tempBannedPlayers;
	}

	public static ArrayList<BannedIP> getBannedIPs() {
		return bannedIPs;
	}

	public static ArrayList<BannedIP> getBannedIPs(String server) {
		ArrayList<BannedIP> tempBannedIPs = new ArrayList<>();
		for (BannedIP bannedIP : bannedIPs) {
			if (bannedIP.server.equals(server)) tempBannedIPs.add(bannedIP);
		}
		return tempBannedIPs;
	}

	public static ArrayList<MutedPlayer> getMutedPlayers() {
		ArrayList<MutedPlayer> tempMutedPlayers = new ArrayList<>();
		for (MutedPlayer mutedPlayer : mutedPlayers) {
			if (new Date(System.currentTimeMillis()).before(mutedPlayer.until)) tempMutedPlayers.add(mutedPlayer);
		}
		return tempMutedPlayers;
	}

	public static ArrayList<MutedPlayer> getMutedPlayers(String server) {
		ArrayList<MutedPlayer> tempMutedPlayers = new ArrayList<>();
		for (MutedPlayer mutedPlayer : mutedPlayers) {
			if (new Date(System.currentTimeMillis()).before(mutedPlayer.until) && mutedPlayer.server.equals(server))
				tempMutedPlayers.add(mutedPlayer);
		}
		return tempMutedPlayers;
	}

	protected static void fetchBannedPlayersFromDatabase() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(AdminBans.getInstance(), () -> {
			ArrayList<BannedPlayer> tempBannedPlayers = new ArrayList<>();
			String query = "SELECT * FROM adminbans_banned_players ORDER BY until DESC;";

			try {
				Connection conn = AdminBans.hikari.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					Timestamp until = rs.getTimestamp("until");
					Timestamp now = new Timestamp(System.currentTimeMillis());
					if (now.before(until) || until.getTime() == Long.MAX_VALUE) {
						tempBannedPlayers.add(new BannedPlayer(rs.getString("uuid_from"), rs.getString("username_from"), rs.getString("uuid_to"), rs.getString("username_to"), rs.getString("reason"), rs.getTimestamp("until"), rs.getString("server"), rs.getTimestamp("created")));
					}
				}
				rs.close();
				ps.close();
				conn.close();
			} catch (SQLException ignored) {}

			bannedPlayers = tempBannedPlayers;
		}, 0L, 20L * AdminBans.getInstance().getConf().getInt("fetch_banned_players", 15));
	}

	protected static void fetchBannedIPsFromDatabase() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(AdminBans.getInstance(), () -> {
			ArrayList<BannedIP> tempBannedIPs = new ArrayList<>();
			String query = "SELECT * FROM adminbans_banned_ips;";

			try {
				Connection conn = AdminBans.hikari.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					tempBannedIPs.add(new BannedIP(rs.getString("ip"), rs.getString("server")));
				}
				rs.close();
				ps.close();
				conn.close();
			} catch (SQLException ignored) {}

			bannedIPs = tempBannedIPs;
		}, 0L, 20L * AdminBans.getInstance().getConf().getInt("fetch_banned_ips", 15));
	}

	protected static void fetchMutedPlayersFromDatabase() {
		Bukkit.getScheduler().runTaskTimerAsynchronously(AdminBans.getInstance(), () -> {
			ArrayList<MutedPlayer> tempMutedPlayers = new ArrayList<>();
			String query = "SELECT * FROM adminbans_muted_players ORDER BY until DESC;";

			try {
				Connection conn = AdminBans.hikari.getConnection();
				PreparedStatement ps = conn.prepareStatement(query);
				ResultSet rs = ps.executeQuery();
				while (rs.next()) {
					Timestamp until = rs.getTimestamp("until");
					Timestamp now = new Timestamp(System.currentTimeMillis());
					if (now.before(until)) {
						tempMutedPlayers.add(new MutedPlayer(rs.getString("uuid_from"), rs.getString("username_from"), rs.getString("uuid_to"), rs.getString("username_to"), rs.getString("reason"), rs.getTimestamp("until"), rs.getString("server"), rs.getTimestamp("created")));
					}
				}
				rs.close();
				ps.close();
				conn.close();
			} catch (SQLException ignored) {}

			mutedPlayers = tempMutedPlayers;
		}, 0L, 20L * AdminBans.getInstance().getConf().getInt("fetch_muted_players", 15));
	}
}
