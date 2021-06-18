package tk.piratecove;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

import static org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult.NOT_SAFE;
import static org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult.OK;

public class GetServerInfo extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        this.getCommand("writePLayers").setExecutor(new WritePlayers());
        this.getCommand("explode").setExecutor(new Explode());
        this.getCommand("isSlimeChunk").setExecutor(new IsSlimeChunk());
        this.getCommand("home").setExecutor(new Home());
        this.getCommand("smite").setExecutor(new Smite());
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event) {
        getLogger().info("Player joined, updating player list...");
        writePlayers();

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        getLogger().info("Player left, updating player list...");
        Player player = event.getPlayer();
        writePlayers(player);
    }

    @Override
    public void onDisable() {
        getLogger().info("I died");
    }

    private void writePlayers() {
        writePlayers(null);
    }

    private void writePlayers(Player leavingPlayer) {
        Logger log = Bukkit.getLogger();
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
            File file = new File("C:\\MCServerFiles\\players.txt");
        try {
            FileWriter fileWriter = new FileWriter("C:\\MCServerFiles\\players.txt");
            for (Object player : players) {
                if (player instanceof Player) {
                    Player player1 = (Player) player;
                    if (player1 != leavingPlayer) {
                        String playername = player1.getName();
                        double health = player1.getHealth();
                        int lifetime = player1.getTicksLived() / 20;
                        fileWriter.write("Playername: " + playername + " Health: " + health + " Lifetime: " + lifetime);
                        fileWriter.flush();
                        fileWriter.close();
                    }
                }
            }
        } catch (IOException exception) {
            log.info("An IOException has occured during the writing of the players file");
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSleep(PlayerBedEnterEvent event) {
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        for (Object player : players) {
            if (player instanceof Player) {
                World world = event.getPlayer().getWorld();
                Player requestingPlayer = event.getPlayer();
                PlayerBedEnterEvent.BedEnterResult bedEnterResult = event.getBedEnterResult();
                boolean isNight = (world.getTime() >= 12516);

                if (isNight && bedEnterResult == OK) {
                    Bukkit.broadcastMessage(ChatColor.GOLD + requestingPlayer.getName() + " has skipped the night");
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "time set 0");
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),"weather clear");
                }
            }
        }
    }
}