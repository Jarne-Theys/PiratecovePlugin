package tk.piratecove.GetServerInfo;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class IsSlimeChunk implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)){
            return false;
        }
        Player sender = (Player) commandSender;
        Location location = sender.getLocation();
        Chunk chunk = location.getChunk();
        commandSender.sendMessage(String.valueOf(chunk.isSlimeChunk()));
        return true;
    }
}
