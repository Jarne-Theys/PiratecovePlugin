package tk.piratecove.GetServerInfo;

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
            World overworld = Bukkit.getServer().getWorlds().get(0);
            World world = sender.getWorld();
            if(overworld==world){
                Location spawnlocation = sender.getBedSpawnLocation();
                if(spawnlocation!=null){
                    sender.teleport(spawnlocation);
                } else {
                    sender.sendMessage(ChatColor.GOLD + "You don't have a bed spawn location!");
                }
            } else {
                sender.sendMessage(ChatColor.GOLD + "You can only use this command in the overworld!");
            }
            return true;
        }
        return false;
    }
}
