package gc.arcaniax.gobrush.listener;

import gc.arcaniax.gobrush.Session;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener
        implements Listener {
    @EventHandler(priority = EventPriority.NORMAL)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Session.addBrushPlayer(event.getPlayer().getUniqueId());
    }
}
