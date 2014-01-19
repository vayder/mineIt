package me.vayder69.Monstar.stuff;

import java.util.TreeMap;

@SuppressWarnings("serial")
public class vmLocation extends TreeMap<String, Object>{
	
	
	private String worldName = "", warpName;
	private double x = 0, y = 0, z = 0;
	
	
	
	
	
	
	public vmLocation(){
		worldName = "";
		x = 0;
		y = 0;
		z = 0;
	}
	
	public vmLocation(String warp, String world, double x1, double y1, double z1){
		warpName = warp;
		worldName = world;
		x = x1;
		y = y1;
		z = z1;
		this.put("Warpname", warpName);
		this.put("Worldname", worldName);
		this.put("X", x);
		this.put("Y", y);
		this.put("Z", z);
	}
	
	
	
	
	
	public String getWorldName(){
		return worldName;
	}
	
	public double getX(){
		return x;
	}
	
	public double getY(){
		return y;
	}
	
	public double getZ(){
		return z;
	}
}
