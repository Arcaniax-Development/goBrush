package com.arcaniax.gobrush;

import com.arcaniax.gobrush.command.CommandHandler;
import com.arcaniax.gobrush.listener.InventoryClickListener;
import com.arcaniax.gobrush.listener.PlayerInteractListener;
import com.arcaniax.gobrush.listener.PlayerJoinListener;
import com.arcaniax.gobrush.listener.PlayerQuitListener;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedInputStream;
import java.io.File;
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
				plugin.getLogger().log(Level.INFO,"Downloading brushes from GitHub, please wait...");
				try (BufferedInputStream bufferedInputStream = new BufferedInputStream(new URL("https://github.com/Arcaniax-Development/goBrush-Assets/blob/main/brushes.zip?raw=true").openStream());
					 FileOutputStream fileOutputStream = new FileOutputStream(GoBrushPlugin.getPlugin().getDataFolder() + "/brushes/brushes.zip")) {
					byte[] dataBuffer = new byte[1024];
					int bytesRead;
					while ((bytesRead = bufferedInputStream.read(dataBuffer, 0, 1024)) != -1) {
						fileOutputStream.write(dataBuffer, 0, bytesRead);
					}
					plugin.getLogger().log(Level.INFO, "Brushes have been downloaded successfully.");
					try {
						plugin.getLogger().log(Level.INFO, "Extracting brushes...");
						ZipFile zipFile = new ZipFile(GoBrushPlugin.getPlugin().getDataFolder() + "/brushes/brushes.zip");
						zipFile.extractAll(GoBrushPlugin.getPlugin().getDataFolder() + "/brushes/");
							GoBrushPlugin.getPlugin().reloadConfig();
							Session.getConfig().reload(GoBrushPlugin.getPlugin().getConfig());
							int amountOfValidBrushes = Session.initializeValidBrushes();
							Session.initializeBrushMenu();
							Session.initializeBrushPlayers();
						    GoBrushPlugin.getPlugin().getLogger().log(Level.INFO, "Registered {0} brushes.", amountOfValidBrushes);
					} catch (ZipException e) {
						e.printStackTrace();
					}
				} catch (IOException e) {
					plugin.getLogger().log(Level.SEVERE, "Could not download brushes. Please download them manually and put them into /plugins/goBrush/brushes");
					plugin.getLogger().log(Level.SEVERE, "https://github.com/Arcaniax-Development/goBrush-Assets/blob/main/brushes.zip?raw=true");
				}
			}
		} catch (Exception ex) {
			plugin.getLogger().log(Level.SEVERE, "Could not download brushes. Please download them manually here and put them into /plugins/goBrush/brushes");
			plugin.getLogger().log(Level.SEVERE, "https://github.com/Arcaniax-Development/goBrush-Assets/blob/main/brushes.zip?raw=true");
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
