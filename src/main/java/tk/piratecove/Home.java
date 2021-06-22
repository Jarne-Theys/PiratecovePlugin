package tk.piratecove;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Home implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player sender = (Player) commandSender;
            Location targetLocation = sender.getBedSpawnLocation();
                if(targetLocation!=null){
                    sender.teleport(targetLocation);
                } else {
                    sender.sendMessage(ChatColor.GOLD + "You don't have a bed spawn location!");
                }
            return true;
        }
        return false;
    }
}
