package tk.piratecove;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Explode implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] args) {
        World world = Bukkit.getServer().getWorlds().get(0);
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        assert targetPlayer != null;
        Location location = targetPlayer.getLocation();
        world.playSound(location, Sound.ENTITY_CREEPER_PRIMED, 2, 1);
        return true;
    }
}
