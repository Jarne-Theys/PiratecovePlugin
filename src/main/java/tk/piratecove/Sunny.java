package tk.piratecove;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class Sunny implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "weather clear");
        Bukkit.getServer().broadcastMessage(ChatColor.GOLD + "" + commandSender.getName() + " has made the rain go away");
        return true;
    }
}
