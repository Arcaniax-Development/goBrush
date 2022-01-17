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
 *                            Copyright (C) 2022 Arcaniax
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
package com.arcaniax.gobrush.command;

import com.arcaniax.gobrush.GoBrushPlugin;
import com.arcaniax.gobrush.Session;
import com.arcaniax.gobrush.object.Brush;
import com.arcaniax.gobrush.object.BrushPlayer;
import com.arcaniax.gobrush.object.HeightMapExporter;
import com.sk89q.worldedit.IncompleteRegionException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class CommandHandler implements CommandExecutor {

    private static final String prefix = "&bgoBrush> ";

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gobrush") || cmd.getName().equalsIgnoreCase("gb")) {
            if (!(sender instanceof Player)) {
                return false;
            }
            final Player p = (Player) sender;
            BrushPlayer bp = Session.getBrushPlayer(p.getUniqueId());
            if (!p.hasPermission("gobrush.use")) {
                p.sendMessage(ChatColor.translateAlternateColorCodes(
                        '&',
                        prefix + "&cYou are lacking the permission gobrush.use"
                ));
                return true;
            }
            if (args.length == 0) {
                if (p.hasPermission("gobrush.admin")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes(
                            '&',
                            prefix + "&c/gb size&7|&cintensity&7|&cbrush&7|&ctoggle&7|&creload&7|&cexport&7|&cinfo "
                    ));
                    return true;
                } else if (p.hasPermission("gobrush.export")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes(
                            '&',
                            prefix + "&c/gb size&7|&cintensity&7|&cbrush&7|&ctoggle&7|&cexport&7|&cinfo "
                    ));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes(
                        '&',
                        prefix + "&c/gb size&7|&cintensity&7|&cbrush&7|&ctoggle&7|&cinfo "
                ));
                return true;
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("size") || args[0].equalsIgnoreCase("s")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&c/gb size [number]"));
                    return true;
                } else if (args[0].equalsIgnoreCase("intensity") || args[0].equalsIgnoreCase("i")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&c/gb intensity [number]"));
                    return true;
                } else if (args[0].equalsIgnoreCase("brush") || args[0].equalsIgnoreCase("b")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&c/gb brush [fileName]"));
                    return true;
                } else if ((args[0].equalsIgnoreCase("export") || args[0].equalsIgnoreCase("e")) && p.hasPermission(
                        "gobrush.export")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&c/gb export [fileName]"));
                    return true;
                } else if (args[0].equalsIgnoreCase("toggle") || args[0].equalsIgnoreCase("t")) {

                    if (bp.isBrushEnabled()) {
                        bp.toggleBrushEnabled();
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&cDisabled brush"));
                    } else {
                        bp.toggleBrushEnabled();
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aEnabled brush"));
                    }
                    return true;
                } else if ((args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) && p.hasPermission(
                        "gobrush.admin")) {
                    GoBrushPlugin.getPlugin().reloadConfig();
                    Session.getConfig().reload(GoBrushPlugin.getPlugin().getConfig());
                    int amountOfValidBrushes = Session.initializeValidBrushes();
                    GoBrushPlugin.getPlugin().getLogger().log(Level.INFO, "Registered {0} brushes.", amountOfValidBrushes);
                    Session.initializeBrushMenu();
                    Session.initializeBrushPlayers();
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&aReload Successful"));
                    return true;
                } else if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("i")) {

                    p.spigot().sendMessage(new ComponentBuilder("goBrush> ").color(ChatColor.AQUA)
                            .append("Created by: ").color(ChatColor.GOLD)
                            .append("Arcaniax").color(ChatColor.YELLOW)
                            .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitter.com/Arcaniax")).create());

                    p.spigot().sendMessage(new ComponentBuilder("goBrush> ").color(ChatColor.AQUA)
                            .append("Sponsored by: ").color(ChatColor.GOLD)
                            .append("@goCreativeMC").color(ChatColor.YELLOW)
                            .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://twitter.com/goCreativeMC")).create());

                    p.sendMessage(ChatColor.translateAlternateColorCodes(
                            '&',
                            prefix + "&6Plugin download: &ehttps://www.spigotmc.org/resources/23118/"
                    ));

                    p.spigot().sendMessage(new ComponentBuilder("goBrush> ").color(ChatColor.AQUA)
                            .append("More brushes: ").color(ChatColor.GOLD)
                            .append("Click here").color(ChatColor.YELLOW)
                            .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://gumroad.com/aerios#JAtxa")).create());

                    return true;
                }
                if (p.hasPermission("gobrush.admin")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes(
                            '&',
                            prefix + "&c/gb size&7|&cintensity&7|&cbrush&7|&ctoggle&7|&creload&7|&cexport&7|&cinfo "
                    ));
                    return true;
                } else if (p.hasPermission("gobrush.export")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes(
                            '&',
                            prefix + "&c/gb size&7|&cintensity&7|&cbrush&7|&ctoggle&7|&cexport&7|&cinfo "
                    ));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes(
                        '&',
                        prefix + "&c/gb size&7|&cintensity&7|&cbrush&7|&ctoggle&7|&cinfo "
                ));
                return true;
            } else if (args.length == 2) {
                if (args[0].equalsIgnoreCase("size") || args[0].equalsIgnoreCase("s")) {
                    try {
                        int sizeAmount = Integer.parseInt(args[1]);
                        if (sizeAmount > bp.getMaxBrushSize() && !p.hasPermission("gobrush.bypass.maxsize")) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes(
                                    '&',
                                    prefix + "&6The maximum size is &e" + bp.getMaxBrushSize()
                            ));
                            sizeAmount = bp.getMaxBrushSize();
                        } else if (sizeAmount < 5) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&6The minimum size is &e5"));
                            sizeAmount = 5;
                        } else if (sizeAmount % 2 == 0) {
                            sizeAmount++;
                        }
                        bp.setBrushSize(sizeAmount);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&6Size set to: &e" + sizeAmount));
                        bp.getBrush().resize(sizeAmount);

                        return true;
                    } catch (Exception e) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&c/gb size [number]"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("intensity") || args[0].equalsIgnoreCase("i")) {
                    try {
                        int intensityAmount = Integer.parseInt(args[1]);
                        if (intensityAmount > bp.getMaxBrushIntensity() && !p.hasPermission("gobrush.bypass.maxintensity")) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes(
                                    '&',
                                    prefix + "&6The maximum intensity is &e" + bp.getBrushIntensity()
                            ));
                            intensityAmount = bp.getMaxBrushIntensity();
                        } else if (intensityAmount < 1) {
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&6The minimum intensity is &e1"));
                            intensityAmount = 1;
                        }
                        bp.setBrushIntensity(intensityAmount);
                        p.sendMessage(ChatColor.translateAlternateColorCodes(
                                '&',
                                prefix + "&6Intensity set to: &e" + intensityAmount
                        ));
                        return true;
                    } catch (Exception e) {
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&c/gb intensity [number]"));
                        return true;
                    }
                } else if (args[0].equalsIgnoreCase("brush") || args[0].equalsIgnoreCase("b")) {
                    String name = args[1].replace("_", " ");
                    if (Session.containsBrush(name)) {
                        int size = bp.getBrushSize();
                        Brush brush = Session.getBrush(name);
                        bp.setBrush(brush);
                        bp.getBrush().resize(size);
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&6Brush set to: &e" + name));
                        return true;
                    } else {
                        p.sendMessage(ChatColor.translateAlternateColorCodes(
                                '&',
                                prefix + "&cCould not load brush \"" + name + "\""
                        ));
                        return true;
                    }
                } else if ((args[0].equalsIgnoreCase("export") || args[0].equalsIgnoreCase("e")) && p.hasPermission(
                        "gobrush.export")) {
                    final String name = args[1];
                    Bukkit.getScheduler().runTaskAsynchronously(GoBrushPlugin.plugin, new Runnable() {
                        @Override
                        public void run() {
                            HeightMapExporter hm;
                            try {
                                hm = new HeightMapExporter(p);
                            } catch (IncompleteRegionException e) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes(
                                        '&',
                                        prefix + "&cPlease make a WorldEdit selection &6(//wand)"
                                ));
                                return;
                            }
                            if (!hm.hasWorldEditSelection()) {
                                p.sendMessage(ChatColor.translateAlternateColorCodes(
                                        '&',
                                        prefix + "&cPlease make a WorldEdit selection &8(//wand)"
                                ));
                                return;
                            }
                            hm.exportImage(500, name);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + "&6Exported &e" + name + ".png"));
                            Session.initializeValidBrushes();
                            Session.initializeBrushMenu();

                        }
                    });
                    return true;
                }
                if (p.hasPermission("gobrush.admin")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes(
                            '&',
                            prefix + "&c/gb size&7|&cintensity&7|&cbrush&7|&ctoggle&7|&creload&7|&cexport&7|&cinfo "
                    ));
                    return true;
                } else if (p.hasPermission("gobrush.export")) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes(
                            '&',
                            prefix + "&c/gb size&7|&cintensity&7|&cbrush&7|&ctoggle&7|&cexport&7|&cinfo "
                    ));
                    return true;
                }
                p.sendMessage(ChatColor.translateAlternateColorCodes(
                        '&',
                        prefix + "&c/gb size&7|&cintensity&7|&cbrush&7|&ctoggle&7|&cinfo "
                ));
                return true;
            }
        }
        return false;
    }

}
