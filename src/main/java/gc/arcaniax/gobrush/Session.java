package gc.arcaniax.gobrush;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import gc.arcaniax.gobrush.object.Brush;
import gc.arcaniax.gobrush.object.BrushMenu;
import gc.arcaniax.gobrush.object.BrushPlayer;
import gc.arcaniax.gobrush.object.Config;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.*;

public class Session {
    private static Map<UUID, BrushPlayer> brushPlayers;
    private static Map<String, Brush> validBrushes;
    private static Config config;
    private static WorldEditPlugin worldEdit;
    private static BrushMenu brushMenu;

    public static void initializeBrushPlayers() {
        brushPlayers = new HashMap();
        for (Player p : Bukkit.getOnlinePlayers()) {
            addBrushPlayer(p.getUniqueId());
        }
    }

    public static int initializeValidBrushes() {
        validBrushes = new HashMap();
        int amountOfValidBrushes = 0;
        File dir = new File(Main.getPlugin().getDataFolder() + "/brushes");
        if (!dir.exists()) {
            dir.mkdir();
            return 0;
        }
        File[] brushes = dir.getAbsoluteFile().listFiles();
        for (int i = 0; i < brushes.length; i++) {
            File file = brushes[i];
            if ((!file.getAbsoluteFile().isDirectory()) && ((file.getName().endsWith(".png")) || (file.getName().endsWith(".jpg")) || (file.getName().endsWith(".jpeg")))) {
                addBrush(new Brush(file.getName()));
                amountOfValidBrushes++;
            }
        }
        return amountOfValidBrushes;
    }

    public static void initializeConfig(FileConfiguration config) {
        config = new Config(config);
    }

    public static void initializeBrushMenu() {
        List<Brush> brushes = new ArrayList();
        for (Map.Entry<String, Brush> brush : validBrushes.entrySet()) {
            brushes.add(brush.getValue());
        }
        Collections.sort(brushes);
        brushMenu = new BrushMenu(brushes);
    }

    public static boolean containsBrushPlayer(UUID uuid) {
        return brushPlayers.containsKey(uuid);
    }

    public static BrushPlayer getBrushPlayer(UUID uuid) {
        if (containsBrushPlayer(uuid)) {
            return brushPlayers.get(uuid);
        }
        return null;
    }

    public static boolean addBrushPlayer(UUID uuid) {
        if (!containsBrushPlayer(uuid)) {
            brushPlayers.put(uuid, new BrushPlayer(uuid));
            return true;
        }
        return false;
    }

    public static boolean removeBrushPlayer(UUID uuid) {
        if (containsBrushPlayer(uuid)) {
            brushPlayers.remove(uuid);
            return true;
        }
        return false;
    }

    public static boolean containsBrush(String name) {
        return validBrushes.containsKey(name);
    }

    public static Brush getBrush(String name) {
        if (containsBrush(name)) {
            return validBrushes.get(name);
        }
        return null;
    }

    public static Map<String, Brush> getBrushes() {
        return validBrushes;
    }

    public static boolean addBrush(Brush brush) {
        if (!containsBrush(brush.getName())) {
            validBrushes.put(brush.getName(), brush);
            return true;
        }
        return false;
    }

    public static boolean removeBrush(String name) {
        if (containsBrush(name)) {
            validBrushes.remove(name);
            return true;
        }
        return false;
    }

    public static Config getConfig() {
        return config;
    }

    public static WorldEditPlugin getWorldEdit() {
        return worldEdit;
    }

    public static void setWorldEdit(WorldEditPlugin worldEdit) {
        worldEdit = worldEdit;
    }

    public static BrushMenu getBrushMenu() {
        return brushMenu;
    }
}
