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

import org.bukkit.Location;

public class BlockUtils {

    private static boolean NEWER_WORLD_VERSION = false;

    public static void setNewerWorldVersion(boolean newer) {
        NEWER_WORLD_VERSION = newer;
    }

    public static boolean isLoaded(Location l) {
        int x = l.getBlockX() >> 4;
        int z = l.getBlockZ() >> 4;
        return l.getWorld().isChunkLoaded(x, z);
    }

    public static int getWorldMin(Location loc) {
        if (NEWER_WORLD_VERSION) {
            return loc.getWorld().getMinHeight();
        } else {
            return 0;
        }
    }

    public static int getWorldMax(Location loc) {
        if (NEWER_WORLD_VERSION) {
            return loc.getWorld().getMaxHeight();
        } else {
            return 255;
        }
    }

}
