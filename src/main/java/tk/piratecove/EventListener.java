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
import java.util.Iterator;
import java.util.Map;

import static org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult.OK;

public class EventListener implements Listener {

    @EventHandler
    public void playerBedEnterEvent(PlayerBedEnterEvent event) {
        Player requestingPlayer = event.getPlayer();
        PlayerBedEnterEvent.BedEnterResult bedEnterResult = event.getBedEnterResult();
        if (bedEnterResult == OK) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        Bukkit.getServer().getLogger().info("----------Error during thread pause----------");
                        Bukkit.broadcastMessage(ChatColor.GOLD + requestingPlayer.getName() + " has skipped the night");
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "time set 0");
                        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "weather clear");
                    }
                    Bukkit.broadcastMessage(ChatColor.GOLD + requestingPlayer.getName() + " has skipped the night");
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "time set 0");
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "weather clear");
                }
            };
            Thread thread = new Thread(runnable);
            thread.start();
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        ArrayList<String> registeredPlayers = new ArrayList<>();
        try {
            Object object = new JSONParser().parse(new FileReader("C:\\MCServerFiles\\achievements.json"));
            JSONObject jo = (JSONObject) object;
            for (Object string : jo.keySet()) {
                if(!registeredPlayers.contains(string.toString())){
                    registeredPlayers.add(string.toString());
                }
            }
        } catch (FileNotFoundException exception) {
            Bukkit.getServer().getLogger().info("Initialise custom achievements failed: File not found");
            Bukkit.getServer().broadcastMessage(ChatColor.MAGIC + "Custom achievements" + ChatColor.RED + " have been dissabled");
        } catch (IOException exception) {
            Bukkit.getServer().getLogger().info("Initialise custom achievements failed: An IOException has occured");
            Bukkit.getServer().broadcastMessage(ChatColor.MAGIC + "Custom achievements" + ChatColor.RED + " have been dissabled");
        } catch (ParseException e) {
            Bukkit.getServer().getLogger().info("Initialise custom achievements failed: A ParseException has occured");
            Bukkit.getServer().broadcastMessage(ChatColor.MAGIC + "Custom achievements" + ChatColor.RED + " have been dissabled");
        }
    }

}
