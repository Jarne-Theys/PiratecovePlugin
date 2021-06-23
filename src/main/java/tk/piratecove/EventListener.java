package tk.piratecove;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.LinkedList;

import static org.bukkit.event.player.PlayerBedEnterEvent.BedEnterResult.OK;
import static tk.piratecove.PiratecovePlugin.initialised;

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
/*
    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        if(!initialised){
            World world = Bukkit.getServer().getWorlds().get(0);
            Bukkit.getServer().getLogger().info("Player spawn locations not force loaded yet!");
            Bukkit.getServer().getLogger().info("Force loading CaptainCapsize farms...");
            LinkedList<Chunk> forceLoadChunks = new LinkedList();
            Chunk AythrixBed = new Location(world, 72.000, 64.000, -97.000).getChunk();
            Chunk CapsizeBed = new Location(world, -595.000, 84.000, 210.000).getChunk();
            Chunk CapsizeIronFarm1 = new Location(world, -546.000, 79.000, -204.000).getChunk();
            Chunk CapsizeIronFarm2 = new Location(world, -540.000, 79.000, -204.000).getChunk();
            Chunk CapsizeSugarFarm1 = new Location(world, -554.405, 78.000, 244.000).getChunk();
            Chunk CapsizeSugarFarm2 = new Location(world, -567.000, 75.000, 244.000).getChunk();
            Chunk CapsizeSheepFarm = new Location(world, -581.000, 79.000, 246.000).getChunk();
            Chunk CapsizeNetherWartFarm = new Location(world,-573.000,77.000,217.000).getChunk();
            forceLoadChunks.add(AythrixBed);
            forceLoadChunks.add(CapsizeBed);
            forceLoadChunks.add(CapsizeIronFarm1);
            forceLoadChunks.add(CapsizeIronFarm2);
            forceLoadChunks.add(CapsizeSugarFarm1);
            forceLoadChunks.add(CapsizeSugarFarm2);
            forceLoadChunks.add(CapsizeSheepFarm);
            forceLoadChunks.add(CapsizeNetherWartFarm);
            for (Chunk chunk : forceLoadChunks) {
                if(!chunk.isForceLoaded()){
                    chunk.setForceLoaded(true);
                } else {
                    Bukkit.getServer().getLogger().info("Chunks have already been forceloaded!");
                }
            }
        }
    }
    */
}
