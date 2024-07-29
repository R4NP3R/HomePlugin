package com.ranper;

import com.ranper.commands.HomeCommand;
import com.ranper.commands.SetHomeCommand;
import com.ranper.db.Database;
import com.ranper.listeners.PlayerListeners;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class Home extends JavaPlugin {

    //array da fila
    private List<UUID> queue;
    private Database database;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.queue = new ArrayList<>();

        try {
            this.database = new Database();
            database.initializeDataBase();
        } catch (SQLException e) {
            System.out.println("Unable to connect to database and create table");
            e.printStackTrace();
        }

        registerCommands();
        registerListeners();
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public Database getDatabase() {
        return database;
    }

    private void registerCommands () {
        getCommand("sethome").setExecutor(new SetHomeCommand(this));
        getCommand("home").setExecutor(new HomeCommand(this));
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerListeners(this), this);
    }

    public void addQueue(UUID id) {
        this.queue.add(id);
    }
    public void cancelQueue(UUID id) {
        this.queue.remove(id);
    }
    public boolean isQued(UUID id) {
       return this.queue.contains(id);
    }

}
