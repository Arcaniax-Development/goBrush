package com.arcaniax.gobrush;

import com.arcaniax.gobrush.command.CommandHandler;
import com.arcaniax.gobrush.listener.InventoryClickListener;
import com.arcaniax.gobrush.listener.PlayerInteractListener;
import com.arcaniax.gobrush.listener.PlayerJoinListener;
import com.arcaniax.gobrush.listener.PlayerQuitListener;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;

public class GoBrushPlugin extends JavaPlugin {

	public static GoBrushPlugin plugin;
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
				plugin.getLogger().log(Level.INFO,"Downloading cliff.png from GitHub, please wait...");
				try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL("https://raw.githubusercontent.com/N0tMyFaultOG/goBrush-Assets/main/cliff.png").openStream());
					 FileOutputStream fileOutputStream = new FileOutputStream(GoBrushPlugin.getPlugin().getDataFolder() + "/brushes/cliff.png")) {
					byte[] dataBuffer = new byte[1024];
					int bytesRead;
					while ((bytesRead = bufferedInputStream.read(dataBuffer, 0, 1024)) != -1) {
						fileOutputStream.write(dataBuffer, 0, bytesRead);
					}
					plugin.getLogger().log(Level.INFO, "cliff.png has been downloaded successfully.");
					GoBrushPlugin.getPlugin().reloadConfig();
					Session.getConfig().reload(GoBrushPlugin.getPlugin().getConfig());
					int amountOfValidBrushes = Session.initializeValidBrushes();
					if (amountOfValidBrushes == 1) {
						GoBrushPlugin.getPlugin().getLogger().log(Level.INFO, "Registered 1 brush.");
					} else {
						GoBrushPlugin.getPlugin().getLogger().log(Level.INFO, "Registered {0} brushes.", amountOfValidBrushes);
					}
					Session.initializeBrushMenu();
					Session.initializeBrushPlayers();
					plugin.getLogger().log(Level.INFO, "You can find more brushes on the internet or here:");
					plugin.getLogger().log(Level.INFO, "https://github.com/N0tMyFaultOG/goBrush-Assets/blob/main/brushes.zip");
				} catch (IOException e) {
					plugin.getLogger().log(Level.SEVERE, "Could not download brushes. Please download them manually here and put them into /plugins/goBrush/brushes");
					plugin.getLogger().log(Level.SEVERE, "https://github.com/N0tMyFaultOG/goBrush-Assets/blob/main/brushes.zip");
				}
			}
		} catch (Exception ex) {
			plugin.getLogger().log(Level.SEVERE, "Could not download brushes. Please download them manually here and put them into /plugins/goBrush/brushes");
			plugin.getLogger().log(Level.SEVERE, "https://github.com/N0tMyFaultOG/goBrush-Assets/blob/main/brushes.zip");
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
		getCommand("gobrush").setExecutor(new CommandHandler());
	}
}
