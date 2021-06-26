package tk.piratecove;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//TODO READ HOME VALUES FROM FILE ON STARTUP

public class PiratecovePlugin extends JavaPlugin {
    private Map<String, Map<String, String>> customAchievements;

    ArrayList<String> achievementNames = new ArrayList<>() {{
        //-----Player events-----
        add("Mob kills");
        add("Blocks broken");
        add("Eggs thrown");
        add("Portals used");
        add("Deaths");
        add("Raids triggered");

        add("Horni bonks");
        //-----Entity events-----
        add("Animals bred");
        add("Animals tamed");
        add("Piglin trades");

        //-----World events-----

        /*
        -----Website-----
        World - Coordinaten
        Dynmap?
         */
    }};

    static Map<String, Long> tpaCooldown = new HashMap<String, Long>();
    static Map<String, String> currentRequest = new HashMap<String, String>();

    public Map<Player, Location> playerHomes = new HashMap<>();

    @Override
    public void onEnable() {
        loadConfig();

        this.getCommand("writePLayers").setExecutor(new WritePlayers());
        this.getCommand("explode").setExecutor(new Explode());
        this.getCommand("isSlimeChunk").setExecutor(new IsSlimeChunk());
        this.getCommand("sethome").setExecutor(new Sethome(this));
        this.getCommand("home").setExecutor(new Home(this));
        this.getCommand("smite").setExecutor(new Smite());
        this.getCommand("sunny").setExecutor(new Sunny());
        this.getCommand("tpa").setExecutor(this);
        this.getCommand("tpaccept").setExecutor(this);
        this.getCommand("tpdeny").setExecutor(this);

        getServer().getPluginManager().registerEvents(new EventListener(), this);
        getServer().getPluginManager().registerEvents(new PluginBlockListener(), this);
        getServer().getPluginManager().registerEvents(new AchievementHandler(this), this);

        if (getConfig().getBoolean("enableCustomAchievements")) {
            customAchievements = setCustomAchievements();
        }
        getLogger().info("Enabled PiratecovePlugin");
    }

    public Map<String, Map<String, String>> setCustomAchievements() {
        OfflinePlayer[] allPlayers = Bukkit.getServer().getOfflinePlayers();
        Player captain = Bukkit.getServer().getPlayer("OGCaptainCapsize");
        Map<String, Map<String, String>> result = new HashMap<>();
        Map<String, String> newAchievements = new HashMap<>();
        String currentPlayer;
        String currentAchievementKey;
        String currentAchievementValue;
        try {
            Object object = new JSONParser().parse(new FileReader("C:\\MCServerFiles\\achievements.json"));
            JSONObject jo = (JSONObject) object;
            for (Object player : jo.keySet()) {
                Map Fileachievements = (Map) jo.get(player);
                Iterator<Map.Entry> itr1 = Fileachievements.entrySet().iterator();
                currentPlayer = (String) player;
                while (itr1.hasNext()) {
                    var pair = itr1.next();
                    currentAchievementKey = (String) pair.getKey();
                    currentAchievementValue = (String) pair.getValue();
                    newAchievements.put(currentAchievementKey, currentAchievementValue);
                }
                for (String achievement : achievementNames) {
                    if (!newAchievements.containsKey(achievement)) {
                        newAchievements.put(achievement, "0");
                    }
                }
                result.put(currentPlayer, newAchievements);
            }
            for (OfflinePlayer player : allPlayers) {
                    if (!result.containsKey(player.getName())) {
                        Map<String, String> newPlayerAchievements = new HashMap<>();
                        for (String achievement : achievementNames) {
                            newPlayerAchievements.put(achievement, "0");
                        }
                        result.put(player.getName(), newPlayerAchievements);
                    }

            }
        } catch (FileNotFoundException exception) {
            getLogger().info("Initialise custom achievements failed: File not found");
            if (captain != null) {
                captain.sendMessage(ChatColor.RED + "Custom achievements have been disabled: FileNotFoundException");
            } else {
                Bukkit.getServer().broadcastMessage(ChatColor.RED + "Custom achievements have been disabled due to an error.");
            }
        } catch (IOException exception) {
            getLogger().info("Initialise custom achievements failed: An IOException has occured");
            if (captain != null) {
                captain.sendMessage(ChatColor.RED + "Custom achievements have been disabled: IOException");
            } else {
                Bukkit.getServer().broadcastMessage(ChatColor.RED + "Custom achievements have been disabled due to an error.");
            }
        } catch (ParseException e) {
            getLogger().info("Initialise custom achievements failed: A ParseException has occured");
            if (captain != null) {
                captain.sendMessage(ChatColor.RED + "Custom achievements have been disabled: ParseException");
            } else {
                Bukkit.getServer().broadcastMessage(ChatColor.RED + "Custom achievements have been disabled due to an error.");
            }
        }
        return result;
    }

    public Map<String, Map<String, String>> getCustomAchievements() {
        return customAchievements;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        //Reload config file
        if (cmd.getName().equalsIgnoreCase("piratecoveplugin")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "Usage: piratecoveplugin /<command>");
            }
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(ChatColor.GOLD + "Reloaded piratecoveplugin config");
                    reloadConfig();
                    return true;
                }
            }
        }
        return tpaRequestHandler(sender,cmd,commandLabel,args);
    }


    @Override
    public void onDisable() {
        writePlayerHomes();
        getLogger().info("Disabled PiratecovePlugin");
    }


    /*
    private void writePlayers() {
        writePlayers(null);
    }

    private void writePlayers(Player leavingPlayer) {
        Logger log = Bukkit.getLogger();
        Collection<? extends Player> players = Bukkit.getServer().getOnlinePlayers();
            File file = new File("C:\\MCServerFiles\\players.txt");
        try {
            FileWriter fileWriter = new FileWriter("C:\\MCServerFiles\\players.txt");
            for (Object player : players) {
                if (player instanceof Player) {
                    Player player1 = (Player) player;
                    if (player1 != leavingPlayer) {
                        String playername = player1.getName();
                        double health = player1.getHealth();
                        int lifetime = player1.getTicksLived() / 20;
                        fileWriter.write("Playername: " + playername + " Health: " + health + " Lifetime: " + lifetime);
                        fileWriter.flush();
                        fileWriter.close();
                    }
                }
            }
        } catch (IOException exception) {
            log.info("An IOException has occured during the writing of the players file");
        }
    }
*/
    private boolean tpaRequestHandler(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String commandLabel, String[] args) {
        Player p = null;
        if (sender instanceof Player) {
            p = (Player) sender;
        }

        if (cmd.getName().equalsIgnoreCase("tpa")) {
            if (!(p == null)) {
                int cooldown = 60;
                if (tpaCooldown.containsKey(p.getName())) {
                    long diff = (System.currentTimeMillis() - tpaCooldown.get(sender.getName())) / 1000;
                    if (diff < cooldown) {
                        p.sendMessage(ChatColor.RED + "Error: You must wait a " + cooldown + " second cooldown in between teleport requests!");
                        return false;
                    }
                }
                if (args.length == 1) {
                    final Player target = getServer().getPlayer(args[0]);
                    long keepAlive = 30 * 20;

                    if (target == null) {
                        sender.sendMessage(ChatColor.RED + "Error: You can only send a teleport request to online players!");
                        return false;
                    }

                    if (target == p) {
                        sender.sendMessage(ChatColor.RED + "Error: You can't teleport to yourself!");
                        return false;
                    }

                    sendRequest(p, target);

                    getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                        public void run() {
                            killRequest(target.getName());
                        }
                    }, keepAlive);
                    tpaCooldown.put(p.getName(), System.currentTimeMillis());
                } else {
                    p.sendMessage("Your command has to many arguments!");
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Error: The console can't teleport to people, silly!");
                return false;
            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("tpaccept")) {
            if (!(p == null)) {
                if (currentRequest.containsKey(p.getName())) {

                    Player heIsGoingOutOnADate = getServer().getPlayer(currentRequest.get(p.getName()));
                    currentRequest.remove(p.getName());

                    if (!(heIsGoingOutOnADate == null)) {
                        heIsGoingOutOnADate.teleport(p);
                        p.sendMessage(ChatColor.GRAY + "Teleporting...");
                        heIsGoingOutOnADate.sendMessage(ChatColor.GRAY + "Teleporting...");
                    } else {
                        sender.sendMessage(ChatColor.RED + "Error: It appears that the person trying to teleport to you doesn't exist anymore. WHOA!");
                        return false;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Error: It doesn't appear that there are any current tp requests. Maybe it timed out?");
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Error: The console can't accept teleport requests, silly!");
                return false;
            }
            return true;
        }

        if (cmd.getName().equalsIgnoreCase("tpdeny")) {
            if (!(p == null)) {
                if (currentRequest.containsKey(p.getName())) {
                    Player poorRejectedGuy = getServer().getPlayer(currentRequest.get(p.getName()));
                    currentRequest.remove(p.getName());

                    if (!(poorRejectedGuy == null)) {
                        poorRejectedGuy.sendMessage(ChatColor.RED + p.getName() + " rejected your teleport request! :(");
                        p.sendMessage(ChatColor.GRAY + poorRejectedGuy.getName() + " was rejected!");
                        return true;
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Error: It doesn't appear that there are any current tp requests. Maybe it timed out?");
                    return false;
                }
            } else {
                sender.sendMessage(ChatColor.RED + "Error: The console can't deny teleport requests, silly!");
                return false;
            }
            return true;
        }
        return false;
    }

    public void killRequest(String key) {
        if (currentRequest.containsKey(key)) {
            Player loser = getServer().getPlayer(currentRequest.get(key));
            if (!(loser == null)) {
                loser.sendMessage(ChatColor.RED + "Your teleport request timed out.");
            }
            currentRequest.remove(key);
        }
    }

    public void sendRequest(Player sender, Player recipient) {
        sender.sendMessage("Sending a teleport request to " + recipient.getName() + ".");

        String sendtpaccept = "";
        String sendtpdeny = "";

        sendtpaccept = " To accept the teleport request, type " + ChatColor.GOLD + "/tpaccept" + ChatColor.RESET + ".";
        sendtpdeny = " To deny the teleport request, type " + ChatColor.GOLD + "/tpdeny" + ChatColor.RESET + ".";


        recipient.sendMessage(ChatColor.GOLD + sender.getName() + ChatColor.RESET + " has sent a request to teleport to you." + sendtpaccept + sendtpdeny);
        currentRequest.put(recipient.getName(), sender.getName());
    }

    public void loadConfig() {
        getConfig().addDefault("enableCustomAchievements", false);
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public Map<Player, Location> getPlayerHomes(){
        return playerHomes;
    }

    public void writePlayerHomes(){
        try {
            for(Map.Entry<Player,Location> entry : playerHomes.entrySet()){
                String playername = entry.getKey().getName();
                String homeLocation = entry.getValue().getWorld().getName() + ":" + entry.getValue().getBlockX() + ":" + entry.getValue().getBlockY() + ":" + entry.getValue().getBlockZ();
                FileWriter fileWriter = new FileWriter("C:\\MCServerFiles\\playerHomes.txt");
                fileWriter.write(playername + "-" + homeLocation);
                fileWriter.flush();
                fileWriter.close();
            }
        } catch (IOException exception) {
            Bukkit.getLogger().info("An IOException has occured during the writing of the players homes");
        }
    }
}