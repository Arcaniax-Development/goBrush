package me.arcaniax.gobrush.listener;

import me.arcaniax.gobrush.Session;
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
