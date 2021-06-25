package tk.piratecove;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult.OK;

public class EventListener implements Listener {

    @EventHandler
    public void playerBedEnterEvent(PlayerBedEnterEvent event) {
        Player requestingPlayer = event.getPlayer();
        PlayerBedEnterEvent.BedEnterResult bedEnterResult = event.getBedEnterResult();
        if (bedEnterResult == OK) {
            Runnable runnable = () -> {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Bukkit.getServer().getLogger().info("----------Error during thread pause----------");
                    Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + requestingPlayer.getName() + "Capsize has done it again");
                }
                Bukkit.broadcastMessage(ChatColor.GOLD + requestingPlayer.getName() + " has skipped the night");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "time set day");
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "weather clear");
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }

}
