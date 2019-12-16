package gc.arcaniax.gobrush.listener;

import gc.arcaniax.gobrush.Session;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener
        implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Session.removeBrushPlayer(event.getPlayer().getUniqueId());
    }
}
