package tk.piratecove;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class PiratecovePlugin extends JavaPlugin{

    public final PluginBlockListener blockListener = new PluginBlockListener(this);
    public static boolean initialised = false;


    static Map<String, Long> tpaCooldown = new HashMap<String, Long>();
    static Map<String, String> currentRequest = new HashMap<String, String>();

    @Override
    public void onEnable() {
        this.getCommand("writePLayers").setExecutor(new WritePlayers());
        this.getCommand("explode").setExecutor(new Explode());
        this.getCommand("isSlimeChunk").setExecutor(new IsSlimeChunk());
        this.getCommand("home").setExecutor(new Home());
        this.getCommand("smite").setExecutor(new Smite());
        this.getCommand("tpa").setExecutor(this::onCommand);
        this.getCommand("tpaccept").setExecutor(this::onCommand);
        this.getCommand("tpdeny").setExecutor(this::onCommand);


        getServer().getPluginManager().registerEvents(new EventListener(),this);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
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

                if (args.length > 0) {
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
                    p.sendMessage("Send a teleport request to a player.");
                    p.sendMessage("/tpa <player>");
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

    public boolean killRequest(String key) {
        if (currentRequest.containsKey(key)) {
            Player loser = getServer().getPlayer(currentRequest.get(key));
            if (!(loser == null)) {
                loser.sendMessage(ChatColor.RED + "Your teleport request timed out.");
            }

            currentRequest.remove(key);

            return true;
        } else {
            return false;
        }
    }

    public void sendRequest(Player sender, Player recipient) {
        sender.sendMessage("Sending a teleport request to " + recipient.getName() + ".");

        String sendtpaccept = "";
        String sendtpdeny = "";

        sendtpaccept = " To accept the teleport request, type " + ChatColor.RED + "/tpaccept" + ChatColor.RESET + ".";
        sendtpdeny = " To deny the teleport request, type " + ChatColor.RED + "/tpdeny" + ChatColor.RESET + ".";


        recipient.sendMessage(ChatColor.RED + sender.getName() + ChatColor.RESET + " has sent a request to teleport to you." + sendtpaccept + sendtpdeny);
        currentRequest.put(recipient.getName(), sender.getName());
    }


    @Override
    public void onDisable() {
        getLogger().info("I died");
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

}