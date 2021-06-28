package tk.piratecove;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class Home implements CommandExecutor {
    private final PiratecovePlugin instance;
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
                Player capsize = Bukkit.getServer().getPlayer("OGCaptainCapsize");
                if(capsize!=null){
                    capsize.sendMessage(instance.getPlayerHomes().toString());
                }
                sender.sendMessage(ChatColor.RED + "You must set a home first\nYou can do this using the /sethome command");
            }
            return true;
        }
        return true;
    }
}
