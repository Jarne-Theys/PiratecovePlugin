package me.McSebi.TimberMP;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class TimberMP extends JavaPlugin {
		
	public static TimberMP plugin;
	public final Logger logger = Logger.getLogger("Minecraft");
	
	public final PluginBlockListener blockListener = new PluginBlockListener(this);
	
	public void onDisable() {
		PluginDescriptionFile pdfFile = this.getDescription();
		this.logger.info("[" + pdfFile.getName() + "]" + " v" +pdfFile.getVersion() + " by " + pdfFile.getAuthors() + " disabled.");
	}
	
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		PluginDescriptionFile pdfFile = this.getDescription();
		pm.registerEvents(blockListener, this);
		this.logger.info("[" + pdfFile.getName() + "]" + " v" +pdfFile.getVersion() + " by " + pdfFile.getAuthors() + " enabled.");
		getConfig().options().copyDefaults(true);
        saveConfig();
	}
	
	public boolean onCommand(CommandSender player, Command comm, String cmd, String[] args) {
		PluginDescriptionFile pdfFile = this.getDescription();
		Player p = (Player) player;
		if(cmd.equalsIgnoreCase("timbermp")) {
			p.sendMessage("Timber multiplayer port by " + pdfFile.getAuthors() + ".");
		}
		return false;
	}
}
