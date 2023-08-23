package de.tiostitch.dragsim;

import java.io.File;
import java.io.IOException;

import de.tiostitch.dragsim.armors.LightningRunnable;
import de.tiostitch.dragsim.craftinginjector.RecipeInjector;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class DragonSimulator extends JavaPlugin{

	private static DragonSimulator plugin;
	private static File config;
	private static FileConfiguration cfg;
	private static EntityHider entityHider;
	private static File itemFile;
	private static FileConfiguration itemConfig;
	private static File altarFile;
	private static FileConfiguration altarConfig;
	
	public void onEnable() {
		plugin = this;


		//NMSDragonControllPhase.setDragonControllPhase(new Location(Bukkit.getWorld("world"), 10, 10, 10));

		entityHider = new EntityHider(plugin, EntityHider.Policy.BLACKLIST);

		config = new File("plugins/DragonSim/config.yml");
		if(!config.exists()) {
			/*try {
				Utils.loadFile(plugin.getResource("ressources/config.yml"), config);
			} catch (IOException | InvalidConfigurationException e) {
//				e.printStackTrace();
			}*/
		}
		cfg = YamlConfiguration.loadConfiguration(config);

		itemFile = new File("plugins/DragonSim/drops.yml");
		if(!itemFile.exists()) {
			try {
				Utils.loadFile(plugin.getResource("drops.yml"), itemFile);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		itemConfig = YamlConfiguration.loadConfiguration(itemFile);

		altarFile = new File("plugins/DragonSim/altar-register.yml");
		if(!altarFile.exists()) {
			try {
				Utils.loadFile(plugin.getResource("altar-register.yml"), altarFile);
			} catch (IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		altarConfig = YamlConfiguration.loadConfiguration(altarFile);

		getCommand("setaltar").setExecutor(new SetAltarCMD());
		getCommand("se").setExecutor(new SECMD());

		saveDefaultConfig();

		PluginManager pm = Bukkit.getServer().getPluginManager();

		pm.registerEvents(new Altar(), plugin);
		pm.registerEvents(entityHider, plugin);
		pm.registerEvents(new RightClickAltar(), plugin);
		pm.registerEvents(new RecipeInjector(), plugin);

		new LightningRunnable(this).start();

		if(!new File("plugins/DragonSim/logs").exists()) {
			new File("plugins/DragonSim/logs").mkdir();
		}

		Altar.loadAltars(altarConfig);
		CustomItems.loadInv();
	}
	
	public void onDisable() {
		entityHider.close();
		entityHider = null;
	}
	
	public static EntityHider getEntityHider() {
		return entityHider;
	}
	
	public static FileConfiguration getCfg() {
		return cfg;
	}
	public static File getCfgFile() {
		return config;
	}

	public static File getAltarFile() {
		return altarFile;
	}

	public static FileConfiguration getAltarConfig() {
		return altarConfig;
	}

	public static FileConfiguration getItemCfg() {
		return itemConfig;
	}
	public static File getItemFile() {
		return itemFile;
	}
	
	public static Plugin getInstance() {
		return plugin;
	}
}
