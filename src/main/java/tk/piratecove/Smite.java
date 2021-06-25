package tk.piratecove;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Smite implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Location location = Bukkit.getPlayer(strings[0]).getLocation();
        World world = Bukkit.getWorlds().get(0);
        if (strings.length == 2) {
            world.strikeLightning(location);
        } else {
            world.strikeLightningEffect(location);
        }
        return true;
    }
}
