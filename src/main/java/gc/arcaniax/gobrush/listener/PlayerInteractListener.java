package gc.arcaniax.gobrush.listener;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.world.block.BlockStateHolder;
import com.sk89q.worldedit.world.block.BlockTypes;
import gc.arcaniax.gobrush.Session;
import gc.arcaniax.gobrush.object.BrushPlayer;
import gc.arcaniax.gobrush.util.BrushPlayerUtil;
import gc.arcaniax.gobrush.util.GuiGenerator;
import gc.arcaniax.gobrush.util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.Random;

public class PlayerInteractListener implements Listener {
    private static final String PREFIX = "&bgoBrush> ";
    private static final String PERMISSION_BYPASS_WORLD = "gobrush.bypass.world";

    @EventHandler
    public void onClickEvent(final PlayerInteractEvent event) {
        final EditSession editsession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld(event.getPlayer().getWorld().getName())).build();
        if (!event.getPlayer().hasPermission("gobrush.use")) {
            return;
        }
        if (event.getPlayer().getInventory().getItemInMainHand().getType() == XMaterial.FLINT.parseMaterial() && (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))) {
            final Player player = event.getPlayer();
            final BrushPlayer brushPlayer = Session.getBrushPlayer(player.getUniqueId());
            if (Session.getConfig().getDisabledWorlds().contains(player.getWorld().getName()) && !player.hasPermission("gobrush.bypass.world")) {
                return;
            }
            if (!brushPlayer.isBrushEnabled()) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bgoBrush> &cYour brush is disabled, left click to enable the brush or type &f/gb toggle&c."));
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
            final Integer size = brushPlayer.getBrushSize();
            final Location start = player.getEyeLocation();
            final Vector v = start.getDirection().normalize();
            final double rot = (player.getLocation().getYaw() - 90.0f) % 360.0f + 360.0f;
            final double rotation = rot / 360.0 * 6.283185307179586;
            double rotPitch = player.getLocation().getPitch() % 360.0f;
            rotPitch += 360.0;
            final double rotationPitch = rotPitch / 360.0 * 6.283185307179586;
            final BrushPlayer brushPlayer2;
            final int n;
            int min;
            int max;
            Random r;
            double random;
            final Player player2;
            String cardinal;
            int x;
            int z;
            final Location location;
            Location loopLoc;
            final EditSession editSession;
            int worldHeight;
            double height;
            double subHeight;
            double n2;
            Location l;
            int y;
            int y2;
            int min2;
            int max2;
            final double n3;
            double xMov;
            double zMov;
            final double a;
            double yMod;
            double mod;
            double xMov2;
            double zMov2;
            double yMod2;
            Random r2;
            double random2;
            int x2;
            int z2;
            final Location location2;
            Location loopLoc2;
            Location blockLoc;
            double height2;
            double subHeight2;
            int y3;
            final Vector vector;
            Vector _v;
            Location place;
            Location place2;
            editsession.getPlayer().queueAction(() -> {
                if (!brushPlayer2.is3DMode()) {
                    min = n / 2 * -1;
                    max = n / 2;
                    r = new Random();
                    random = r.nextDouble();
                    cardinal = BrushPlayerUtil.getCardinalDirection(player2);
                    for (x = min; x <= max; ++x) {
                        for (z = min; z <= max; ++z) {
                            loopLoc = location.clone().add(x, 0.0, z);
                            worldHeight = editSession.getHighestTerrainBlock(loopLoc.getBlockX(), loopLoc.getBlockZ(), 0, 255);
                            if (editSession.getMask() == null || editSession.getMask().test(BlockVector3.at(loopLoc.getBlockX(), worldHeight, loopLoc.getBlockZ()))) {
                                height = BrushPlayerUtil.getHeight(player2, x - min, z - min, cardinal);
                                subHeight = height % 1.0;
                                if (random > 1.0 - subHeight) {
                                    n2 = ++height;
                                }
                                l = new Location(loopLoc.getWorld(), loopLoc.getBlockX(), worldHeight, loopLoc.getBlockZ());
                                if (brushPlayer2.isDirectionMode()) {
                                    for (y = 1; y < Math.floor(height); ++y) {
                                        if (brushPlayer2.isFlatMode()) {
                                            if (l.getBlockY() + y > location.getY()) {
                                                continue;
                                            }
                                        }
                                        try {
                                            editSession.setBlock(Vector3.at(l.getBlockX(), l.getBlockY() + y, l.getBlockZ()).toBlockPoint(), editSession.getBlock(Vector3.at(l.getBlockX(), l.getBlockY(), l.getBlockZ()).toBlockPoint()).toBaseBlock());
                                        } catch (Exception ex) {
                                        }
                                    }
                                } else {
                                    for (y2 = 0; y2 < Math.floor(height); ++y2) {
                                        if ((!brushPlayer2.isFlatMode() || l.getBlockY() - y2 > location.getY()) && (editSession.getMask() == null || editSession.getMask().test(Vector3.at(l.getBlockX(), l.getBlockY() - y2, l.getBlockZ()).toBlockPoint())) && l.getBlockY() - y2 >= 0) {
                                            try {
                                                editSession.setBlock(Vector3.at(l.getBlockX(), l.getBlockY() + y2, l.getBlockZ()).toBlockPoint(), BlockTypes.AIR.getDefaultState());
                                            } catch (Exception ex2) {
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    min2 = n / 2 * -1;
                    max2 = n / 2;
                    xMov = Math.cos(n3);
                    zMov = Math.sin(n3);
                    yMod = Math.cos(a);
                    mod = 0.25;
                    xMov2 = xMov * mod;
                    zMov2 = zMov * mod;
                    yMod2 = yMod * mod;
                    r2 = new Random();
                    random2 = r2.nextDouble();
                    for (x2 = min2; x2 <= max2; ++x2) {
                        for (z2 = min2; z2 <= max2; ++z2) {
                            loopLoc2 = location2.clone().add(zMov2 * x2, z2 * yMod2, -xMov2 * x2);
                            if (player2.getLocation().getPitch() < 0.0f) {
                                loopLoc2.add((1.0 - yMod2) * xMov2 * z2, 0.0, (1.0 - yMod2) * zMov2 * z2);
                            } else {
                                loopLoc2.add(-(1.0 - yMod2) * xMov2 * z2, 0.0, -(1.0 - yMod2) * zMov2 * z2);
                            }
                            blockLoc = BrushPlayerUtil.getClosest(player2, loopLoc2.clone(), location.clone(), n);
                            if (blockLoc != null && (editSession.getMask() == null || editSession.getMask().test(BlockVector3.at(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ())))) {
                                height2 = BrushPlayerUtil.getHeight(player2, x2 - min2, z2 - min2, "N");
                                subHeight2 = height2 % 1.0;
                                if (height2 == 255.0) {
                                    subHeight2 = 1.0;
                                }
                                if (random2 > 1.0 - subHeight2) {
                                    ++height2;
                                }
                                for (y3 = 0; y3 < height2; ++y3) {
                                    _v = vector.clone().multiply(-1).multiply(y3);
                                    if (brushPlayer2.isDirectionMode()) {
                                        if (!brushPlayer2.isFlatMode()) {
                                            try {
                                                editSession.setBlock(Vector3.at(blockLoc.getBlockX() + _v.getBlockX(), blockLoc.getBlockY() + _v.getBlockY(), blockLoc.getBlockZ() + _v.getBlockZ()).toBlockPoint(), editSession.getBlock(Vector3.at(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ()).toBlockPoint()));
                                            } catch (Exception ex3) {
                                            }
                                        } else {
                                            place = blockLoc.clone().add(vector.clone().multiply(-1).multiply(y3));
                                            if (place.distance(loopLoc2) > location.distance(location2)) {
                                                try {
                                                    editSession.setBlock(Vector3.at(blockLoc.getBlockX() + _v.getBlockX(), blockLoc.getBlockY() + _v.getBlockY(), blockLoc.getBlockZ() + _v.getBlockZ()).toBlockPoint(), editSession.getBlock(Vector3.at(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ()).toBlockPoint()));
                                                } catch (Exception ex4) {
                                                }
                                            }
                                        }
                                    } else if (!brushPlayer2.isFlatMode()) {
                                        try {
                                            editSession.setBlock(Vector3.at(blockLoc.getBlockX() + _v.getBlockX(), blockLoc.getBlockY() + _v.getBlockY(), blockLoc.getBlockZ() + _v.getBlockZ()).toBlockPoint(), BlockTypes.AIR.getDefaultState());
                                        } catch (Exception ex5) {
                                        }
                                    } else {
                                        place2 = blockLoc.clone().add(vector.clone().multiply(1).multiply(y3 - 1));
                                        if (place2.distance(loopLoc2) < location.distance(location2) - 1.0) {
                                            try {
                                                editSession.setBlock(Vector3.at(blockLoc.getBlockX() + _v.getBlockX(), blockLoc.getBlockY() + _v.getBlockY(), blockLoc.getBlockZ() + _v.getBlockZ()).toBlockPoint(), BlockTypes.AIR.getDefaultState());
                                            } catch (Exception ex6) {
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                editSession.flushQueue();
                editSession.getPlayer().getSession().remember(editSession);
            });
        } else if (event.getPlayer().getInventory().getItemInMainHand().getType() == XMaterial.FLINT.parseMaterial() && (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK))) {
            final Player player = event.getPlayer();
            event.setCancelled(true);
            player.openInventory(GuiGenerator.generateMainMenu(Session.getBrushPlayer(player.getUniqueId())));
        }
    }
}