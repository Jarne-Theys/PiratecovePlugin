package tk.piratecove;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AchievementHandler implements Listener {
    private PiratecovePlugin instance;

    public AchievementHandler(PiratecovePlugin instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (instance.getConfig().getBoolean("enableCustomAchievements")) {
            ArrayList<String> registeredPlayers = new ArrayList<>();
            try {
                Object object = new JSONParser().parse(new FileReader("C:\\MCServerFiles\\achievements.json"));
                JSONObject jo = (JSONObject) object;
                for (Object string : jo.keySet()) {
                    if (!registeredPlayers.contains(string.toString())) {
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
}
