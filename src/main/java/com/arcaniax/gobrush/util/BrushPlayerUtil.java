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
package com.arcaniax.gobrush.util;

import com.arcaniax.gobrush.Session;
import com.arcaniax.gobrush.object.BrushPlayer;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.math.Vector3;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

/**
 * This class contains a bunch of utilities that can be used for determinating
 * several important factors regarding location of a player.
 *
 * @author Arcaniax, McJeffr
 */
public class BrushPlayerUtil {

    public static Location getClosest(Player player, Location _loc, Location l, int brushSize, EditSession session) {
        Location loc = _loc.clone();

        while (loc.getBlock().getType() == XMaterial.AIR.parseMaterial()
                || (!(session.getMask() == null || session.getMask().test(Vector3
                .at(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ())
                .toBlockPoint())))
                && (loc.distance(l.clone().add(0.5, 0.5, 0.5)) < ((double) brushSize / (double) 4))) {
            Vector v = player.getEyeLocation().getDirection();
            loc.add(v.multiply(0.5));
            if (!BlockUtils.isLoaded(loc)) {
                return null;
            }
            if (loc.getBlockY() <= BlockUtils.getWorldMin(loc)) {
                break;
            }
            if (loc.getBlockY() > BlockUtils.getWorldMax(loc)) {
                return null;
            }
            if (loc.distance(_loc) > 200) {
                return null;
            }
        }
        return loc;
    }

    public static Location getClosest(Player player) {
        Location loc = player.getEyeLocation();
        while (loc.getBlock().getType() == XMaterial.AIR.parseMaterial()) {
            Vector v = player.getEyeLocation().getDirection();
            loc.add(v.multiply(0.5));
            if (!BlockUtils.isLoaded(loc)) {
                return null;
            }
            if (loc.getBlockY() <= BlockUtils.getWorldMin(loc)) {
                break;
            }
            if (loc.getBlockY() > BlockUtils.getWorldMax(loc)) {
                return null;
            }
            if (loc.distance(player.getEyeLocation()) > 200) {
                return null;
            }
        }
        return loc;
    }

    /**
     * This method fetches the cardinal direction the player is looking at. This
     * is either N, E, S or W.
     *
     * @param player The player of which the cardinal location needs to be
     *               retrieved of.
     * @return The cardinal direction the player is facing.
     */
    public static String getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
        if (rotation < 0.0D) {
            rotation += 360.0D;
        }
        if ((0.0D <= rotation) && (rotation < 45.0D)) {
            return "W";
        }
        if ((45.0D <= rotation) && (rotation < 135.0D)) {
            return "N";
        }
        if ((135.0D <= rotation) && (rotation < 225.0D)) {
            return "E";
        }
        if ((225.0D <= rotation) && (rotation < 315.0D)) {
            return "S";
        }
        if ((315.0D <= rotation) && (rotation < 360.0D)) {
            return "W";
        }
        return null;
    }

    /**
     * This method fetches the height of a brush pattern based of several
     * factors.
     *
     * @param player      The player of who the brush needs to be fetched.
     * @param x           The x coord of the area that needs to be converted.
     * @param z           The z coord of the area that needs to be converted.
     * @param cardinalDir The cardinal direction the player is facing.
     * @return A double value containing the height of the brush pattern.
     */
    public static double getHeight(Player player, int x, int z, String cardinalDir) {
        BrushPlayer brushPlayer = Session.getBrushPlayer(player.getUniqueId());
        int size = brushPlayer.getBrush().getCroppedPattern().getHeight();
        int _x = x;
        int _z = z;
        if (brushPlayer.isAutoRotation()) {
            switch (cardinalDir) {
                case "S":
                    x = size - _x;
                    z = size - _z;
                    break;
                case "W":
                    x = size - _z;
                    z = _x;
                    break;
                case "E":
                    x = _z;
                    z = size - _x;
                    break;
            }
        }
        int rgb = 0;
        if ((z < size) && (x < size) && (x >= 0) && (z >= 0)) {
            rgb = brushPlayer.getBrush().getCroppedPattern().getRGB(x, z);
        }
        int red = rgb >>> 16 & 0xFF;
        int green = rgb >>> 8 & 0xFF;
        int blue = rgb & 0xFF;
        if ((red == 0) && (green == 0) && (blue == 0)) {
            return 0.0D;
        }
        float grayScale = (red + blue + green) / 3.0F / 255.0F;

        double height = grayScale * brushPlayer.getBrushIntensity();
        return height;

    }

}
