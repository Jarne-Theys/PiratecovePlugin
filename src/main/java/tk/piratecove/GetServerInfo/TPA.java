package tk.piratecove.GetServerInfo;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPA implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(commandSender instanceof Player){
            Player sender = (Player) commandSender;
            for(String arg : strings){
                Player target = Bukkit.getServer().getPlayer(arg);
                if(target!=null){
                    target.sendMessage(ChatColor.GOLD + "" + sender.getName() + "Wants to teleport to you, do you accept?");
                    TextComponent Accept = new TextComponent("Accept ");
                    TextComponent Decline = new TextComponent(" Decline");

                    Accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/teleport " + sender.getName() + " " + target.getName()));
                    Decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,target.getName() + " has declined"));

                    target.spigot().sendMessage(Accept,Decline);
                }
            }
        }
        return false;
    }
}
