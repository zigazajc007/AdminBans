package com.rabbitcompany.adminbans;

import com.rabbitcompany.adminbans.commands.*;
import com.rabbitcompany.adminbans.listeners.PlayerLoginListener;
import com.rabbitcompany.adminbans.listeners.PlayerMessageListener;
import com.rabbitcompany.adminbans.utils.Message;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public final class AdminBans extends JavaPlugin {

    private static AdminBans instance;

    String username = "%%__USERNAME__%%";
    String user_id = "%%__USER__%%";

    //Database
    public static HikariDataSource hikari;
    private static Connection conn = null;

    //Config
    private File co = null;
    private final YamlConfiguration conf = new YamlConfiguration();

    //Messages
    private File me = null;
    private final YamlConfiguration mess = new YamlConfiguration();

    @Override
    public void onEnable() {
        instance = this;
        this.co = new File(getDataFolder(), "config.yml");
        this.me = new File(getDataFolder(), "Messages.yml");

        mkdir();
        loadYamls();

        info("&aEnabling");

        //Database connection
        if(getConf().getBoolean("mysql", false)){
            setupMySQL();
        }else{
            setupH2();
        }

        //Listeners
        new PlayerLoginListener(this);
        new PlayerMessageListener(this);

        //Commands
        this.getCommand("adminbans").setExecutor(new com.rabbitcompany.adminbans.commands.AdminBans());
        this.getCommand("adminbans").setTabCompleter(new TabCompletion());
        this.getCommand("ban").setExecutor(new Ban());
        this.getCommand("ban").setTabCompleter(new TabCompletion());
        this.getCommand("unban").setExecutor(new Unban());
        this.getCommand("unban").setTabCompleter(new TabCompletion());
        this.getCommand("kick").setExecutor(new Kick());
        this.getCommand("kick").setTabCompleter(new TabCompletion());
        this.getCommand("warn").setExecutor(new Warn());
        this.getCommand("warn").setTabCompleter(new TabCompletion());
        this.getCommand("mute").setExecutor(new Mute());
        this.getCommand("mute").setTabCompleter(new TabCompletion());
        this.getCommand("unmute").setExecutor(new Unmute());
        this.getCommand("unmute").setTabCompleter(new TabCompletion());
        this.getCommand("banip").setExecutor(new BanIP());
        this.getCommand("ban-ip").setExecutor(new BanIP());
        this.getCommand("unbanip").setExecutor(new UnbanIP());
        this.getCommand("unbanip").setTabCompleter(new TabCompletion());
        this.getCommand("banlist").setExecutor(new BanList());
        this.getCommand("mutelist").setExecutor(new MuteList());

    }

    @Override
    public void onDisable() {
        info("&4Disabling");

        if(conn != null){
            try {
                conn.close();
            } catch (SQLException ignored) { }
        }
    }


    private void setupMySQL(){
        try {
            hikari = new HikariDataSource();
            hikari.setMaximumPoolSize(10);
            hikari.setJdbcUrl("jdbc:mysql://" + getConf().getString("mysql_host") + ":" + getConf().getString("mysql_port") + "/" + getConf().getString("mysql_database"));
            hikari.setUsername(getConf().getString("mysql_user"));
            hikari.setPassword(getConf().getString("mysql_password"));
            hikari.addDataSourceProperty("useSSL", getConf().getString("mysql_useSSL"));
            hikari.addDataSourceProperty("cachePrepStmts", "true");
            hikari.addDataSourceProperty("prepStmtCacheSize", "250");
            hikari.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            conn = hikari.getConnection();
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS adminbans_banned_players(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid_from CHAR(36) NOT NULL, username_from varchar(25) NOT NULL, uuid_to CHAR(36) NOT NULL, username_to varchar(25) NOT NULL, reason VARCHAR(255), until DATETIME NOT NULL, server VARCHAR(30) NOT NULL DEFAULT 'Global', created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS adminbans_banned_ips(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, ip varchar(15) NOT NULL, server VARCHAR(30) NOT NULL DEFAULT 'Global')");
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS adminbans_muted_players(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid_from CHAR(36) NOT NULL, username_from varchar(25) NOT NULL, uuid_to CHAR(36) NOT NULL, username_to varchar(25) NOT NULL, reason VARCHAR(255), until DATETIME NOT NULL, server VARCHAR(30) NOT NULL DEFAULT 'Global', created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS adminbans_warned_players(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid_from CHAR(36) NOT NULL, username_from varchar(25) NOT NULL, uuid_to CHAR(36) NOT NULL, username_to varchar(25) NOT NULL, reason VARCHAR(255), server VARCHAR(30) NOT NULL DEFAULT 'Global', created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS adminbans_kicked_players(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid_from CHAR(36) NOT NULL, username_from varchar(25) NOT NULL, uuid_to CHAR(36) NOT NULL, username_to varchar(25) NOT NULL, reason VARCHAR(255), server VARCHAR(30) NOT NULL DEFAULT 'Global', created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            conn.close();
        } catch (SQLException e) {
            setupH2();
        }
    }

    private void setupH2(){
        try {
            Class.forName("org.h2.Driver");
        }
        catch (ClassNotFoundException e) {
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }
        try {
            hikari = new HikariDataSource();
            hikari.setMaximumPoolSize(10);
            hikari.setJdbcUrl("jdbc:h2:./" + getDataFolder() + "/h2");
            hikari.setUsername("sa");
            hikari.setPassword("");
            hikari.addDataSourceProperty("cachePrepStmts", "true");
            hikari.addDataSourceProperty("prepStmtCacheSize", "250");
            hikari.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

            conn = hikari.getConnection();
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS adminbans_banned_players(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid_from CHAR(36) NOT NULL, username_from varchar(25) NOT NULL, uuid_to CHAR(36) NOT NULL, username_to varchar(25) NOT NULL, reason VARCHAR(255), until DATETIME NOT NULL, server VARCHAR(30) NOT NULL DEFAULT 'Global', created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS adminbans_banned_ips(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, ip varchar(15) NOT NULL, server VARCHAR(30) NOT NULL DEFAULT 'Global')");
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS adminbans_muted_players(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid_from CHAR(36) NOT NULL, username_from varchar(25) NOT NULL, uuid_to CHAR(36) NOT NULL, username_to varchar(25) NOT NULL, reason VARCHAR(255), until DATETIME NOT NULL, server VARCHAR(30) NOT NULL DEFAULT 'Global', created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS adminbans_warned_players(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid_from CHAR(36) NOT NULL, username_from varchar(25) NOT NULL, uuid_to CHAR(36) NOT NULL, username_to varchar(25) NOT NULL, reason VARCHAR(255), server VARCHAR(30) NOT NULL DEFAULT 'Global', created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            conn.createStatement().execute("CREATE TABLE IF NOT EXISTS adminbans_kicked_players(id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid_from CHAR(36) NOT NULL, username_from varchar(25) NOT NULL, uuid_to CHAR(36) NOT NULL, username_to varchar(25) NOT NULL, reason VARCHAR(255), server VARCHAR(30) NOT NULL DEFAULT 'Global', created DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP)");
            conn.close();

        } catch (SQLException e) {
            Bukkit.getConsoleSender().sendMessage(e.getMessage());
        }
    }

    private void mkdir(){

        if(!this.co.exists()){
            saveResource("config.yml", false);
        }

        if(!this.me.exists()){
            saveResource("Messages.yml", false);
        }

    }

    public void loadYamls(){
        try{
            this.conf.load(this.co);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        try{
            this.mess.load(this.me);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public YamlConfiguration getConf() { return this.conf; }

    public YamlConfiguration getMess() { return this.mess; }

    private void info(String message){
        Bukkit.getConsoleSender().sendMessage(Message.chat(""));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6[]=====[" + message + " &cAdminBans&6]=====[]"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6| &cInformation:"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Name: &bAdminBans"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Developer: &bBlack1_TV"));
        if(!username.contains("%%__")){
            Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Plugin owner: &b" + username));
        }else if(!user_id.contains("%%__")){
            Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Plugin owner: &b" + user_id));
        }else{
            Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Plugin owner: &4&lCRACKED"));
        }
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Version: &b1.1.0"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6| &cSupport:"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Discord: &bCrazy Rabbit#0001"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Mail: &bziga.zajc007@gmail.com"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|   &9Discord: &bhttps://discord.gg/hUNymXX"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6|"));
        Bukkit.getConsoleSender().sendMessage(Message.chat("&6[]=====================================[]"));
        Bukkit.getConsoleSender().sendMessage(Message.chat(""));
    }

    public static AdminBans getInstance(){ return instance; }

}
