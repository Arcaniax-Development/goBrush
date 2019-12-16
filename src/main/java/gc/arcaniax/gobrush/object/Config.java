package gc.arcaniax.gobrush.object;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Config {
    private boolean defaultDirectionMode;
    private boolean default3DMode;
    private boolean defaultBoundingBox;
    private boolean defaultAutoRotation;
    private boolean defaultBrushEnabled;
    private int defaultBrushSize;
    private int maxBrushSize;
    private int defaultBrushIntensity;
    private int maxBrushIntensity;
    private int imgLoreSize;
    private String defaultBrushName;
    private List<String> disabledWorlds;

    public Config(FileConfiguration config) {
        this.defaultBrushSize = config.getInt("defaults.size");
        this.defaultBrushIntensity = config.getInt("defaults.intensity");
        this.imgLoreSize = config.getInt("defaults.imgloresize");
        this.defaultDirectionMode = (!config.getBoolean("defaults.Directionmode"));
        this.default3DMode = config.getBoolean("defaults.3Dmode");
        this.defaultBoundingBox = config.getBoolean("defaults.boundingbox");
        this.defaultAutoRotation = config.getBoolean("defaults.autorotation");
        this.defaultBrushEnabled = config.getBoolean("defaults.brushenabled");
        this.defaultBrushName = config.getString("defaults.brushname");
        this.maxBrushSize = config.getInt("maximums.size");
        this.maxBrushIntensity = config.getInt("maximums.intensity");
        this.disabledWorlds = config.getStringList("disabledworlds");
        if (this.maxBrushSize % 2 == 0) {
            this.maxBrushSize += 1;
        }
        if (this.defaultBrushSize % 2 == 0) {
            this.defaultBrushSize += 1;
        }
    }

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
            this.maxBrushSize += 1;
        }
        if (this.defaultBrushSize % 2 == 0) {
            this.defaultBrushSize += 1;
        }
    }

    public boolean isDefaultDirectionMode() {
        return this.defaultDirectionMode;
    }

    public boolean isDefault3DMode() {
        return this.default3DMode;
    }

    public boolean isDefaultBoundingBox() {
        return this.defaultBoundingBox;
    }

    public boolean isDefaultAutoRotation() {
        return this.defaultAutoRotation;
    }

    public boolean isDefaultBrushEnabled() {
        return this.defaultBrushEnabled;
    }

    public int getDefaultBrushSize() {
        return this.defaultBrushSize;
    }

    public int getMaxBrushSize() {
        return this.maxBrushSize;
    }

    public int getDefaultBrushIntensity() {
        return this.defaultBrushIntensity;
    }

    public int getMaxBrushIntensity() {
        return this.maxBrushIntensity;
    }

    public int getImgLoreSize() {
        return this.imgLoreSize;
    }

    public String getDefaultBrushName() {
        return this.defaultBrushName;
    }

    public List<String> getDisabledWorlds() {
        return this.disabledWorlds;
    }
}
