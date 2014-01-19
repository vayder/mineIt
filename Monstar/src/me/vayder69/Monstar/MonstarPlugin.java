package me.vayder69.Monstar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.logging.Logger;
import java.nio.file.*;
import me.vayder69.Monstar.listeners.VMPlayerListener;
import me.vayder69.Monstar.stuff.vmLocation;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MonstarPlugin extends JavaPlugin{
	
	public final Logger logger = Logger.getLogger("Minecraft");
	public static MonstarPlugin plugin;
	public final VMPlayerListener pl = new VMPlayerListener(this); 
	private VMCommandExecutor mExecutor;
	private FileConfiguration mConfig = null;
	private File mConfigFile = null;
	private List <String> monstarPlayers = new ArrayList<String>();
	private TreeMap<String, vmLocation> vmWarps = new TreeMap<String, vmLocation>();
	private boolean testMode = false;
	private double monstarWalkSpeed , monstarFlySpeed;
	private Path warpsPath = Paths.get("plugins\\MonstarPlugin\\warps.bin");

	/*_________________________________________________________________________________________________________
	 *
	 * 			Getter/Setter
	 * 
	 * _________________________________________________________________________________________________________
	 */
	
	public boolean getTestMode(){
		return testMode;
	}
	
	public void setTestMode(boolean mode){
		testMode = mode;
	}
	
	public List <String> getMonstarPlayers(){
		return monstarPlayers;
	}
	
	public TreeMap<String, vmLocation> getVMWarps(){
		return vmWarps;
	}
	
	public void addVMWarp(String world, vmLocation vmlocation){
		
	}
	
	public double getMonstarWalkSpeed(){
		return monstarWalkSpeed;
	}
	
	public void setMonstarWalkSpeed(double speed){
		monstarWalkSpeed = speed;
	}
	
	public double getMonstarFlySpeed(){
		return monstarFlySpeed;
	}
	
	public void setMonstarFlySpeed(double speed){
		monstarFlySpeed = speed;
	}
	
	/*_________________________________________________________________________________________________________
	 *
	 *		 	Configs
	 * 
	 * _________________________________________________________________________________________________________
	 */
	
	
	
	
	public void addMonstarPlayers(String playerName){
		monstarPlayers.add(playerName);
		this.getMConfig().set("monstars", monstarPlayers);
		this.saveMConfig();
	}
	
	
	
	
	public void removeMonstarPlayers(String playerName){
		monstarPlayers.remove(playerName);
		this.getMConfig().set("monstars", monstarPlayers);
		this.saveMConfig();
	}
	
	
	
	
	public void reloadMConfig(){
		if(mConfigFile == null){
			mConfigFile = new File(getDataFolder(), "mConfig.yml");
		}
		mConfig = YamlConfiguration.loadConfiguration(mConfigFile);
		
		InputStream defConfigStream = getResource("mConfig.yml");
		
	    if (defConfigStream != null) {
	        YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
	        mConfig.setDefaults(defConfig);
	    }
        this.configureMConfig();
        //if(this.getMConfig().get("monstar") != null){
        	monstarPlayers = this.getMConfig().getStringList("monstars");
        //}
	}
	
	
	
	
	public FileConfiguration getMConfig() {
	    if (mConfig == null) {
	        reloadMConfig();
	    }
	    return mConfig;
	}
	
	
	
	
	public void saveMConfig() {
	    if (mConfig == null || mConfigFile == null) {
	    return;
	    }
	    try {
	        mConfig.save(mConfigFile);
	    } catch (IOException ex) {
	        this.logger.severe( "Could not save config to " + mConfigFile);
	    }
	}
	
	
	
	
	//______________________________________________________________________________________________________________

	
	
	
	public void saveWarp(){
		
    	try{
    		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("plugins\\MonstarPlugin\\warps.bin"));
    		oos.writeObject(vmWarps);
    		oos.flush();
    		oos.close();
    		//Handle I/O exceptions
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
	
	
    @SuppressWarnings("unchecked")
	public TreeMap<String, vmLocation> loadWarp() {
		//check if shit exists
		//if not there's a shitload of errors and u gotta restart
		if(Files.exists(warpsPath)){
	    	try{
	    		ObjectInputStream ois = new ObjectInputStream(new FileInputStream("plugins\\MonstarPlugin\\warps.bin"));
	    		Object result = ois.readObject();
	    		ois.close(); //wenn nichts mehr läuft is der dreck hier schuld!
	    		
	    		//cast auf treemap? ich weiß, dass da ne treemap drin is und mehr nich...
	    		return (TreeMap<String, vmLocation>) result;
	    	}catch(Exception e){
	    		e.printStackTrace();	
	    	}
		}
		else{
			//make da shiet!!
			createWarpList();
		}
    	return null;
    }
	
	private void createWarpList(){
		vmWarps = new TreeMap<String, vmLocation>();
		try{
    		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("plugins\\MonstarPlugin\\warps.bin"));
    		oos.writeObject(vmWarps);
    		oos.flush();
    		oos.close();
    		//Handle I/O exceptions
    	}catch(Exception e){
    		e.printStackTrace();
    	}
	}


	
/*_________________________________________________________________________________________________________________________________________________
 * 
 * ___________________Enable / Disable
 * 
 *_________________________________________________________________________________________________________________________________________________
 */
	
	
	
	
	
	
	@Override
	public void onDisable(){
		PluginDescriptionFile descFile = this.getDescription();
		
		this.logger.info(descFile.getName() + " has been disabled.");
	}
	
	@Override
	public void onEnable(){
		PluginDescriptionFile descFile = this.getDescription();
		PluginManager pm = getServer().getPluginManager();
		mExecutor = new VMCommandExecutor(this);
		
		/* ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ________________COMMAND GETTERS_______________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 */
		
		
		getCommand("vmconvert").setExecutor(mExecutor);
		getCommand("vmreload").setExecutor(mExecutor);
		getCommand("vm").setExecutor(mExecutor);
		getCommand("vmcure").setExecutor(mExecutor);
		getCommand("vmtestmode").setExecutor(mExecutor);
		getCommand("vml").setExecutor(mExecutor);
		getCommand("gmode").setExecutor(mExecutor);
		getCommand("position").setExecutor(mExecutor);
		getCommand("heal").setExecutor(mExecutor);
		getCommand("h").setExecutor(mExecutor);
		getCommand("slap").setExecutor(mExecutor);
		getCommand("vmw").setExecutor(mExecutor);
		getCommand("vmwadd").setExecutor(mExecutor);
		getCommand("vmwdel").setExecutor(mExecutor);
		getCommand("vmwl").setExecutor(mExecutor);
		getCommand("blubb").setExecutor(mExecutor);


		
		
		
		/* ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 * ______________________________________________________________________________________________________________________________________
		 */
		
		
		pm.registerEvents(this.pl, this);
		
		
		
		//__________Config stuff____________________________________________________________________________________________________________
		
		
		//maybe i need that shit later on...? now it's just bugs'n shiiet....
		
		//this.getConfig().options().copyDefaults(true);
		
		
		//get mConfig ready for TEH SHIIIET!!
		
		this.configureMConfig();
		
		
		//initialize vmWarps, because it sucks...
		//by now it sucks less by the way, still not good....
		//edit: made it look (and work) nice
		
		vmWarps = this.loadWarp();
		//--> kA warum hier ein this. vorne dran steht.... da wollte wohl wer mehr tippen als nötig
		this.saveWarp();
		// japp, hier das gleiche
		
		
		//LOAD & SAVE ALL THE THINGS!!!!! also initialize some stuff kk
		
		//initialize the weapons monstars can't use for bonus dmg
		pl.loadWeapons();
		saveMConfig();
		reloadMConfig();
        saveConfig();
        reloadConfig();
        saveWarp();        
        //___________________________________________________________________________________________________________________________________
        
		this.logger.info(descFile.getName() + " Ver. " + descFile.getVersion() + " has been enabled.");
	}
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		return false;
	}
	
	
	/* ______________________________________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 * _____________Helpful Methods__________________________________________________________________________________________________________
	 * ______________________________________________________________________________________________________________________________________
	 */
	
	private void configureMConfig(){
		//load monstar list
				if(this.getMConfig().getList("monstars") == null){
					monstarPlayers.add("deleteMePls");
					//this.getMConfig().set("monstars", Arrays.asList(monstarPlayers));
					this.getMConfig().set("monstars", monstarPlayers);
				}
				else{
					
					monstarPlayers = this.getMConfig().getStringList("monstars");
				}
				
				//load monstarWalkSpeed
				if(this.getMConfig().getDouble("walkspeed") == 0){
					monstarWalkSpeed = 0.25f;
					
					this.getMConfig().set("walkspeed", monstarWalkSpeed);
				}
				else{
					
					monstarWalkSpeed = this.getMConfig().getDouble("walkspeed");
				}
				
				//load monstarFlySpeed
				if(this.getMConfig().getDouble("flyspeed") == 0){
					monstarFlySpeed = 0.15;
					
					this.getMConfig().set("flyspeed", 0.15);
				}
				else{
					
					monstarFlySpeed = this.getMConfig().getDouble("flyspeed");
				}
	}
	

}
