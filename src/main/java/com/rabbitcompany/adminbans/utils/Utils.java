package com.rabbitcompany.adminbans.utils;

import com.rabbitcompany.adminbans.AdminBans;
import com.rabbitcompany.adminbans.AdminBansAPI;

import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

public class Utils {

    public static String banReasonMessage(UUID target, String reason, String time){
        String bumper = org.apache.commons.lang.StringUtils.repeat("\n", 35);

        return bumper + Message.getMessage(target, "ban_message").replace("{reason}", reason).replace("{time}", time) + bumper;
    }

    public static String playerBannedMessage(UUID uuid){
        if(AdminBansAPI.isPlayerBanned(uuid)){
            String query = "SELECT * FROM adminbans_banned_players WHERE uuid_to = '" + uuid.toString() + "' ORDER BY until DESC;";
            AtomicReference<String> reason = new AtomicReference<>("");
            AtomicReference<Date> until = new AtomicReference<>(new Date(System.currentTimeMillis()));
            try {
                AdminBans.mySQL.query(query, results -> {
                    if (results.next()) {
                        until.set(results.getTimestamp("until"));
                        reason.set(results.getString("reason"));
                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return banReasonMessage(uuid, reason.get(), AdminBansAPI.date_format.format(until.get()));
        }else{
            return "";
        }
    }

    public static String stripNonDigits(final CharSequence input){
        final StringBuilder sb = new StringBuilder(input.length());
        for(int i = 0; i < input.length(); i++){
            final char c = input.charAt(i);
            if(c > 47 && c < 58){
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
