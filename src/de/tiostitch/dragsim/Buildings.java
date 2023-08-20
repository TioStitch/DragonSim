package de.tiostitch.dragsim;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Buildings {

	@SuppressWarnings("deprecation")
	public static ArrayList<Location> placeAltar(Location loc) {
		ArrayList<Location> portals = new ArrayList<Location>();
		
		
		portals.add(loc.getWorld().getBlockAt((int) loc.getX() + 2, (int) loc.getY(), (int) loc.getZ() + 1).getLocation());
		portals.add(loc.getWorld().getBlockAt((int) loc.getX() + 2, (int) loc.getY(), (int) loc.getZ() - 1).getLocation());
		portals.add(loc.getWorld().getBlockAt((int) loc.getX() - 2, (int) loc.getY(), (int) loc.getZ() + 1).getLocation());
		portals.add(loc.getWorld().getBlockAt((int) loc.getX() - 2, (int) loc.getY(), (int) loc.getZ() - 1).getLocation());
		portals.add(loc.getWorld().getBlockAt((int) loc.getX() + 1, (int) loc.getY(), (int) loc.getZ() + 2).getLocation());
		portals.add(loc.getWorld().getBlockAt((int) loc.getX() + 1, (int) loc.getY(), (int) loc.getZ() - 2).getLocation());
		portals.add(loc.getWorld().getBlockAt((int) loc.getX() - 1, (int) loc.getY(), (int) loc.getZ() + 2).getLocation());
		portals.add(loc.getWorld().getBlockAt((int) loc.getX() - 1, (int) loc.getY(), (int) loc.getZ() - 2).getLocation());
	
		Location t = loc.clone().add(1,0,1);
		if (DragonSimulator.getInstance().getConfig().getBoolean("settings.altar-glass-enabled")) {

			for (int i = 1; i < 30; i++) {
				Block b = t.getWorld().getBlockAt(t.add(0, 1, 0));
				b.setType(Material.STAINED_GLASS);
				b.setData((byte) DragonSimulator.getInstance().getConfig().getInt("settings.altar-glass-color"));
			}
		}
		return portals;
	}

	@SuppressWarnings("deprecation")
	public static void setEgg(HashMap<Location, HashMap<Material, Byte>> egg2) {
		if(egg2.isEmpty()) return;
		for(Location loc : egg2.keySet()) {
			for(Material mat : egg2.get(loc).keySet()) {
				loc.getBlock().setType(mat);
				loc.getBlock().setData(egg2.get(loc).get(mat));
			}
		}
	}
	
}	

