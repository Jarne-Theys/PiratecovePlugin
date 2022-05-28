package tk.piratecove;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Heal implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(strings.length !=1){
            commandSender.sendMessage(ChatColor.RED + "You must specify a target player!");
        }
        Player target = Bukkit.getPlayer(strings[0]);
        if(target!=null){
            if(target.getHealth()<20){
                target.setHealth(20);
            }
        }else{
            commandSender.sendMessage(ChatColor.RED + "That player is not online!");
        }
        return true;
    }
}
