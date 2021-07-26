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

import com.arcaniax.gobrush.util.NestedFor;
import com.arcaniax.gobrush.util.XMaterial;

import com.arcaniax.gobrush.Session;
import com.arcaniax.gobrush.object.BrushPlayer;
import com.arcaniax.gobrush.util.BrushPlayerUtil;
import com.arcaniax.gobrush.util.GuiGenerator;
import com.fastasyncworldedit.core.Fawe;
import com.fastasyncworldedit.core.queue.implementation.QueueHandler;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.world.block.BlockState;
import com.sk89q.worldedit.world.block.BlockTypes;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

import java.util.HashMap;
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
            LocalSession localSession = WorldEdit.getInstance().getSessionManager().get(BukkitAdapter.adapt(event.getPlayer()));
            QueueHandler queue = Fawe.get().getQueueHandler();
            queue.async(() -> {
                synchronized (localSession) {
                    EditSession editsession = localSession.createEditSession(BukkitAdapter.adapt(event.getPlayer()));
                    try {
                        HashMap<Vector3, BlockState> blocksToSet = new HashMap<>();
                        try {
                            editsession.setFastMode(false);
                            int size = brushPlayer.getBrushSize();
                            Location start = player.getEyeLocation();
                            Vector v = start.getDirection().normalize();
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
                                                            blocksToSet.put(Vector3.at(l.getBlockX(), l.getBlockY() + y, l.getBlockZ()), editsession.getBlock(Vector3.at(l.getBlockX(), l.getBlockY(), l.getBlockZ()).toBlockPoint()));
                                                        } catch (Exception ignored) {
                                                        }
                                                    }
                                                }
                                            } else {
                                                for (int y = 0; y < Math.floor(height); y++) {
                                                    if ((!brushPlayer.isFlatMode()) || l.getBlockY() - y > loc.getY()) {
                                                        if (editsession.getMask() == null || editsession.getMask().test(Vector3.at(l.getBlockX(), l.getBlockY() - y, l.getBlockZ()).toBlockPoint())) {
                                                            if (!(l.getBlockY() - y < 0)) {
                                                                try {
                                                                    blocksToSet.put(Vector3.at(l.getBlockX(), l.getBlockY() + y, l.getBlockZ()), BlockTypes.AIR.getDefaultState());
                                                                } catch (Exception ignored) {
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                                for (Vector3 block : blocksToSet.keySet()) {
                                    editsession.setBlock(block.toBlockPoint().getBlockX(), block.toBlockPoint().getBlockY(), block.toBlockPoint().getBlockZ(), blocksToSet.get(block));
                                }
                                blocksToSet.clear();
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


                                double finalZMov = zMov;
                                double finalYMod = yMod;
                                double finalXMov = xMov;
                                NestedFor.IAction threeDimensionModeXZLoops = indices -> {

                                    Location loopLoc = start.clone().add(finalZMov * indices[0], indices[1] * finalYMod, -finalXMov * indices[0]);

                                    if (player.getLocation().getPitch() < 0) {
                                        loopLoc.add((1 - finalYMod) * finalXMov * indices[1], 0, (1 - finalYMod) * finalZMov * indices[1]);
                                    } else {
                                        loopLoc.add(-(1 - finalYMod) * finalXMov * indices[1], 0, -(1 - finalYMod) * finalZMov * indices[1]);
                                    }

                                    Location blockLoc = BrushPlayerUtil.getClosest(player, loopLoc.clone(), loc.clone(), size, editsession);

                                    if (blockLoc != null && (editsession.getMask() == null || editsession.getMask().test(BlockVector3.at(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ())))) {
                                        double height = BrushPlayerUtil.getHeight(player, indices[0] - min, indices[1] - min, "N");
                                        double subHeight = height % 1.0D;
                                        if (height == 255.0) {
                                            subHeight = 1.0;
                                        }
                                        if (random > 1.0 - subHeight) {
                                            height++;
                                        }
                                        for (int y = 0; y < height; y++) {
                                            Vector _v = v.clone().multiply(-1).multiply(y);
                                            if (brushPlayer.isDirectionMode()) {
                                                if (!brushPlayer.isFlatMode()) {
                                                    try {
                                                        blocksToSet.put(Vector3.at(blockLoc.getBlockX() + _v.getBlockX(), blockLoc.getBlockY() + _v.getBlockY(), blockLoc.getBlockZ() + _v.getBlockZ()), editsession.getBlock(Vector3.at(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ()).toBlockPoint()));
                                                    } catch (Exception ignored) {
                                                    }
                                                } else {
                                                    Location place = blockLoc.clone().add(v.clone().multiply(-1).multiply(y));
                                                    if (place.distance(loopLoc) > loc.distance(start)) {
                                                        try {
                                                            blocksToSet.put(Vector3.at(blockLoc.getBlockX() + _v.getBlockX(), blockLoc.getBlockY() + _v.getBlockY(), blockLoc.getBlockZ() + _v.getBlockZ()), editsession.getBlock(Vector3.at(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ()).toBlockPoint()));
                                                        } catch (Exception ignored) {
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (!brushPlayer.isFlatMode()) {
                                                    try {
                                                        blocksToSet.put(Vector3.at(blockLoc.getBlockX() + _v.getBlockX(), blockLoc.getBlockY() + _v.getBlockY(), blockLoc.getBlockZ() + _v.getBlockZ()), editsession.getBlock(Vector3.at(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ()).toBlockPoint()));
                                                    } catch (Exception ignored) {
                                                    }
                                                } else {
                                                    Location place = blockLoc.clone().add(v.clone().multiply(1).multiply(y - 1));
                                                    if (place.distance(loopLoc) < loc.distance(start) - 1) {
                                                        try {
                                                            blocksToSet.put(Vector3.at(blockLoc.getBlockX() + _v.getBlockX(), blockLoc.getBlockY() + _v.getBlockY(), blockLoc.getBlockZ() + _v.getBlockZ()), editsession.getBlock(Vector3.at(blockLoc.getBlockX(), blockLoc.getBlockY(), blockLoc.getBlockZ()).toBlockPoint()));
                                                        } catch (Exception ignored) {
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                };
                                NestedFor nf = new NestedFor(min - 1, max, threeDimensionModeXZLoops);
                                nf.nFor(2);

                                for (Vector3 block : blocksToSet.keySet()) {
                                    if (brushPlayer.isDirectionMode()) {
                                        editsession.setBlock(block.toBlockPoint().getBlockX(), block.toBlockPoint().getBlockY(), block.toBlockPoint().getBlockZ(), blocksToSet.get(block));
                                    } else {
                                        editsession.setBlock(block.toBlockPoint().getBlockX(), block.toBlockPoint().getBlockY(), block.toBlockPoint().getBlockZ(), BlockTypes.AIR.getDefaultState());
                                    }
                                }
                                blocksToSet.clear();
                            }
                        } finally {

                            editsession.commit();
                        }
                    } finally {
                        localSession.remember(editsession);
                    }
                }
            });
        } else if ((event.getPlayer().getInventory().getItemInMainHand().getType() == XMaterial.FLINT.parseMaterial())
                && ((event.getAction().equals(Action.LEFT_CLICK_AIR))
                || (event.getAction().equals(Action.LEFT_CLICK_BLOCK)))) {
            event.setCancelled(true);
            player.openInventory(GuiGenerator.generateMainMenu(Session.getBrushPlayer(player.getUniqueId())));
        }
    }


}
