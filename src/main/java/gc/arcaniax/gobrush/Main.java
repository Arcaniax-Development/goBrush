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

public class Main
        extends JavaPlugin {
    public static Main plugin;
    public int amountOfValidBrushes;

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        Session.initializeConfig(getConfig());
        Session.initializeBrushPlayers();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Main.this.amountOfValidBrushes = Session.initializeValidBrushes();
                Main.plugin.getLogger().log(Level.INFO, "Registered {0} brushes.", Integer.valueOf(Main.this.amountOfValidBrushes));
                if (Main.this.amountOfValidBrushes == 0) {
                    Main.plugin.getLogger().log(Level.WARNING, "Could not find any brushes in the folder!");
                    Main.plugin.getLogger().log(Level.WARNING, "Make sure to put in the brushes from the downloaded ZIP!");
                    Main.plugin.setEnabled(false);
                }
                Session.initializeBrushMenu();
            } catch (Exception ex) {
                Main.plugin.getLogger().log(Level.WARNING, "Could not find any brushes in the folder!");
                Main.plugin.getLogger().log(Level.WARNING, "Make sure to put in the brushes from the downloaded ZIP!");
                Main.plugin.setEnabled(false);
            }
            if (Bukkit.getPluginManager().getPlugin("FastAsyncWorldEdit") == null) {
                getLogger().log(Level.SEVERE, "FastAsyncWorldEdit is required. Disabling goBrush.");
                getLogger().log(Level.SEVERE, "https://www.spigotmc.org/resources/fast-async-worldedit-voxelsniper.13932/");
                Bukkit.getPluginManager().disablePlugin(this);
                return;
            }
        });
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

    private void registerCommands() {
        getCommand("gobrush").setExecutor(new Cmd());
    }
}
