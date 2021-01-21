package me.arcaniax.gobrush.listener;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitPlayer;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.world.block.BlockTypes;
import me.arcaniax.gobrush.Session;
import me.arcaniax.gobrush.object.BrushPlayer;
import me.arcaniax.gobrush.util.BrushPlayerUtil;
import me.arcaniax.gobrush.util.GuiGenerator;
import me.arcaniax.gobrush.util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.Random;

/**
 * This class contains the listener that gets fired when a player interacts with
 * the world.
 *
 * @author Arcaniax, McJeffr
 */
public class PlayerInteractListener implements Listener {

	private static final String PREFIX = "&bgoBrush> ";
	private static final String PERMISSION_BYPASS_WORLD = "gobrush.bypass.world";
    private static final String PERMISSION_USE = "gobrush.use";

	@EventHandler
	public void onClickEvent(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final boolean holdingFlint = player.getInventory().getItemInMainHand().getType() == Material.FLINT;

        if (!holdingFlint) return;
        if (!player.hasPermission(PERMISSION_USE)) return;
		if (event.getPlayer().getInventory().getItemInMainHand().getType() == XMaterial.FLINT.parseMaterial()
			&& ((event.getAction().equals(Action.RIGHT_CLICK_AIR))
				|| (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)))) {
			BrushPlayer brushPlayer = Session.getBrushPlayer(player.getUniqueId());
			if ((Session.getConfig().getDisabledWorlds().contains(player.getWorld().getName())) && (!player.hasPermission(PERMISSION_BYPASS_WORLD))) {
				return;
			}
			if (!(brushPlayer.isBrushEnabled())) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&',
																		  PREFIX + "&cYour brush is disabled, left click to enable the brush or type &f/gb toggle&c."));
				return;
			}
			Location loc;
			if (event.getAction().equals(Action.RIGHT_CLICK_AIR)) {
				loc = BrushPlayerUtil.getClosest(player);
			} else {
				loc = event.getClickedBlock().getLocation().clone();
			}
			if (loc == null) {
				return;
			}
			LocalSession localSession = WorldEdit.getInstance().getSessionManager().get(new BukkitPlayer(event.getPlayer()));
			try (EditSession editsession = localSession.createEditSession(new BukkitPlayer(event.getPlayer()))) {
			    try {
					editsession.setFastMode(false);
                    Integer size = brushPlayer.getBrushSize();
                    Location start = player.getEyeLocation();
                    org.bukkit.util.Vector v = start.getDirection().normalize();
                    double rot = (player.getLocation().getYaw() - 90.0F) % 360.0F + 360.0F;
                    final double rotation = (rot / 360.0F) * (2 * Math.PI);
                    double rotPitch = (player.getLocation().getPitch()) % 360.0F;
                    rotPitch += 360.0F;
                    final double rotationPitch = (rotPitch / 360.0F) * (2 * Math.PI);
                    if (!brushPlayer.is3DMode()) {
                        int min = size / 2 * -1;
                        int max = size / 2;
                        Random r = new Random();
                        double random = r.nextDouble();
                        String cardinal = BrushPlayerUtil.getCardinalDirection(player);
                        for (int x = min; x <= max; x++) {
                            for (int z = min; z <= max; z++) {
                                Location loopLoc = loc.clone().add(x, 0.0D, z);
                                int worldHeight = editsession.getHighestTerrainBlock(loopLoc.getBlockX(), loopLoc.getBlockZ(), 0, 255);
                                if (editsession.getMask() == null || editsession.getMask().test(BlockVector3.at(loopLoc.getBlockX(), worldHeight, loopLoc.getBlockZ()))) {
                                    double height = BrushPlayerUtil.getHeight(player, x - min, z - min, cardinal);
                                    double subHeight = height % 1.0D;
                                    if (random > 1.0 - subHeight) {
                                        height++;
                                    }
                                    Location l = new Location(loopLoc.getWorld(), loopLoc.getBlockX(), worldHeight, loopLoc.getBlockZ());
                                    if (brushPlayer.isDirectionMode()) {
                                        for (int y = 1; y < Math.floor(height); y++) {
                                            if ((!brushPlayer.isFlatMode()) || l.getBlockY() + y <= loc.getY()) {
                                                try {
                                                    editsession.setBlock(Vector3.at(l.getBlockX(), l.getBlockY() + y, l.getBlockZ()).toBlockPoint(), editsession.getBlock(Vector3.at(l.getBlockX(), l.getBlockY(), l.getBlockZ()).toBlockPoint()).toBaseBlock());
                                                } catch (Exception e) {
                                                }
                                            }
                                        }
                                    } else {
                                        for (int y = 0; y < Math.floor(height); y++) {
                                            if ((!brushPlayer.isFlatMode()) || l.getBlockY() - y > loc.getY()) {
                                                if (editsession.getMask() == null || editsession.getMask().test(Vector3.at(l.getBlockX(), l.getBlockY() - y, l.getBlockZ()).toBlockPoint())) {
                                                    if (!(l.getBlockY() - y < 0)) {
                                                        try {
                                                            editsession.setBlock(Vector3.at(l.getBlockX(), l.getBlockY() + y, l.getBlockZ()).toBlockPoint(), BlockTypes.AIR.getDefaultState());
                                                        } catch (Exception e) {
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else { //3D Mode
                        int min = size / 2 * -1;
                        int max = size / 2;
                        double xMov = Math.cos(rotation);
                        double zMov = Math.sin(rotation);
                        double yMod = Math.cos(rotationPitch);

                        double mod = 0.25;
                        xMov *= mod;
                        zMov *= mod;
                        yMod *= mod;

                        Random r = new Random();
                        double random = r.nextDouble();
                        for (int x = min; x <= max; x++) {
                            for (int z = min; z <= max; z++) {
                                Location loopLoc = start.clone().add(zMov * x, z * yMod, -xMov * x);
                                if (player.getLocation().getPitch() < 0) {
                                    loopLoc.add((1 - yMod) * xMov * z, 0, (1 - yMod) * zMov * z);
                                } else {
                                    loopLoc.add(-(1 - yMod) * xMov * z, 0, -(1 - yMod) * zMov * z);
                                }
                                Location blockLoc = BrushPlayerUtil.getClosest(player, loopLoc.clone(), loc.clone(), size);
                                if (blockLoc != null && (editsession.getMask() == null || editsession.getMask().test(BlockVector3.at(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ())))) {
                                    double height = BrushPlayerUtil.getHeight(player, x - min, z - min, "N");
                                    double subHeight = height % 1.0D;
                                    if (height == 255.0) {
                                        subHeight = 1.0;
                                    }
                                    if (random > 1.0 - subHeight) {
                                        height++;
                                    }
                                    for (
                                            int y = 0;
                                            y < height; y++) {
                                        Vector _v = v.clone().multiply(-1).multiply(y);
                                        if (brushPlayer.isDirectionMode()) {
                                            if (!brushPlayer.isFlatMode()) {
                                                try {
                                                    editsession.setBlock(Vector3.at(blockLoc.getBlockX() + _v.getBlockX(), blockLoc.getBlockY() + _v.getBlockY(), blockLoc.getBlockZ() + _v.getBlockZ()).toBlockPoint(), editsession.getBlock(Vector3.at(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ()).toBlockPoint()));
                                                } catch (Exception e) {
                                                }
                                            } else {
                                                Location place = blockLoc.clone().add(v.clone().multiply(-1).multiply(y));
                                                if (place.distance(loopLoc) > loc.distance(start)) {
                                                    try {
                                                        editsession.setBlock(Vector3.at(blockLoc.getBlockX() + _v.getBlockX(), blockLoc.getBlockY() + _v.getBlockY(), blockLoc.getBlockZ() + _v.getBlockZ()).toBlockPoint(), editsession.getBlock(Vector3.at(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ()).toBlockPoint()));
                                                    } catch (Exception e) {
                                                    }
                                                }
                                            }
                                        } else {
                                            if (!brushPlayer.isFlatMode()) {
                                                try {
                                                    editsession.setBlock(Vector3.at(blockLoc.getBlockX() + _v.getBlockX(), blockLoc.getBlockY() + _v.getBlockY(), blockLoc.getBlockZ() + _v.getBlockZ()).toBlockPoint(), BlockTypes.AIR.getDefaultState());
                                                } catch (Exception e) {
                                                }
                                            } else {
                                                Location place = blockLoc.clone().add(v.clone().multiply(1).multiply(y - 1));
                                                if (place.distance(loopLoc) < loc.distance(start) - 1) {
                                                    try {
                                                        editsession.setBlock(Vector3.at(blockLoc.getBlockX() + _v.getBlockX(), blockLoc.getBlockY() + _v.getBlockY(), blockLoc.getBlockZ() + _v.getBlockZ()).toBlockPoint(), BlockTypes.AIR.getDefaultState());
                                                    } catch (Exception e) {
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } finally {
                    localSession.remember(editsession);
                }
            }
		} else if ((event.getPlayer().getInventory().getItemInMainHand().getType() == XMaterial.FLINT.parseMaterial())
				   && ((event.getAction().equals(Action.LEFT_CLICK_AIR))
					   || (event.getAction().equals(Action.LEFT_CLICK_BLOCK)))) {
			event.setCancelled(true);
			player.openInventory(GuiGenerator.generateMainMenu(Session.getBrushPlayer(player.getUniqueId())));
		}
	}

}
