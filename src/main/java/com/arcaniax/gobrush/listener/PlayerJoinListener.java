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
 *                            Copyright (C) 2021 Arcaniax
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
package com.arcaniax.gobrush.listener;

import com.arcaniax.gobrush.Session;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

/**
 * This class contains the listener that gets fired upon player join. This
 * listener is used to set the default settings of the goBrush plugin of that
 * player.
 *
 * @author McJeffr
 */
public class PlayerJoinListener implements Listener {

    /**
     * The event handler for the AsyncPlayerPreLoginEvent. This event adds the player
     * that joined to the list of BrushPlayers.
     *
     * @param event The event that is fired upon a AsyncPlayerPreLoginEvent.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoinEvent(AsyncPlayerPreLoginEvent event) {
        Session.addBrushPlayer(event.getUniqueId());
    }
}
