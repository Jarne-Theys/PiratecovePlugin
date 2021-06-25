package tk.piratecove;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import static tk.piratecove.PiratecovePlugin.playerHomes;

public class Home implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player sender = (Player) commandSender;
            if (playerHomes.containsKey(sender)) {
                sender.teleport(playerHomes.get(sender));
            } else {
                sender.sendMessage(ChatColor.RED + "You must set a home first\nYou can do this using the /sethome command");
            }
            return true;
        }
        return true;
    }
}
