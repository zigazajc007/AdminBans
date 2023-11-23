package com.rabbitcompany.adminbans.utils;

import com.rabbitcompany.adminbans.AdminBansAPI;
import java.util.UUID;

public class Utils {

	public static String banReasonMessage(UUID target, String reason, String time) {
		if (time.equals("9999-12-31 23:59:59")) {
			if (reason == null || reason.equals("null")) {
				return Message.getMessage(target, "ban_message_perm_no_reason");
			} else {
				return Message.getMessage(target, "ban_message_perm").replace("{reason}", reason);
			}
		} else if (reason == null || reason.equals("null")) {
			return Message.getMessage(target, "ban_message_no_reason").replace("{time}", time);
		} else {
			return Message.getMessage(target, "ban_message").replace("{reason}", reason).replace("{time}", time);
		}
	}

	public static String banIPMessage() {
		return Message.getMessage(UUID.randomUUID(), "ip_ban_message");
	}

	public static String playerBannedMessage(UUID uuid) {
		for (BannedPlayer bannedPlayer : AdminBansAPI.getBannedPlayers()) {
			if (bannedPlayer.uuid_to.equals(uuid.toString()))
				return banReasonMessage(uuid, bannedPlayer.reason, AdminBansAPI.date_format.format(bannedPlayer.until));
		}
		return "";
	}

	public static String stripNonDigits(final CharSequence input) {
		final StringBuilder sb = new StringBuilder(input.length());
		for (int i = 0; i < input.length(); i++) {
			final char c = input.charAt(i);
			if (c > 47 && c < 58) {
				sb.append(c);
			}
		}
		return sb.toString();
	}

}
