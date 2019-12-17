package gc.arcaniax.gobrush.listener;

import gc.arcaniax.gobrush.Session;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * This class contains the listener that gets fired upon player join. This
 * listener is used to set the default settings of the goBrush plugin of that
 * player.
 *
 * @author McJeffr
 */
public class PlayerJoinListener implements Listener {

    /**
     * The event handler for the PlayerJoinEvent. This event adds the player
     * that joined to the list of BrushPlayers.
     *
     * @param event The event that is fired upon a PlayerJoinEvent.
     */
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Session.addBrushPlayer(event.getPlayer().getUniqueId());
    }
}