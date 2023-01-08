/*
 * goBrush is designed to streamline and simplify your mountain building experience.
 * Copyright (C) Arcaniax-Development
 * Copyright (C) Arcaniax team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.arcaniax.gobrush.object;

import com.arcaniax.gobrush.GoBrushPlugin;
import com.arcaniax.gobrush.Session;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
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
    private final String name;
    private final BufferedImage unmodifiedPattern;
    public int amountOfValidBrushes;
    private BufferedImage croppedPattern;
    private int size;

    /**
     * Default constructor of a Brush object. This constructor will pick all the
     * default values registered on config as the default brush.
     */
    public Brush() {
        amountOfValidBrushes = Session.initializeValidBrushes();
        if (amountOfValidBrushes == 0) {
            this.name = "No brushes installed";
        } else {
            this.name = DEFAULT_NAME;
        }
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
            pattern = ImageIO.read(new File(GoBrushPlugin.getPlugin().getDataFolder() + "/brushes/" + name));
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
     *         with underscored and the extension has been removed.
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
