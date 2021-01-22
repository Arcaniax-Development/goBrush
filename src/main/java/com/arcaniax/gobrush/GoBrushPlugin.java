package com.arcaniax.gobrush;

import com.arcaniax.gobrush.command.CommandHandler;
import com.arcaniax.gobrush.listener.InventoryClickListener;
import com.arcaniax.gobrush.listener.PlayerInteractListener;
import com.arcaniax.gobrush.listener.PlayerJoinListener;
import com.arcaniax.gobrush.listener.PlayerQuitListener;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static com.arcaniax.gobrush.util.BrushZipManager.setupBrushes;

public class GoBrushPlugin extends JavaPlugin {

	public static GoBrushPlugin plugin;

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
		setupBrushes();
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
		getCommand("gobrush").setExecutor(new CommandHandler());
	}
}
