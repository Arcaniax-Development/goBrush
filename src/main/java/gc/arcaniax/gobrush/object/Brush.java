package gc.arcaniax.gobrush.object;

import gc.arcaniax.gobrush.Main;
import gc.arcaniax.gobrush.Session;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * This class contains the Brush object. This object contains all information
 * about a brush, including the patterns. This object can be attached to a
 * player to create resized brushes.
 *
 * @author McJeffr
 */
@SuppressWarnings("rawtypes")
public class Brush implements Comparable {

    private static final String DEFAULT_NAME = Session.getConfig().getDefaultBrushName();
    private static final int DEFAULT_SIZE = 15;
    private String name;
    private BufferedImage unmodifiedPattern;
    private BufferedImage croppedPattern;
    private int size;

    /**
     * Default constructor of a Brush object. This constructor will pick all the
     * default values registered on config as the default brush.
     */
    public Brush() {
        this.name = DEFAULT_NAME;
        this.unmodifiedPattern = getPattern(DEFAULT_NAME);
        this.size = DEFAULT_SIZE;

        BufferedImage pattern = new BufferedImage(size, size, 1);
        Graphics2D graphic = pattern.createGraphics();
        graphic.drawImage(unmodifiedPattern, 0, 0, size, size, null);
        graphic.dispose();
        this.croppedPattern = pattern;
    }

    /**
     * Constructor for storing brushes that are seen as valid. These brushes
     * contain no size information and can therefore not be used by players
     * unless the resize method is called on them.
     *
     * @param name The name of the brush, INCLUDING the file extension.
     */
    public Brush(String name) {
        this.name = name;
        this.unmodifiedPattern = getPattern(name);
        this.croppedPattern = null;
        this.size = 0;
    }

    /**
     * Constructor for connecting a brush to a player. This constructor allows
     * the setting of the size of the brush, making it available for safe use by
     * players.
     *
     * @param name The name of the brush, INCLUDING the file extension.
     * @param size The size that the brush needs to be.
     */
    public Brush(String name, int size) {
        this.name = name;
        this.unmodifiedPattern = getPattern(name);
        System.out.println(unmodifiedPattern);
        this.size = size;

        BufferedImage pattern = new BufferedImage(size, size, 1);
        Graphics2D graphic = pattern.createGraphics();
        graphic.drawImage(unmodifiedPattern, 0, 0, size, size, null);
        graphic.dispose();
        this.croppedPattern = pattern;
    }

    private BufferedImage getPattern(String name) {
        BufferedImage pattern;
        try {
            pattern = ImageIO.read(new File(Main.getPlugin().getDataFolder() + "/brushes/" + name));
        } catch (IOException ex) {
            pattern = null;
        }
        return pattern;
    }

    public void resize(int size) {
        this.size = size;
        BufferedImage pattern = new BufferedImage(size, size, 1);
        Graphics2D graphic = pattern.createGraphics();
        graphic.drawImage(unmodifiedPattern, 0, 0, size, size, null);
        graphic.dispose();
        this.croppedPattern = pattern;
    }

    /**
     * Getter for the name of the brush.
     *
     * @return The identifying name of this brush. All spaces have been replaced
     * with underscored and the extension has been removed.
     */
    public String getName() {
        return this.name;
    }

    public int getSize() {
        return size;
    }

    public BufferedImage getCroppedPattern() {
        return croppedPattern;
    }

    @Override
    public int compareTo(Object o) {
        return getName().compareTo(((Brush) o).getName());
    }
}
