package com.arcaniax.gobrush.object;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

/**
 * This method contains the object of Config. This object contains all the
 * values from the config file and it holds functions to reload this file or
 * fetch and store any information from the config file.
 *
 * @author McJeffr
 */
public class Config {

    private boolean defaultDirectionMode, default3DMode, defaultBoundingBox, defaultAutoRotation, defaultBrushEnabled;
    private int defaultBrushSize, maxBrushSize, defaultBrushIntensity, maxBrushIntensity, imgLoreSize;
    private String defaultBrushName;
    private List<String> disabledWorlds;

    /**
     * Constructor for the Config object. This constructor accepts the config
     * file of the goBrush plugin. No checks are in place to make sure the
     * correct configuration file is served, so check if it is.
     *
     * @param config The configuration file of the goBrush plugin.
     */
    public Config(FileConfiguration config) {
        this.defaultBrushSize = config.getInt("defaults.size");
        this.defaultBrushIntensity = config.getInt("defaults.intensity");
        this.imgLoreSize = config.getInt("defaults.imgloresize");
        this.defaultDirectionMode = !config.getBoolean("defaults.Directionmode");
        this.default3DMode = config.getBoolean("defaults.3Dmode");
        this.defaultBoundingBox = config.getBoolean("defaults.boundingbox");
        this.defaultAutoRotation = config.getBoolean("defaults.autorotation");
        this.defaultBrushEnabled = config.getBoolean("defaults.brushenabled");
        this.defaultBrushName = config.getString("defaults.brushname");
        this.maxBrushSize = config.getInt("maximums.size");
        this.maxBrushIntensity = config.getInt("maximums.intensity");
        this.disabledWorlds = config.getStringList("disabledworlds");
        if (this.maxBrushSize % 2 == 0) {
            this.maxBrushSize++;
        }
        if (this.defaultBrushSize % 2 == 0) {
            this.defaultBrushSize++;
        }
    }

    /**
     * This method reloads all the stored values from the configuration file.
     *
     * @param config The configuration file of the goBrush plugin.
     */
    public void reload(FileConfiguration config) {
        this.defaultBrushSize = config.getInt("defaults.size");
        this.defaultBrushIntensity = config.getInt("defaults.intensity");
        this.imgLoreSize = config.getInt("defaults.imgloresize");
        this.defaultDirectionMode = config.getBoolean("defaults.directionmode");
        this.default3DMode = config.getBoolean("defaults.3Dmode");
        this.defaultBoundingBox = config.getBoolean("defaults.3dmode");
        this.defaultAutoRotation = config.getBoolean("defaults.autorotation");
        this.defaultBrushEnabled = config.getBoolean("defaults.brushenabled");
        this.defaultBrushName = config.getString("defaults.brushname");
        this.maxBrushSize = config.getInt("maximums.size");
        this.maxBrushIntensity = config.getInt("maximums.intensity");
        this.disabledWorlds = config.getStringList("disabledworlds");
        if (this.maxBrushSize % 2 == 0) {
            this.maxBrushSize++;
        }
        if (this.defaultBrushSize % 2 == 0) {
            this.defaultBrushSize++;
        }
    }

    public boolean isDefaultDirectionMode() {
        return defaultDirectionMode;
    }

    public boolean isDefault3DMode() {
        return default3DMode;
    }

    public boolean isDefaultBoundingBox() {
        return defaultBoundingBox;
    }

    public boolean isDefaultAutoRotation() {
        return defaultAutoRotation;
    }


    public boolean isDefaultBrushEnabled() {
        return defaultBrushEnabled;
    }

    public int getDefaultBrushSize() {
        return defaultBrushSize;
    }

    public int getMaxBrushSize() {
        return maxBrushSize;
    }

    public int getDefaultBrushIntensity() {
        return defaultBrushIntensity;
    }

    public int getMaxBrushIntensity() {
        return maxBrushIntensity;
    }

    public int getImgLoreSize() {
        return imgLoreSize;
    }

    public String getDefaultBrushName() {
        return defaultBrushName;
    }

    public List<String> getDisabledWorlds() {
        return disabledWorlds;
    }
}
