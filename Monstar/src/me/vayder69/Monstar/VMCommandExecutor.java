package me.vayder69.Monstar;


import java.util.ArrayList;
import me.vayder69.Monstar.stuff.vmLocation;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VMCommandExecutor implements CommandExecutor{
	
	private MonstarPlugin plugin;
	
	public VMCommandExecutor(MonstarPlugin plugin){
		this.plugin = plugin;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		
		
		
		
		
		if(commandLabel.equalsIgnoreCase("vmconvert")){
			
			boolean canAdd = true;
			
			if(args.length != 1){
				if(sender instanceof Player){
					
					Player player = (Player) sender;
					
					player.sendMessage(ChatColor.RED + "Usage: /vmconvert <playername>");
				}
				else{
					plugin.logger.info("Usage: vmconvert <playername>");
				}
			}else if(args.length == 1){
				
				//search in yaml
				for(String s : plugin.getMonstarPlayers()){
					
					if(args[0].equalsIgnoreCase(s)){
						canAdd = false;
					}
				}
				
				
				//target can be added
				if(canAdd){
					
					plugin.addMonstarPlayers(args[0].toLowerCase());
					
					if(sender.getServer().getPlayer(args[0])!= null){
						Player target = sender.getServer().getPlayer(args[0]);
						target.sendMessage(ChatColor.DARK_RED + "You became a MONSTAR!");
					}
					
					
					
					if(sender instanceof Player){
						Player player = (Player) sender;

						player.sendMessage(args[0] + ChatColor.GREEN + " became a monstar.");
					}
					else{
						plugin.logger.info(args[0] + " became a monstar.");
					}
				}
				
				//target can't be added
				else{
					sender.sendMessage(ChatColor.RED + " -" + ChatColor.WHITE + args[0] + ChatColor.RED + " already is a monstar!-");
				}
			}
			
			
			//standard true in case you wanna check if the command was enforced (right word?)
			return true;
		}
		
		/* ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 */
		
		
		if(commandLabel.equalsIgnoreCase("vmcure")){
			if(args.length != 1){
				sender.sendMessage(ChatColor.RED + "Usage: /vmcure <playername>");
			}
			else if(plugin.getMonstarPlayers().contains(args[0].toLowerCase())){
				plugin.removeMonstarPlayers(args[0]);
				sender.sendMessage(args[0] + ChatColor.GREEN + " is human again.");
				plugin.saveMConfig();
				plugin.reloadMConfig();
				if(sender.getServer().getPlayer(args[0])!= null){
					Player target = sender.getServer().getPlayer(args[0]);
					target.sendMessage(ChatColor.GOLD + "You are human again!");
				}
			}
			else{
				sender.sendMessage(args[0] + ChatColor.RED + " is no monstar!");
			}

			return true;
		}
		
		/* ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 */
		
		if(commandLabel.equalsIgnoreCase("vmreload")){
			plugin.reloadConfig();
			plugin.reloadMConfig();
			if(sender instanceof Player){
				Player player = (Player) sender;
				player.sendMessage(ChatColor.GREEN + "Configs have been reloaded.");
			}
			else{
				plugin.logger.info("Configs have been reloaded.");
			}
			
			return true;
		}
		
		/* ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 */
		
		if(commandLabel.equalsIgnoreCase("vm")){
			sender.sendMessage(ChatColor.RED + "-------------------VM HELP-------------------");
			sender.sendMessage(ChatColor.RED + "vmconvert: " + ChatColor.GREEN + "makes target player a monstar");
			sender.sendMessage(ChatColor.RED + "vmcure: " + ChatColor.GREEN + "makes target monstar human");
			sender.sendMessage(ChatColor.RED + "vml: " + ChatColor.GREEN + "shows who is currently a monstar");
			sender.sendMessage(ChatColor.RED + "vmreload: " + ChatColor.GREEN +"reloads the configs");
			sender.sendMessage(ChatColor.RED + "vmwl: " + ChatColor.GREEN + "lists all warps in current world");
			sender.sendMessage(ChatColor.RED + "vmw: " + ChatColor.GREEN + "teleports you to a warp of your choice");
			sender.sendMessage(ChatColor.RED + "vmwadd: " + ChatColor.GREEN + "current position as warp to this world");
			sender.sendMessage(ChatColor.RED + "vmwdel: " + ChatColor.GREEN + "deletes a warp of your choice");
			return true;
		}
		
		/* ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 */
		
		if(commandLabel.equalsIgnoreCase("vmtestmode")){
			
			if(args.length != 1){
				sender.sendMessage(ChatColor.RED + "Usage: vmtestmode on/off");
				
				return true;
			}
			else{
				if(sender instanceof Player){
					Player player = (Player) sender;
					
					if(player.isOp()){
						if(args[0].equalsIgnoreCase("on")){
							plugin.setTestMode(true);
							sender.sendMessage(ChatColor.GOLD + "--TestMode"  + ChatColor.GREEN + " ON--");

						}else if(args[0].equalsIgnoreCase("off")){
							plugin.setTestMode(false);
							sender.sendMessage(ChatColor.GOLD + "--TestMode" + ChatColor.DARK_RED + " OFF--");

						}else{
							sender.sendMessage(ChatColor.RED + "Usage: vmtestmode on/off");
						}
					}
					
				}else{
					if(args[0].equalsIgnoreCase("on")){
						plugin.setTestMode(true);
						sender.sendMessage(ChatColor.GOLD + "--TestMode"  + ChatColor.GREEN + " ON--");

					}else if(args[0].equalsIgnoreCase("off")){
						plugin.setTestMode(false);
						sender.sendMessage(ChatColor.GOLD + "--TestMode" + ChatColor.DARK_RED + " OFF--");

					}else{
						sender.sendMessage(ChatColor.RED + "Usage: vmtestmode on/off");
					}
				}
			}
			
			return true;
		}
		
		/* ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 */
		
		if(commandLabel.equalsIgnoreCase("vml")){
			for(String s : plugin.getMonstarPlayers()){
				if(sender.getServer().getPlayer(s) != null){
					sender.sendMessage(s);
				}
			}
		}
		
		/* ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ___________________________________________CONVENIENCE COMMANDS_______________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 */
		
		if(commandLabel.equalsIgnoreCase("gmode")){
			
			if(sender instanceof Player){
				Player player = (Player) sender;
				
				if(player.isOp()){
					String mode = player.getGameMode().toString();
					
					if(mode.equalsIgnoreCase("creative")){
						player.setGameMode(GameMode.SURVIVAL);
						player.sendMessage(ChatColor.GOLD + "GameMode set to SURVIVAL.");
					}
					else{
						player.setGameMode(GameMode.CREATIVE);
						player.sendMessage(ChatColor.GOLD + "GameMode set to CREATIVE.");
					}
				}
				else{
					player.sendMessage(ChatColor.RED + "You have to be an OP!");
				}
			}
			else{
				sender.sendMessage("Only usable as a Player.");
			}
		}
		
		
		
		
/*_____________________________________________________________________________________________________________________________________________________________*/
		
		
		if(commandLabel.equalsIgnoreCase("heal") || commandLabel.equalsIgnoreCase("h")){
			
			if(sender instanceof Player){
				Player player = (Player) sender;
				if(player.isOp()){
					if(args.length == 0){
						player.setFireTicks(0);
						player.setHealth(20);
						player.setFoodLevel(20);
						player.setSaturation(20f);
						player.sendMessage(ChatColor.GREEN + " -Healed-");
					}
					else if(args.length == 1){
						if(sender.getServer().getPlayer(args[0]) != null){
							Player target = sender.getServer().getPlayer(args[0]);

							if(sender.getServer().getPlayer(args[0]).isOnline()){
								target.setFireTicks(0);
								target.setHealth(20);
								target.setFoodLevel(20);
								target.setSaturation(20f);
								target.sendMessage(ChatColor.GREEN + " -Healed-");
								player.sendMessage(ChatColor.GREEN + " -" + ChatColor.WHITE + target.getName() + ChatColor.GREEN + " got healed-");
							}
							else{
								sender.sendMessage(ChatColor.RED + " -" + ChatColor.WHITE + target.getName() + ChatColor.RED + " is not online!");
							}
						}
						else{
							player.sendMessage(ChatColor.RED + " -" + ChatColor.WHITE + args[0] + ChatColor.RED + " is no player / not online!-");
						}

					}
					else sender.sendMessage(ChatColor.RED + "Usage: /h   or   /h <target>");
				}
			}
			else{
				if(args.length == 1){

					if(sender.getServer().getPlayer(args[0]) != null){
						Player target = sender.getServer().getPlayer(args[0]);
						if(sender.getServer().getPlayer(args[0]).isOnline()){
							target.setFireTicks(0);
							target.setHealth(20);
							target.setFoodLevel(20);
							target.setSaturation(20f);
							sender.sendMessage(" -" + target.getName() + " got healed-");
						}
						else{
							sender.sendMessage(" -" + target.getName() + "is not online!");
						}
					}
					else{
						sender.sendMessage(" -" + args[0] + " is no player / not online!-");
					}
				}
				else sender.sendMessage("Usage: h <target>");
			}
		}
		
		

		
/*_____________________________________________________________________________________________________________________________________________________________*/

		
		
		
		if(commandLabel.equalsIgnoreCase("slap")){
			if(sender instanceof Player){
				Player player = (Player) sender;
				
				if(player.isOp()){
					if(args.length == 1){
						if(sender.getServer().getPlayer(args[0]) != null){
							Player target = sender.getServer().getPlayer(args[0]);
							target.damage(4);
							target.sendMessage(ChatColor.RED + " -SLAPPED-");
							sender.sendMessage(ChatColor.RED + " -" + ChatColor.WHITE + target.getName() + ChatColor.RED + " got SLAPPED-");
						}
						else{
							sender.sendMessage(ChatColor.RED + " - " + ChatColor.WHITE + args[0] + ChatColor.RED + " is no player / not online! -");
						}
					}
					else{
						sender.sendMessage(ChatColor.RED + "Usage: /slap <target>");
					}
				}
			}
			else{
				if(args.length == 1){
					if(sender.getServer().getPlayer(args[0]) != null){
						Player target = sender.getServer().getPlayer(args[0]);
						target.damage(4);
						target.sendMessage(ChatColor.RED + " -SLAPPED-");
						sender.sendMessage(" -" + target.getName() + " got SLAPPED-");
					}
					else{
						sender.sendMessage(ChatColor.RED + " - " + ChatColor.WHITE + args[0] + ChatColor.RED + " is no player / not online! -");
					}
				}
				else{
					sender.sendMessage(ChatColor.RED + "Usage: /slap <target>");
				}
			}
		}
		
		
		
		/*_____________________________________________________________________________________________________________________________________________________________*/

		
		if(commandLabel.equalsIgnoreCase("blubb")){
			
			if(sender instanceof Player){
			Player player = (Player) sender;
			
		
			player.setWalkSpeed(0.2f);
			player.setFlySpeed(0.1f);
			}
			
		}
		
		
		
		
		
		/* ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ________________________________WARP STUFF____________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 */
		

		if(commandLabel.equalsIgnoreCase("vmw")){
			if(sender instanceof Player){
				
				Player player = (Player) sender;
				
				if(args.length == 1){
					if(plugin.getVMWarps().isEmpty()){
				
						player.sendMessage(ChatColor.RED + " -Your warps.bin is completely empty-");
					}
					if(plugin.getVMWarps().containsKey(args[0])){
						
						vmLocation loc = plugin.getVMWarps().get(args[0]);
						Location location = new Location(player.getServer().getWorld(loc.getWorldName()), loc.getX(), loc.getY(), loc.getZ());
						player.teleport(location);
						player.sendMessage(ChatColor.GREEN + " -teleported to: " + ChatColor.WHITE + args[0] + ChatColor.GREEN + "-");
					}
					else{
						
						player.sendMessage(ChatColor.RED + " -There is no warp called " + ChatColor.WHITE + args[0] + ChatColor.RED + "-");
					}
				}
				else{
					
					player.sendMessage(ChatColor.RED + "Usage: /vmw <WarpName>");
				}
			}
			else{
				
				sender.sendMessage("---Player command only---");
			}
		}
		
		
		
		//_______________________________________________________________________________________________________________________________________
		
		
		if(commandLabel.equalsIgnoreCase("vmwadd")){
			if(sender instanceof Player){
				if(args.length == 1){
					
					Player player = (Player) sender;
					String warpN = args[0];
					vmLocation loc = new vmLocation(warpN, player.getWorld().getName(), player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ());
					
					if(!plugin.getVMWarps().containsKey(warpN)){
						
						plugin.getVMWarps().put(warpN, loc);
						plugin.saveWarp();
						plugin.loadWarp();
						sender.sendMessage(ChatColor.GOLD + " -Warp " + ChatColor.WHITE + warpN + ChatColor.GOLD + " successfully CREATED-");
					}
					else{
						
						sender.sendMessage(ChatColor.RED + " -Warp with this name already exists-");
					}
					
				}
				else{
					
					sender.sendMessage(ChatColor.RED + " Usage: /vmwadd <WarpName>");
				}
			}
			else{
				
				sender.sendMessage("---Player command only---");
			}
		}
		
		
		
		
		//_______________________________________________________________________________________________________________________________________

		
		if(commandLabel.equalsIgnoreCase("vmwdel")){
			if(args.length == 1){
				
				String warpN = args[0];
				if(plugin.getVMWarps().containsKey(warpN)){
					plugin.getVMWarps().remove(warpN);
					plugin.saveWarp();
					plugin.loadWarp();
					sender.sendMessage(ChatColor.GOLD + " -Warp " + ChatColor.WHITE + warpN + ChatColor.GOLD + " successfully DELETED-");
				}
				else{
					sender.sendMessage(ChatColor.RED + " -Warp doesn't exist-");
				}

			}
			else{
				
				sender.sendMessage(ChatColor.RED + "Usage: /vmwdel <WarpName>");
			}
		}
		
		
		
		
		//_______________________________________________________________________________________________________________________________________

		
		
		if(commandLabel.equalsIgnoreCase("vmwl")){
			
			ArrayList<String> warpList = new ArrayList<String>();
			
			if(sender instanceof Player){
				Player player = (Player) sender;
				
				String worldName = player.getWorld().getName();
				
				for(String key : plugin.getVMWarps().keySet()){
					if(plugin.getVMWarps().get(key).getWorldName().equalsIgnoreCase(worldName)){
						warpList.add(key);
					}
				}
				player.sendMessage(ChatColor.GOLD + "        List of Warps :");
				player.sendMessage(warpList.toString());
				
			}
			else{
			
				for(String key : plugin.getVMWarps().keySet()){
					warpList.add(key);
				}
				sender.sendMessage(ChatColor.GOLD + "        List of Warps :");
				sender.sendMessage(warpList.toString());
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
		
		
		if(commandLabel.equalsIgnoreCase("position")){
			if(sender instanceof Player){
				Player player = (Player) sender;
				player.sendMessage(player.getLocation().getBlock().toString());
			}
			else{
				sender.sendMessage("---Player command only---");
			}
		}
		
		
		return false;
		
	}
	
}
