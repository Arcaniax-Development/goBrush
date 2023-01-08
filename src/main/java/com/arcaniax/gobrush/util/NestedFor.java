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

public class NestedFor {

    private final int lo;
    private final int hi;
    private final IAction action;
    public NestedFor(int lo, int hi, IAction action) {
        this.lo = lo;
        this.hi = hi;
        this.action = action;
    }

    public void nFor(int depth) {
        n_for(0, new int[0], depth);
    }

    private void n_for(int level, int[] indices, int maxLevel) {
        if (level == maxLevel) {
            action.act(indices);
        } else {
            int newLevel = level + 1;
            int[] newIndices = new int[newLevel];
            System.arraycopy(indices, 0, newIndices, 0, level);
            newIndices[level] = lo;

            while (newIndices[level] < hi) {
                n_for(newLevel, newIndices, maxLevel);
                ++newIndices[level];
            }

        }
    }

    public interface IAction {

        void act(int[] indices);

    }

}
