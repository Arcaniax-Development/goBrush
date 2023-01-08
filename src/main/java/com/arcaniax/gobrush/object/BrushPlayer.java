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

import com.arcaniax.gobrush.Session;

import java.util.UUID;

/**
 * This class contains the object of BrushPlayer. This is the object
 * that contains the settings of the goBrush plugin for a user.
 *
 * @author McJeffr
 */
public class BrushPlayer {

    private UUID uuid;
    private int brushSize, brushIntensity;
    private boolean brushEnabled, flatMode, directionMode, _3dmode, autoRotation;
    private Brush brush;

    /**
     * Default constructor for a BrushPlayer object. This default
     * constructor sets all the values of the attributes of this object to the
     * default values.
     *
     * @param uuid The UUID of the player that this BrushPlayer object
     *             belongs to.
     */
    public BrushPlayer(UUID uuid) {
        this.uuid = uuid;
        this.brushSize = Session.getConfig().getDefaultBrushSize();
        this.brushIntensity = Session.getConfig().getDefaultBrushIntensity();
        this.directionMode = Session.getConfig().isDefaultDirectionMode();
        this.flatMode = Session.getConfig().isDefault3DMode();
        this._3dmode = Session.getConfig().isDefaultBoundingBox();
        this.autoRotation = Session.getConfig().isDefaultAutoRotation();
        this.brushEnabled = Session.getConfig().isDefaultBrushEnabled();
        this.brush = new Brush();
    }

    /**
     * This method toggles whether or not the brush is enabled.
     */
    public void toggleBrushEnabled() {
        brushEnabled = !brushEnabled;
    }

    /**
     * This method toggles whether or not flat mode is enabled.
     */
    public void toggleFlatMode() {
        flatMode = !flatMode;
    }

    /**
     * This method toggles whether or not the bounding box feature is enabled.
     */
    public void toggle3DMode() {
        _3dmode = !_3dmode;
    }

    /**
     * This method toggles whether or not the auto rotate feature is enabled.
     */
    public void toggleAutoRotation() {
        autoRotation = !autoRotation;
    }

    /**
     * Getter for the UUID of the player that this object belongs to.
     *
     * @return The UUID of the player that this object belongs to.
     */
    public UUID getUuid() {
        return uuid;
    }

    /**
     * Setter for the UUID of the player that this objet belongs to.
     *
     * @param uuid The UUID of that player that this object belongs to.
     */
    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * Getter for the brush size.
     *
     * @return The brush size.
     */
    public int getBrushSize() {
        return brushSize;
    }

    /**
     * Setter for the brush size.
     *
     * @param brushSize The brush size.
     */
    public void setBrushSize(int brushSize) {
        this.brushSize = brushSize;
        this.brush.resize(brushSize);
    }

    /**
     * Getter for the brush intensity.
     *
     * @return The brush intensity.
     */
    public int getBrushIntensity() {
        return brushIntensity;
    }

    /**
     * Setter for the brush intensity.
     *
     * @param brushIntensity The brush intensity.
     */
    public void setBrushIntensity(int brushIntensity) {
        this.brushIntensity = brushIntensity;
    }

    /**
     * Getter for the boolean that indicates if the brush is enabled.
     *
     * @return A boolean that indicates if the brush is enabled.
     */
    public boolean isBrushEnabled() {
        return brushEnabled;
    }

    /**
     * Getter for the boolean that indicates if flat mode is enabled.
     *
     * @return A boolean that indicates if flat mode is enabled.
     */
    public boolean isFlatMode() {
        return flatMode;
    }

    /**
     * Getter for the boolean that indicates if mountain mode is enabled.
     *
     * @return A boolean that indicates if mountain mode is enabled.
     */
    public boolean isDirectionMode() {
        return directionMode;
    }

    /**
     * Getter for the boolean that indicates if the bounding box feature is
     * enabled.
     *
     * @return A boolean that indicates if the bounding box feature is enabled.
     */
    public boolean is3DMode() {
        return this._3dmode;
    }

    /**
     * Getter for the boolean that indicats if the auto rotation feature is
     * enabled.
     *
     * @return A boolean that indicates if the auto rotation feature is enabled.
     */
    public boolean isAutoRotation() {
        return autoRotation;
    }

    /**
     * Getter for the brush that the user has selected.
     *
     * @return The brush that the user has selected.
     */
    public Brush getBrush() {
        return brush;
    }

    /**
     * Setter for the brush that the user has selected.
     *
     * @param brush The brush that the user has selected.
     */
    public void setBrush(Brush brush) {
        this.brush = brush;
    }

    /**
     * Getter for the maximum brush size that the user can select.
     *
     * @return The maximum brush size that the user can select.
     */
    public int getMaxBrushSize() {
        return Session.getConfig().getMaxBrushSize();
    }

    /**
     * Getter for the maximum brush intensity that the user can select.
     *
     * @return The maximum brush intensity that the user can select.
     */
    public int getMaxBrushIntensity() {
        return Session.getConfig().getMaxBrushIntensity();
    }

    public void toggleDirectionMode() {
        directionMode = !directionMode;
    }

}
