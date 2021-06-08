package tk.piratecove.getServerInfo;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

public class writePlayers implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Logger log = Bukkit.getLogger();
        if(!commandSender.getName().equalsIgnoreCase("OGCaptainCapsize") && commandSender instanceof Player){
            log.info(commandSender.getName() + " Tried to use the writePlayers command");
        }
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        try {
            File file = new File("players.txt");
            if (file.createNewFile()) {
                log.info("A new players file has been created");
            } else {
                log.info("A players file already exists");
            }
        } catch (IOException exception) {
            log.info("An IOException has occured during the creation of the players file");
        }
        try {
            FileWriter fileWriter = new FileWriter("players.txt");
            for (Object player : players) {
                if (player instanceof Player) {
                    Player player1 = (Player) player;
                    String playername = player1.getName();
                    String gamemode = player1.getGameMode().toString();
                    fileWriter.write("Playername: " + playername + " Gamemode: " + gamemode);
                    fileWriter.flush();
                    fileWriter.close();
                }
            }
        } catch (IOException exception) {
            log.info("An IOException has occured during the writing of the players file");

        }
        return true;
    }
}
