package me.vayder69.Monstar.listeners;


import java.util.ArrayList;
import java.util.List;

import me.vayder69.Monstar.MonstarPlugin;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.*;
import org.bukkit.util.Vector;

public class VMPlayerListener implements Listener{
	
	public static MonstarPlugin plugin;
	private boolean isMonstar = false;
	private boolean isMonstarTime = false;
	private boolean speedIsSet = false;
	private ArrayList<String> weapons = new ArrayList<String>();
	
	
	
	@SuppressWarnings("static-access")
	public VMPlayerListener(MonstarPlugin plugin){
		this.plugin = plugin;
	}
	
	//when wearing one of these weapons there is no bonus damage as a monstar
	public void loadWeapons(){
		weapons.add("stone_sword");
		weapons.add("stone_axe");
		weapons.add("stone_pickaxe");
		weapons.add("stone_shovel");
		weapons.add("iron_sword");
		weapons.add("iron_axe");
		weapons.add("iron_pickaxe");
		weapons.add("iron_shovel");
		weapons.add("gold_sword");
		weapons.add("gold_axe");
		weapons.add("gold_pickaxe");
		weapons.add("gold_shovel");
		weapons.add("diamond_sword");
		weapons.add("diamond_axe");
		weapons.add("diamond_pickaxe");
		weapons.add("diamond_shovel");
	}
	
	public ArrayList<String> getWeapons(){
		return weapons;
	}
	
	/* ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 */
	
	
	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent event){
		
		Player player = event.getPlayer();
		Action action = event.getAction();
		isMonstar = false;
		
		
		for(String s : plugin.getMConfig().getStringList("monstars")){
			if(player.getName().equalsIgnoreCase(s)){
				isMonstar = true;
			}
		}
	
	
		
		if(isMonstar){
			if(event.getMaterial().toString().equalsIgnoreCase("FEATHER")){
				if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
					
					Vector vadd, vorig;
					vorig = player.getLocation().getDirection();
					vadd = new Vector(vorig.getX()/1.3, 0.5, vorig.getZ()/1.3);
					vadd.normalize();
					vadd.multiply(2.5D);
					player.setVelocity(player.getVelocity().add(vadd));
				}
			}
		}
		
		
		if(plugin.getTestMode()){
			if(event.getPlayer().isOp()){
				if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)){
					
					String materialName = event.getMaterial().toString();
					String itemName = event.getPlayer().getItemInHand().getType().toString();
					event.getPlayer().sendMessage(ChatColor.BLUE + "Item used: " + ChatColor.WHITE + materialName);
					event.getPlayer().sendMessage(ChatColor.BLUE + "Item name: " + ChatColor.WHITE + itemName);
				}
			}
		}
	}
	
	
	
	/* ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 */
	
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event){
		Player player = event.getPlayer();
		long worldTime = player.getWorld().getTime();
		
		
		//check if it is monstar time
		if((getIsMonstar(player)) && (worldTime > 13600) && (worldTime < 22600) && (!isMonstarTime)){
			setIsMonstarTime(true);
		}
		else if((getIsMonstar(player)) && ((worldTime < 13600) || (worldTime > 22600)) && (isMonstarTime)){
			setIsMonstarTime(false);
		}
		
		
		
		if(getIsMonstar(player) && isMonstarTime && !speedIsSet){
			
			//player.setWalkSpeed(0.25f);
			
			List<Player> pList = player.getWorld().getPlayers();
			
			setSpeedIsSet(true);
			for(Player p : pList){
				if(p.isOnline()){
					if(getIsMonstar(p)){
						p.setWalkSpeed((float) plugin.getMonstarWalkSpeed());
						p.setFlySpeed((float) plugin.getMonstarFlySpeed());
					}
				}
			}
		} else if(getIsMonstar(player) && !isMonstarTime && speedIsSet){
			
			//player.setWalkSpeed(0.2f);
			
			List<Player> pList = player.getWorld().getPlayers();
			
			setSpeedIsSet(false);
			for(Player p : pList){
				if(p.isOnline()){
					if(getIsMonstar(p)){
						p.setWalkSpeed(0.2f);
						p.setFlySpeed(0.1f);
					}
				}
			}
		}
		if(plugin.getTestMode()){
			
			//player.sendMessage("" + worldTime);
			//player.sendMessage("" + player.getWalkSpeed());
			//player.sendMessage( "" + player.getFlySpeed());
			//player.sendMessage("" + player.getSaturation());
			
		}
		
		
	}
	
	
	
	
	/* ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 */
	
	
	
	@EventHandler
	public void onPlayerJoinEvent(PlayerJoinEvent event){
		
		Player player = event.getPlayer();
		if(getIsMonstar(player)){
			//do fun stuff!!
		}
	}
	
	

	
	
	
	/* ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 */
	
	
	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){
		
		Entity victim = event.getEntity();
		
		if(event.isCancelled()){
			return;
		}
			
		
		//--------------DMG BONUS STUFF----------------------------------------------------------------
		
		
		if (event instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent edbeEvent = (EntityDamageByEntityEvent) event;
			Entity damager = edbeEvent.getDamager();
			
			//Entity victim = event.getEntity();
			double damage = event.getDamage();
			

			if(damager instanceof Player){
				Player pDamager = (Player) damager;
				
				if(getIsMonstar(pDamager) && isMonstarTime){
					if(pDamager.getGameMode().toString().equalsIgnoreCase("creative")){
						damage = damage + 500;
						event.setDamage((int) Math.round(damage));

					}
					else{
						String item = pDamager.getItemInHand().getType().toString();
						
						//boolean for check
						boolean isWeapon = false;
						
						for(String weapon : weapons){
							if(item.equalsIgnoreCase(weapon)){
								isWeapon = true;
								
							}
						}
						if(!isWeapon){
							damage = damage + 4;

						}
					}
				}
				event.setDamage((int) Math.round(damage));
				
				if(plugin.getTestMode()){
					pDamager.sendMessage("Damage: " + event.getDamage());
				}
			}
			
		}
		
		
		//---------------------LESS DMG FOR MONSTAR----------------------------------------------

		
		if(victim instanceof Player){
			Player pVictim = (Player) victim;
			
			if(getIsMonstar(pVictim)){
				
				int damage = event.getDamage();
				DamageCause damageCause = event.getCause();
			
				//fire immunity and shit
				if(damageCause.equals(DamageCause.FIRE) || damageCause.equals(DamageCause.FIRE_TICK) || 
					damageCause.equals(DamageCause.LAVA)){
				
					pVictim.setFireTicks(0);
					event.setCancelled(true);
				}
				else{
					//receive less dmg, cause you're awesome, you're a monstar
					damage = (int) (damage * 0.75);
				}
			
				event.setDamage((int) Math.round(damage));
				pVictim.sendMessage(ChatColor.RED + "" + damage);
			}
		}
		
		
		
	}
	
	
	
	
	
	/* ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 */
	
	
	
	public boolean getIsMonstar(Player player){
		
		Player thisPlayer = player;
		
		isMonstar = false;
		
		for(String s : plugin.getMConfig().getStringList("monstars")){
			if(thisPlayer.getName().equalsIgnoreCase(s)){
				return true;
			}
		}
		return false;
	}
	
	
	public boolean geItMonstarTime(){
		return isMonstarTime;
	}
	
	public void setIsMonstarTime(boolean bool){
		isMonstarTime = bool;
	}
	
	public boolean getSpeedIsSet(){
		return speedIsSet;
	}
	
	public void setSpeedIsSet(boolean bool){
		speedIsSet = bool;
	}
}