package tk.piratecove;

import java.util.ArrayList;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

public class PluginBlockListener implements Listener {

    ArrayList<Material> logs = new ArrayList<>() {{
        add(Material.ACACIA_LOG);
        add(Material.BIRCH_LOG);
        add(Material.JUNGLE_LOG);
        add(Material.OAK_LOG);
        add(Material.SPRUCE_LOG);
        add(Material.DARK_OAK_LOG);
        add(Material.CRIMSON_STEM);
        add(Material.WARPED_STEM);
    }};

    ArrayList<Material> leaves = new ArrayList<>() {{
        add(Material.ACACIA_LEAVES);
        add(Material.BIRCH_LEAVES);
        add(Material.JUNGLE_LEAVES);
        add(Material.OAK_LEAVES);
        add(Material.DARK_OAK_LEAVES);
        add(Material.SPRUCE_LEAVES);
    }};

    public static PiratecovePlugin plugin;
    public PluginBlockListener(PiratecovePlugin instance) {
        plugin = instance;
    }

    @EventHandler
    public void onBlockBreak (BlockBreakEvent event) {
        if(logs.contains(event.getBlock().getType()) && event.getPlayer().getInventory().getItemInMainHand().getType().name().toLowerCase().contains("axe") && !event.getPlayer().getInventory().getItemInMainHand().getType().name().toLowerCase().contains("pickaxe")) {

            Player player = event.getPlayer();
            ItemStack handItem = player.getInventory().getItemInMainHand();
            FileConfiguration conf = plugin.getConfig();
            World world = player.getWorld();
            Location blockLocation = event.getBlock().getLocation();

            double x = blockLocation.getBlockX();
            double y = blockLocation.getBlockY();
            double y2 = blockLocation.getBlockY();
            double z = blockLocation.getBlockZ();

            int height = 0;

            boolean ground = false;

            while(!ground) {
                y--;
                y2--;
                Location blockBelow = new Location(world, x, y-1, z);
                Material blockBelowType = blockBelow.getBlock().getType();

                if(!logs.contains(blockBelowType)) {
                    ground = true;
                }
            }

            boolean logsLeft = true;
            boolean isTree = false;

            while(logsLeft) {
                y++;
                height++;

                Location blockAbove = new Location(world, x, y, z);
                Material blockAboveType = blockAbove.getBlock().getType();
                if(logs.contains(blockAboveType)) {
                    if(player.getGameMode().equals(GameMode.SURVIVAL)) {
                        blockAbove.getBlock().breakNaturally();

                        int enchLvl = handItem.getEnchantmentLevel(Enchantment.DURABILITY);
                        long random = Math.round((Math.random()*enchLvl));

                        if(random != 0) {
                           Damageable damageableHandItem = (Damageable) handItem.getItemMeta();
                           int currentDamage = damageableHandItem.getDamage();
                           damageableHandItem.setDamage(currentDamage + 1);
                        }
                    }

                    logsLeft = true;
                } else if(leaves.contains(blockAboveType)) {
                    logsLeft = false;
                    isTree = true;
                } else {
                    logsLeft = false;
                    isTree = false;
                }
            }



            //TODO BELOW



            //If the broken stem is a tree, the following will be executed
            if (isTree) {
                event.setCancelled(true);

                //SYSTEM FOR SMALL TREE REMOVAL
                for(int xCount = -2; xCount <= 2; xCount++) {
                    for(int yCount = 0; yCount <= height + 2; yCount++) {
                        for(int zCount = -2; zCount <= 2; zCount++) {

                            Location surround = new Location(world, x + xCount, y2 + yCount, z + zCount);
                            Material surroundType = surround.getBlock().getType();

                            if(logs.contains(surroundType)) {

                                if(!player.getGameMode().equals(GameMode.CREATIVE)) {
                                    surround.getBlock().breakNaturally();
                                } else {
                                    surround.getBlock().setType(Material.AIR);
                                }


                                if(conf.getBoolean("playSmokeEffect")) {
                                    player.getWorld().playEffect(surround, Effect.SMOKE, 4);
                                }

                                if(conf.getBoolean("damageAxe") && !player.getGameMode().equals(GameMode.CREATIVE)) {
                                    int enchLvl = handItem.getEnchantmentLevel(Enchantment.DURABILITY);
                                    long random = Math.round((Math.random()*enchLvl));

                                    if(random == 0) {
                                        if(handItem.getType().getMaxDurability() > handItem.getDurability()) {
                                            handItem.setDurability((short) (handItem.getDurability() + 1));
                                        } else if(handItem.getDurability() == handItem.getType().getMaxDurability()) {
                                            player.getInventory().setItemInHand(null);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //SYSTEM FOR BIG TREE REMOVAL
                if (height >= 8 && height <= 13) {
                    for(int xCount = -5; xCount <= 5; xCount++) {
                        for(int yCount = -7; yCount <= 5; yCount++) {
                            for(int zCount = -5; zCount <= 5; zCount++) {
                                Location surround = new Location(world, x + xCount, y2 + height + yCount, z + zCount);
                                Material surroundType = surround.getBlock().getType();

                                if(conf.getBoolean("breakLeaves")) {
                                    if(leaves.contains(surroundType)) {
                                        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
                                            surround.getBlock().breakNaturally();
                                        } else {
                                            surround.getBlock().setType(Material.AIR);
                                        }
                                    }
                                }

                                if(logs.contains(surroundType)) {

                                    if(!player.getGameMode().equals(GameMode.CREATIVE)) {
                                        surround.getBlock().breakNaturally();
                                    } else {
                                        surround.getBlock().setType(Material.AIR);
                                    }

                                    if(conf.getBoolean("playSmokeEffect")) {
                                        player.getWorld().playEffect(surround, Effect.SMOKE, 4);
                                    }

                                    if(conf.getBoolean("damageAxe") && !player.getGameMode().equals(GameMode.CREATIVE)) {
                                        int enchLvl = handItem.getEnchantmentLevel(Enchantment.DURABILITY);
                                        long random = Math.round((Math.random()*enchLvl));

                                        if(random == 0) {
                                            if(handItem.getType().getMaxDurability() > handItem.getDurability()) {
                                                handItem.setDurability((short) (handItem.getDurability() + 1));
                                            } else if(handItem.getDurability() == handItem.getType().getMaxDurability()) {
                                                player.getInventory().setItemInHand(null);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                //SYSTEM FOR BIG JUNGLE TREE REMOVAL
                if(height > 13) {
                    for(int xCount = -5; xCount <= 5; xCount++) {
                        for(int yCount = -(height-5); yCount <= 5; yCount++) {
                            for(int zCount = -5; zCount < 6; zCount++) {
                                Location surround = new Location(world, x + xCount, y2 + height + yCount, z + zCount);
                                Material surroundType = surround.getBlock().getType();

                                if(conf.getBoolean("breakLeaves")) {
                                    if(leaves.contains(surroundType)) {
                                        if(!player.getGameMode().equals(GameMode.CREATIVE)) {
                                            surround.getBlock().breakNaturally();
                                        } else {
                                            surround.getBlock().setType(Material.AIR);
                                        }
                                    }
                                }

                                if(logs.contains(surroundType)) {

                                    if(!player.getGameMode().equals(GameMode.CREATIVE)) {
                                        surround.getBlock().breakNaturally();
                                    } else {
                                        surround.getBlock().setType(Material.AIR);
                                    }

                                    if(conf.getBoolean("playSmokeEffect")) {
                                        player.getWorld().playEffect(surround, Effect.SMOKE, 4);
                                    }

                                    if(conf.getBoolean("damageAxe") && !player.getGameMode().equals(GameMode.CREATIVE)) {
                                        int enchLvl = handItem.getEnchantmentLevel(Enchantment.DURABILITY);
                                        long random = Math.round((Math.random()*enchLvl));

                                        if(random == 0) {
                                            if(handItem.getType().getMaxDurability() > handItem.getDurability()) {
                                                handItem.setDurability((short) (handItem.getDurability() + 1));
                                            } else if(handItem.getDurability() == handItem.getType().getMaxDurability()) {
                                                player.getInventory().setItemInHand(null);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

