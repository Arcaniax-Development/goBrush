/*
 *                              ____                 _
 *                             |  _ \               | |
 *                   __ _  ___ | |_) |_ __ _   _ ___| |__
 *                  / _` |/ _ \|  _ <| '__| | | / __| '_ \
 *                 | (_| | (_) | |_) | |  | |_| \__ \ | | |
 *                  \__, |\___/|____/|_|   \__,_|___/_| |_|
 *                   __/ |
 *                  |___/
 *
 *    goBrush is designed to streamline and simplify your mountain building experience.
 *                            Copyright (C) 2022 Arcaniax
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.arcaniax.gobrush;

import com.arcaniax.gobrush.object.Brush;
import com.arcaniax.gobrush.object.BrushMenu;
import com.arcaniax.gobrush.object.BrushPlayer;
import com.arcaniax.gobrush.object.Config;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

/**
 * This class contains all the information that needs to be stored during a
 * session. A session is a runtime.
 *
 * @author McJeffr
 */
public class Session {

    private static Map<UUID, BrushPlayer> brushPlayers;
    private static Map<String, Brush> validBrushes;
    private static Config config;
    private static WorldEditPlugin worldEdit;
    private static BrushMenu brushMenu;

    /**
     * This method initializes the HashMap containing the BrushPlayer objects.
     * Calling this method will reset the player configurations, only use this
     * method when enabling the plugin.
     */
    public static void initializeBrushPlayers() {
        brushPlayers = new HashMap<>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            Session.addBrushPlayer(p.getUniqueId());
        }
    }

    /**
     * This method initializes the HashMap containing the BrushHistory objects.
     * This method should only be called upon plugin startup, as it will clear
     * all of the history lists.
     */

    /**
     * This method initializes the HashMap containing the valid brushes. Calling
     * this method will reset the loaded brushes, only use this method when
     * enabling the plugin.
     *
     * @return The amount of brushes that have been initialized.
     */
    public static int initializeValidBrushes() {
        validBrushes = new HashMap<>();
        AtomicInteger amountOfValidBrushes = new AtomicInteger();
        File dir = new File(GoBrushPlugin.getPlugin().getDataFolder() + "/brushes");
        if (!dir.exists()) {
            dir.mkdir();
            return 0;
        }
        File[] brushes = dir.getAbsoluteFile().listFiles();
        IntStream.range(0, brushes.length).parallel().forEach(value -> {
            File file = brushes[value];
            if ((!file.getAbsoluteFile().isDirectory()) && ((file.getName().endsWith(".png")) || (file
                    .getName()
                    .endsWith(".jpg")) || (file.getName().endsWith(".jpeg")))) {
                Session.addBrush(new Brush(file.getName()));
                amountOfValidBrushes.getAndIncrement();
            }
        });
        return amountOfValidBrushes.get();
    }

    /**
     * This method initializes the Config object from the provided configuration
     * file. This method will reset any other configuration files stored. Do not
     * use this method to reload the configuration file, instead use that of
     * Config#reload(ConfigurationFile config) after fetching this Config object
     * using Session#getConfig().
     *
     * @param config The configuration file of the goBrush plugin.
     */
    public static void initializeConfig(FileConfiguration config) {
        Session.config = new Config(config);
    }

    /**
     * This method initializes the BrushMenu object that contains all the
     * brushes. This method should not be executed during runtime as it will
     * generate a new inventory, leaving the old one disconnected.
     */
    @SuppressWarnings("unchecked")
    public static void initializeBrushMenu() {
        List<Brush> brushes = new ArrayList<>();
        for (Map.Entry<String, Brush> brush : validBrushes.entrySet()) {
            brushes.add(brush.getValue());
        }
        Collections.sort(brushes);
        Session.brushMenu = new BrushMenu(brushes);
    }

    /**
     * This method checks if the UUID provided is already part of the HashMap of
     * player configurations.
     *
     * @param uuid The UUID of the player that needs to be checked.
     * @return True if the HashMap of players contains the provided UUID, false
     *         otherwise.
     */
    public static boolean containsBrushPlayer(UUID uuid) {
        return brushPlayers.containsKey(uuid);
    }

    /**
     * This method gets the BrushPlayer object of a player with the provided
     * UUID.
     *
     * @param uuid The UUID of the player of which the BrushPlayer object needs
     *             to be returned of.
     * @return The BrushPlayer object of a player, or null when the uuid is not
     *         in the HashMap.
     */
    public static BrushPlayer getBrushPlayer(UUID uuid) {
        if (containsBrushPlayer(uuid)) {
            return brushPlayers.get(uuid);
        } else {
            return null;
        }
    }

    /**
     * This method adds a new BrushPlayer object to the HashMap of player
     * configurations.
     *
     * @param uuid The UUID of the new player that needs to be added to the
     *             HashMap of player configurations.
     * @return True if the user was added successfully, false if it was not
     *         added successfully (this occurs when the UUID is already in the HashMap).
     */
    public static boolean addBrushPlayer(UUID uuid) {
        if (!containsBrushPlayer(uuid)) {
            brushPlayers.put(uuid, new BrushPlayer(uuid));
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method removes a BrushPlayer object from the HashMap of player
     * configurations.
     *
     * @param uuid The UUID of the player whose BrushPlayer object needs to be
     *             removed from the HashMap of player configurations.
     * @return True if the player's BrushPlayer was removed from the HashMap,
     *         false otherwise.
     */
    public static boolean removeBrushPlayer(UUID uuid) {
        if (containsBrushPlayer(uuid)) {
            brushPlayers.remove(uuid);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method checks if a player has an undo history.
     *
     * @param uuid The UUID of the player that needs to be checked.
     * @return True if the player has an undo history, false otherwise.
     */

    /**
     * This method fetches the undo history of a player from their UUID.
     *
     * @param uuid The UUID of the player of which the undo history needs to be
     * returned.
     * @return The undo history in a List or null when no undo history is
     * present.
     */

    /**
     * This method checks if the String provided is a name of a valid brush.
     *
     * @param name The name of a brush that needs to be checked.
     * @return True if the HashMap of valid brushes contains the provided name,
     *         false otherwise.
     */
    public static boolean containsBrush(String name) {
        return validBrushes.containsKey(name);
    }

    /**
     * This method gets a brush with the provided name.
     *
     * @param name The name of a brush that needs to be returned.
     * @return The Brush object, or null when the name is not in the HashMap.
     */
    public static Brush getBrush(String name) {
        if (containsBrush(name)) {
            return validBrushes.get(name);
        } else {
            return null;
        }
    }

    /**
     * This method gets all the registered brushes and returns them in a map.
     *
     * @return A map of all registered brushes.
     */
    public static Map<String, Brush> getBrushes() {
        return validBrushes;
    }

    /**
     * This method adds a new Brush object to the HashMap of valid brushes.
     *
     * @param brush the Brush object that needs to be added. The key will be the
     *              brush name.
     * @return True if the brush was added successfully, false if it was not
     *         added successfully (this occurs when the brush is already in the
     *         HashMap).
     */
    public static boolean addBrush(Brush brush) {
        if (!containsBrush(brush.getName())) {
            validBrushes.put(brush.getName(), brush);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method removes a Brush object from the HashMap of valid brushes.
     *
     * @param name The name of the brush that needs to be removed.
     * @return True if the brush was removed from the HashMap, false otherwise.
     */
    public static boolean removeBrush(String name) {
        if (containsBrush(name)) {
            validBrushes.remove(name);
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method returns the config object that contains all the data from the
     * configuration file.
     *
     * @return The config object that contains all data from the configuration
     *         file.
     */
    public static Config getConfig() {
        return config;
    }

    /**
     * This method gets the WorldEditPlugin instance.
     *
     * @return The WorldEditPlugin instance.
     */
    public static WorldEditPlugin getWorldEdit() {
        return worldEdit;
    }

    /**
     * This method sets the WorldEditPlugin instance.
     *
     * @param worldEdit The WorldEditPlugin instance.
     */
    public static void setWorldEdit(WorldEditPlugin worldEdit) {
        Session.worldEdit = worldEdit;
    }

    /**
     * This method gets the BrushMenu object that stores all brushes.
     *
     * @return
     */
    public static BrushMenu getBrushMenu() {
        return brushMenu;
    }

}
