package tk.piratecove;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult.OK;

public class EventListener implements Listener {
    PiratecovePlugin instance;
    public EventListener(PiratecovePlugin instance){
        this.instance=instance;
    }

    @EventHandler
    public void playerBedEnterEvent(PlayerBedEnterEvent event) {
        Player requestingPlayer = event.getPlayer();
        PlayerBedEnterEvent.BedEnterResult bedEnterResult = event.getBedEnterResult();
        if (bedEnterResult == OK) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "time set day");
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "weather clear");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Bukkit.getServer().broadcastMessage("Currently working commands:\n/sethome - /home - /sunny - /isSlimeChunk\nWork in progress: Heal");
        instance.writePlayers();
        instance.readPlayerHomes();
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event){
        instance.writePlayers(event.getPlayer());
        instance.writePlayerHomes();
    }

}
