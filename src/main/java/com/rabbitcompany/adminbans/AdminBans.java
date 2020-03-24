package com.rabbitcompany.adminbans;

import com.rabbitcompany.adminbans.commands.*;
import com.rabbitcompany.adminbans.listeners.PlayerLoginListener;
import com.rabbitcompany.adminbans.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pro.husk.mysql.MySQL;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public final class AdminBans extends JavaPlugin {

    private static AdminBans instance;

    //SQL
    public static MySQL mySQL;
    public static Connection conn = null;

    //Config
    private File co = null;
    private YamlConfiguration conf = new YamlConfiguration();

    @Override
    public void onEnable() {
        instance = this;
        this.co = new File(getDataFolder(), "config.yml");

        mkdir();
        loadYamls();

        info("&aEnabling");

        //SQL
        if(getConf().getBoolean("mysql", false)){
            try {
                mySQL = new MySQL(getConf().getString("mysql_host"), getConf().getString("mysql_port"), getConf().getString("mysql_database"), getConf().getString("mysql_user"), getConf().getString("mysql_password"), "?useSSL=" + getConf().getBoolean("mysql_useSSL") +"&allowPublicKeyRetrieval=true");
                conn = mySQL.getConnection();
                conn.createStatement().execute("CREATE TABLE IF NOT EXISTS admin_gui_banned_players(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid_from CHAR(36) NOT NULL, username_from varchar(25) NOT NULL, uuid_to CHAR(36) NOT NULL, username_to varchar(25) NOT NULL, reason VARCHAR(255), until DATETIME NOT NULL, created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");
                conn.createStatement().execute("CREATE TABLE IF NOT EXISTS admin_gui_muted_players(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid_from CHAR(36) NOT NULL, username_from varchar(25) NOT NULL, uuid_to CHAR(36) NOT NULL, username_to varchar(25) NOT NULL, reason VARCHAR(255), until DATETIME NOT NULL, created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");
                conn.createStatement().execute("CREATE TABLE IF NOT EXISTS admin_gui_warned_players(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid_from CHAR(36) NOT NULL, username_from varchar(25) NOT NULL, uuid_to CHAR(36) NOT NULL, username_to varchar(25) NOT NULL, reason VARCHAR(255), created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");
                conn.createStatement().execute("CREATE TABLE IF NOT EXISTS admin_gui_kicked_players(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid_from CHAR(36) NOT NULL, username_from varchar(25) NOT NULL, uuid_to CHAR(36) NOT NULL, username_to varchar(25) NOT NULL, reason VARCHAR(255), created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            } catch (SQLException e) {
                conn = null;
                Bukkit.getConsoleSender().sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "mysql_not_connected"));
                Bukkit.getPluginManager().disablePlugin(this);
            }
        }else{
            Bukkit.getConsoleSender().sendMessage(Message.getMessage(UUID.randomUUID(), "prefix") + Message.getMessage(UUID.randomUUID(), "mysql_not_connected"));
            Bukkit.getPluginManager().disablePlugin(this);
        }

        //Listeners
        new PlayerLoginListener(this);

        //Commands
        this.getCommand("ban").setExecutor(new Ban());
        this.getCommand("ban").setTabCompleter(new TabCompletion());
        this.getCommand("unban").setExecutor(new Unban());
        this.getCommand("unban").setTabCompleter(new TabCompletion());
        this.getCommand("kick").setExecutor(new Kick());
        this.getCommand("kick").setTabCompleter(new TabCompletion());
        this.getCommand("mute").setExecutor(new Mute());
        this.getCommand("mute").setTabCompleter(new TabCompletion());
        this.getCommand("unmute").setExecutor(new Unmute());
        this.getCommand("unmute").setTabCompleter(new TabCompletion());
    }

    @Override
    public void onDisable() {
        info("&4Disabling");

        //SQL
        if(conn != null){
            try {
                conn.close();
            } catch (SQLException ignored) { }
        }
    }


    private void mkdir(){

        if(!this.co.exists()){
            saveResource("config.yml", false);
        }

    }

    public void loadYamls(){
        try{
            this.conf.load(this.co);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getConf() { return this.conf; }

    private void info(String message){
        Bukkit.getConsoleSender().sendMessage(Message.chat(""));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8[]=====[" + message + " &cAdminBans&8]=====[]"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8| &cInformation:"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Name: &bAdminBans"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Developer: &bBlack1_TV"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Version: &b1.0.0"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8| &cSupport:"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Discord: &bCrazy Rabbit#0001"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Mail: &bziga.zajc007@gmail.com"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|   &9Discord: &bhttps://discord.gg/hUNymXX"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&8[]=====================================[]"));
        Bukkit.getConsoleSender().sendMessage(Message.chat(""));
    }

    public static AdminBans getInstance(){ return instance; }

}
