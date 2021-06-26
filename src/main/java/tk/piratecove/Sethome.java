package tk.piratecove;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class Sethome implements CommandExecutor {
    private PiratecovePlugin instance;
    public Sethome(PiratecovePlugin plugin){
        instance=plugin;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            if (sender.getWorld() == Bukkit.getServer().getWorlds().get(0)) {
                instance.getPlayerHomes().put(sender, sender.getLocation());
                return true;
            } else {
                sender.sendMessage(ChatColor.RED + "You can only set a home in the overworld");
                return true;
            }
        } else {
            return true;
        }
    }
}
