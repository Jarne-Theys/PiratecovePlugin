package tk.piratecove;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;

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

    @EventHandler
    public void onBlockBreak (BlockBreakEvent event) {
        if(logs.contains(event.getBlock().getType()) && event.getPlayer().getInventory().getItemInMainHand().getType().name().toLowerCase().contains("axe") && !event.getPlayer().getInventory().getItemInMainHand().getType().name().toLowerCase().contains("pickaxe")) {

            Player player = event.getPlayer();
            ItemStack handItem = player.getInventory().getItemInMainHand();
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
                    breakBlockAndRemoveDurability(player, handItem, blockAbove);

                    logsLeft = true;
                } else if(leaves.contains(blockAboveType)) {
                    logsLeft = false;
                    isTree = true;
                } else {
                    logsLeft = false;
                    isTree = false;
                }
            }

            //If the broken stem is a tree, the following will be executed
            if (isTree) {
                event.setCancelled(true);

                //SYSTEM FOR SMALL TREE REMOVAL
                for(int xCount = -2; xCount <= 2; xCount++) {
                    for(int yCount = 0; yCount <= height + 2; yCount++) {
                        for(int zCount = -2; zCount <= 2; zCount++) {

                            Location surround = new Location(world, x + xCount, y2 + yCount, z + zCount);
                            Material surroundType = surround.getBlock().getType();
                            bigTreeRemoval(player, handItem, surround, surroundType);
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
                                bigTreeRemoval(player, handItem,surround, surroundType);
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
                                bigTreeRemoval(player, handItem, surround, surroundType);
                            }
                        }
                    }
                }
            }
        }
    }

    private void bigTreeRemoval(Player player, ItemStack handItem, Location surround, Material surroundType) {
        if(logs.contains(surroundType)) {
            breakBlockAndRemoveDurability(player, handItem, surround);
        }
    }

    private void breakBlockAndRemoveDurability(Player player, ItemStack handItem, Location surround) {
        if(player.getGameMode().equals(GameMode.SURVIVAL)) {
            surround.getBlock().breakNaturally();
            int enchLvl = handItem.getEnchantmentLevel(Enchantment.DURABILITY);
            long random = Math.round((Math.random()*enchLvl));
            if(random == 0) {
                Damageable damageableHandItem = (Damageable) handItem.getItemMeta();
                int currentDamage = damageableHandItem.getDamage();
                damageableHandItem.setDamage(currentDamage + 1);
            }
        }
    }
}

