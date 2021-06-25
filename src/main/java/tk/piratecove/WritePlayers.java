package tk.piratecove;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.logging.Logger;

public class WritePlayers implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Logger log = Bukkit.getLogger();
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        try {
            File file = new File("C:\\MCServerFiles\\players.txt");
            if (file.createNewFile()) {
                log.info("A new players file has been created");
            }
        } catch (IOException exception) {
            log.info("An IOException has occured during the creation of the players file");
        }
        try {
            FileWriter fileWriter = new FileWriter("C:\\MCServerFiles\\players.txt");
            for (Object player : players) {
                if (player instanceof Player) {
                    Player player1 = (Player) player;
                    String playername = player1.getName();
                    double health = player1.getHealth();
                    int lifetime = player1.getTicksLived() / 20;
                    fileWriter.write("Playername: " + playername + " Health: " + health + " Lifetime: " + lifetime);
                    fileWriter.flush();
                    fileWriter.close();
                }
            }
        } catch (IOException exception) {
            log.info("An IOException has occured during the writing of the players file");
        }
        log.info("Succesfully updated players file");
        return true;
    }
}
