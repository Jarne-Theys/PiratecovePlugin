package tk.piratecove.getServerInfo;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class getServerInfo extends JavaPlugin implements Listener {
    @Override
    public void onEnable() {
        getLogger().info("getServerInfo has been enabled");
        this.getCommand("writePLayers").setExecutor(new writePlayers());
        getServer().getPluginManager().registerEvents(this, this);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onJoin(PlayerJoinEvent event){
        getLogger().info("Player joined, updating player list...");
        writePlayers();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event){
        getLogger().info("Player left, updating player list...");
        Player player = event.getPlayer();
        writePlayers(player);
    }

    @Override
    public void onDisable() {
    }

    private void writePlayers(){
        writePlayers(null);
    }

    private void writePlayers(Player leavingPlayer){
        Logger log = Bukkit.getLogger();
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
        try {
            File file = new File("C:\\MCServerFiles\\players.txt");
            if (file.createNewFile()) {
                log.info("A new players file has been created");
            }
        } catch (IOException exception) {
            log.info("An IOException has occured during the creation of the players file");
        }
        try {
            String result = "";
            FileWriter fileWriter = new FileWriter("C:\\MCServerFiles\\players.txt");
            for (Object player : players) {
                if (player instanceof Player) {
                    Player player1 = (Player) player;
                    if(player1 != leavingPlayer){
                        String playername = player1.getName();
                        String gamemode = player1.getGameMode().toString();
                        result += ("Playername: " + playername + " Gamemode: " + gamemode + "\n");
                    }
                }
            }
            fileWriter.write(result);
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException exception) {
            log.info("An IOException has occured during the writing of the players file");
        }
    }
}