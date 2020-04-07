package gc.arcaniax.gobrush;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import gc.arcaniax.gobrush.command.Cmd;
import gc.arcaniax.gobrush.listener.InventoryClickListener;
import gc.arcaniax.gobrush.listener.PlayerInteractListener;
import gc.arcaniax.gobrush.listener.PlayerJoinListener;
import gc.arcaniax.gobrush.listener.PlayerQuitListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Main extends JavaPlugin {

	public static Main plugin;
	public int amountOfValidBrushes;

	/**
	 * This method returns the main JavaPlugin instance, used for several things.
	 *
	 * @return The main JavaPlugin instance.
	 */
	public static JavaPlugin getPlugin() {
		return plugin;
	}

	@Override
	public void onEnable() {
		plugin = this;
		saveDefaultConfig();
		Session.initializeConfig(this.getConfig());
		Session.initializeBrushPlayers();
		try {
			amountOfValidBrushes = Session.initializeValidBrushes();
			plugin.getLogger().log(Level.INFO, "Registered {0} brushes.", amountOfValidBrushes);
			if (amountOfValidBrushes == 0) {
				plugin.getLogger().log(Level.WARNING, "Could not find any brushes in the folder!");
				plugin.getLogger().log(Level.WARNING, "Make sure to put in the brushes from the downloaded ZIP!");
				plugin.setEnabled(false);
			}
			Session.initializeBrushMenu();
		} catch (Exception ex) {
			plugin.getLogger().log(Level.WARNING, "Could not find any brushes in the folder!");
			plugin.getLogger().log(Level.WARNING, "Make sure to put in the brushes from the downloaded ZIP!");
			plugin.setEnabled(false);
		}
		Session.setWorldEdit((WorldEditPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldEdit"));
		registerListeners();
		registerCommands();
	}

	private void registerListeners() {
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new InventoryClickListener(), this);
		Bukkit.getServer().getPluginManager().registerEvents(new PlayerInteractListener(), this);
	}

	/**
	 * This method registers the commands of the plugin.
	 */
	private void registerCommands() {
		getCommand("gobrush").setExecutor(new Cmd());
	}
}
