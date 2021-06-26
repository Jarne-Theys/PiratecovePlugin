package tk.piratecove;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class Home implements CommandExecutor {
    private PiratecovePlugin instance;
    public Home(PiratecovePlugin plugin){
        instance=plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            if (instance.getPlayerHomes().containsKey(sender)) {
                sender.teleport(instance.getPlayerHomes().get(sender));
            } else {
                sender.sendMessage(ChatColor.RED + "You must set a home first\nYou can do this using the /sethome command");
            }
            return true;
        }
        return true;
    }
}
