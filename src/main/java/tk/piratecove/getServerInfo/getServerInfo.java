package tk.piratecove.getServerInfo;

import org.bukkit.plugin.java.JavaPlugin;

public class getServerInfo extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("getServerInfo has been enabled");
        this.getCommand("writePLayers").setExecutor(new writePlayers());
    }

    @Override
    public void onDisable() {
        getLogger().info("onDisable is called!");
    }
}