package me.McSebi.TimberMP;

import java.util.logging.Logger;

import me.McSebi.TimberMP.TimberMP;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
//import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.plugin.Plugin;

public class PluginBlockListener implements Listener {
	
	public final Logger logger = Logger.getLogger("Minecraft");
	
	public static TimberMP plugin;
	public PluginBlockListener(TimberMP instance) {
		plugin = instance;
	}
	

	private WorldGuardPlugin getWorldGuard() {
	    Plugin WG = plugin.getServer().getPluginManager().getPlugin("WorldGuard");
	 
	    if (WG == null || !(WG instanceof WorldGuardPlugin)) {
	        return null;
	    }
	 
	    return (WorldGuardPlugin) WG;
	}

	
	@EventHandler
	public void onBlockBreak (BlockBreakEvent event) {
		//Define wether permissions are enabled or not
		boolean perm = true;
		if(plugin.getConfig().getBoolean("permissions")) {
			if(event.getPlayer().hasPermission("timbermp.use")) {
				perm = true;
			} else if (!event.getPlayer().hasPermission("timbermp.use")) {
				perm = false;
				if(plugin.getConfig().getBoolean("permissionsMessage")) {
					event.getPlayer().sendMessage(ChatColor.RED + "TimberMP: You don't have the permission to mine this Tree!");
				}
			}
		} else {
			perm = true;
		}
		
		if(event.getBlock().getType().equals(Material.LOG) && event.getPlayer().getItemInHand().getType().name().toLowerCase().contains("axe") && !event.getPlayer().getItemInHand().getType().name().toLowerCase().contains("pickaxe") && !event.getPlayer().isSneaking() && perm && getWorldGuard().canBuild(event.getPlayer(), event.getBlock().getLocation())) {
			
			Player player = event.getPlayer();
			ItemStack handItem = player.getItemInHand();
			FileConfiguration conf = plugin.getConfig();
			World world = player.getWorld();
			Location blockLocation = event.getBlock().getLocation();
			
				double x = blockLocation.getBlockX();
				double y = blockLocation.getBlockY();
				double y2 = blockLocation.getBlockY();
				double z = blockLocation.getBlockZ();
			
			byte treeType = new Location(world, x, y+1, z).getBlock().getData();
			
			int height = 0;
			
			boolean ground = false;
			
			while(!ground) {
				y--;
				y2--;
				Location blockBelow = new Location(world, x, y-1, z);
				Material blockBelowType = blockBelow.getBlock().getType();
				
				if(!blockBelowType.equals(Material.LOG)) {
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
				
				if(blockAboveType.equals(Material.LOG)) {
					if(!player.getGameMode().equals(GameMode.CREATIVE)) {
						blockAbove.getBlock().breakNaturally();
					} else {
						blockAbove.getBlock().setType(Material.AIR);
					}
					
					if(conf.getBoolean("playSmokeEffect")) {
						player.getWorld().playEffect(blockAbove, Effect.SMOKE, 4);
					}
					
					if(conf.getBoolean("damageAxe") && !player.getGameMode().equals(GameMode.CREATIVE)) {
						int enchLvl = handItem.getEnchantmentLevel(Enchantment.DURABILITY);
						long random = Math.round((Math.random()*enchLvl));
						
						if(random == 0) {
							if(handItem.getType().getMaxDurability() > handItem.getDurability()) {
								handItem.setDurability((short) (handItem.getDurability() + 1));
							}
						}
					}
					
					logsLeft = true;
				} else if(blockAboveType.equals(Material.LEAVES)) {
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
							
							if(conf.getBoolean("breakLeaves")) {
								if(surroundType == Material.LEAVES) {
									if(!player.getGameMode().equals(GameMode.CREATIVE)) {
										surround.getBlock().breakNaturally();
									} else {
										surround.getBlock().setType(Material.AIR);
									}
								}
							}
							
							if(surroundType == Material.LOG) {
								
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
								
								if(conf.getBoolean("auto-replant") && (new Location(world, surround.getBlockX(), surround.getBlockY()-1, surround.getBlockZ()).getBlock().getType().equals(Material.GRASS) || new Location(world, surround.getBlockX(), surround.getBlockY()-1, surround.getBlockZ()).getBlock().getType().equals(Material.DIRT))) {
									surround.getBlock().setType(Material.SAPLING);
									surround.getBlock().setData(treeType);
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
									if(surroundType == Material.LEAVES) {
										if(!player.getGameMode().equals(GameMode.CREATIVE)) {
											surround.getBlock().breakNaturally();
										} else {
											surround.getBlock().setType(Material.AIR);
										}
									}
								}
								
								if(surroundType == Material.LOG) {
									
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
									if(surroundType == Material.LEAVES) {
										if(!player.getGameMode().equals(GameMode.CREATIVE)) {
											surround.getBlock().breakNaturally();
										} else {
											surround.getBlock().setType(Material.AIR);
										}
									}
								}
								
								if(surroundType == Material.LOG) {
									
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
