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
package com.arcaniax.gobrush.listener;

import com.arcaniax.gobrush.Session;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * This class contains the EventHandler for the PlayerQuitEvent. This Listener
 * is used to remove stored data of players when they leave the server, to clear
 * up some RAM and general performance of the plugin.
 *
 * @author McJeffr
 */
public class PlayerQuitListener implements Listener {

    /**
     * The event handler for the PlayerQuitEvent. This event removes the player
     * that left from the list of BrushPlayers and clears their history from the
     * list of history.
     *
     * @param event The event that is fired upon a PlayerJoinEvent.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Session.removeBrushPlayer(event.getPlayer().getUniqueId());
    }

}
