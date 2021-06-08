package tk.piratecove.getServerInfo;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class getServerInfo extends JavaPlugin {
    private Collection getPlayers(){
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        return players;
    }

    @Override
    public void onEnable() {
        getLogger().info("getServerInfo has been enabled");
        this.getCommand("writePLayers").setExecutor(new writePlayers());

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getPlayers();
                getLogger().info("getServerInfo has collected all the players");
            }
        },600000,600000);

    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }
}