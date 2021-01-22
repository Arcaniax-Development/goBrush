package com.arcaniax.gobrush.listener;

import com.arcaniax.gobrush.enumeration.MainMenuSlot;
import com.arcaniax.gobrush.util.XMaterial;
import com.arcaniax.gobrush.Session;
import com.arcaniax.gobrush.object.Brush;
import com.arcaniax.gobrush.object.BrushMenu;
import com.arcaniax.gobrush.object.BrushPlayer;
import com.arcaniax.gobrush.util.GuiGenerator;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

/**
 * This class contains the listener that gets fired when an inventory is
 * clicked. There are multiple EventHandlers in this listener object.
 *
 * @author McJeffr
 */
public class InventoryClickListener implements Listener {

    private static final String PERMISSION_BYPASS_MAXSIZE = "gobrush.bypass.maxsize";
    private static final String PERMISSION_BYPASS_MAXINTENSITY = "gobrush.bypass.maxintensity";
    private static final String MAIN_MENU_INVENTORY_TITLE = "goBrush Menu";
    private static final String BRUSH_MENU_INVENTORY_TITLE = "goBrush Brushes";
    private static int amountOfValidBrushes;

    @EventHandler(priority = EventPriority.NORMAL)
    public void mainMenuClickEvent(InventoryClickEvent event) {
        if (isInvalidInventory(event, MAIN_MENU_INVENTORY_TITLE)) return;
        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        BrushPlayer brushPlayer = Session.getBrushPlayer(player.getUniqueId());
        int rawSlot = event.getRawSlot();

        if (MainMenuSlot.MODE_DIRECTION.isValidSlot(rawSlot)) {
            brushPlayer.toggleDirectionMode();
            openMenu(player);
        } else if (MainMenuSlot.MODE_FLAT.isValidSlot(rawSlot)) {
            brushPlayer.toggleFlatMode();
            openMenu(player);
        } else if (MainMenuSlot.MODE_3D.isValidSlot(rawSlot)) {
            brushPlayer.toggle3DMode();
            openMenu(player);
        } else if (MainMenuSlot.FEATURE_AUTOROTATION.isValidSlot(rawSlot)) {
            brushPlayer.toggleAutoRotation();
            openMenu(player);
        } else if (MainMenuSlot.BRUSH_INTENSITY.isValidSlot(rawSlot)) {
            if (event.getClick() == ClickType.RIGHT) {
                int intensity = brushPlayer.getBrushIntensity() - 1;
                if (intensity > 0) {
                    brushPlayer.setBrushIntensity(intensity);
                }
            } else if (event.getClick() == ClickType.LEFT) {
                int intensity = brushPlayer.getBrushIntensity() + 1;
                if (player.hasPermission(PERMISSION_BYPASS_MAXINTENSITY)) {
                    brushPlayer.setBrushIntensity(intensity);
                } else if (intensity <= brushPlayer.getMaxBrushIntensity()) {
                    brushPlayer.setBrushIntensity(intensity);
                }
            }
            openMenu(player);
        } else if (MainMenuSlot.BRUSH_SIZE.isValidSlot(rawSlot)) {
            if (event.getClick() == ClickType.RIGHT) {
                int size = brushPlayer.getBrushSize() - 2;
                if (size > 0) {
                    brushPlayer.setBrushSize(size);
                }
            } else if (event.getClick() == ClickType.LEFT) {
                int size = brushPlayer.getBrushSize() + 2;
                if (player.hasPermission(PERMISSION_BYPASS_MAXSIZE)) {
                    brushPlayer.setBrushSize(size);
                    brushPlayer.getBrush().resize(size);
                } else if (size <= brushPlayer.getMaxBrushSize()) {
                    brushPlayer.setBrushSize(size);
                    brushPlayer.getBrush().resize(size);
                }
            } else if (event.getClick() == ClickType.SHIFT_RIGHT) {
                int size = brushPlayer.getBrushSize() - 10;
                if (size > 0) {
                    brushPlayer.setBrushSize(size);
                }
            } else if (event.getClick() == ClickType.SHIFT_LEFT) {
                int size = brushPlayer.getBrushSize() + 10;
                if (player.hasPermission(PERMISSION_BYPASS_MAXSIZE)) {
                    brushPlayer.setBrushSize(size);
                    brushPlayer.getBrush().resize(size);
                } else if (size <= brushPlayer.getMaxBrushSize()) {
                    brushPlayer.setBrushSize(size);
                    brushPlayer.getBrush().resize(size);
                }
            }

            openMenu(player);
        } else if (MainMenuSlot.BRUSH_SELECTOR.isValidSlot(rawSlot)) {
            if (event.getClick() == ClickType.RIGHT) {
                brushPlayer.toggleBrushEnabled();
                openMenu(player);
            } else if (event.getClick() == ClickType.LEFT) {
                player.closeInventory();
                amountOfValidBrushes = Session.initializeValidBrushes();
                if (amountOfValidBrushes == 0) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bgoBrush> &cWARNING! The automatic brush installation failed because the server cannot connect to GitHub."));
                    player.spigot().sendMessage(new ComponentBuilder("goBrush> ").color(ChatColor.AQUA)
                            .append("Click here to download the default brushes manually.").color(ChatColor.GOLD)
                            .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Arcaniax-Development/goBrush-Assets/blob/main/brushes.zip?raw=true")).create());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bgoBrush> &cExtract the zip into &e/plugins/goBrush/brushes"));
                } else {
                    player.openInventory(Session.getBrushMenu().getPage(0).getInventory());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void brushMenuClickEvent(InventoryClickEvent event) {
        if (isInvalidInventory(event, BRUSH_MENU_INVENTORY_TITLE)) return;
        event.setCancelled(true);

        if (event.isShiftClick()) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        BrushPlayer brushPlayer = Session.getBrushPlayer(player.getUniqueId());
        BrushMenu brushMenu = Session.getBrushMenu();
        int rawSlot = event.getRawSlot();
        int pageNumber = 0;

        for (int i = 0; i < Session.getBrushMenu().getAmountOfPages(); i++) {
            if (event.getInventory().equals(Session.getBrushMenu().getPage(i).getInventory())) {
                pageNumber = i;
            }
        }

        switch (rawSlot) {
            case (45): {
                if (event.getCurrentItem().getType().equals(XMaterial.ARROW.parseMaterial())) {
                    if (pageNumber == 0) {
                        player.openInventory(brushMenu.getPage(brushMenu.getAmountOfPages() - 1).getInventory());
                    } else {
                        player.openInventory(brushMenu.getPage(pageNumber - 1).getInventory());
                    }
                }

                break;
            }
            case (49): {
                openMenu(player);
                break;
            }
            case (53): {
                if (event.getCurrentItem().getType().equals(XMaterial.ARROW.parseMaterial())) {
                    if (pageNumber == brushMenu.getAmountOfPages() - 1) {
                        player.openInventory(brushMenu.getPage(0).getInventory());
                    } else {
                        player.openInventory(brushMenu.getPage(pageNumber + 1).getInventory());
                    }
                }
                break;
            }
            default: {
                if (event.getCurrentItem() != null) {
                    if (event.getCurrentItem().getType().equals(XMaterial.MAP.parseMaterial())) {
                        String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                        int size = brushPlayer.getBrushSize();
                        Brush brush = Session.getBrush(name);
                        brushPlayer.setBrush(brush);
                        brushPlayer.getBrush().resize(size);
                        openMenu(player);
                    }
                }
            }
        }
    }

    /**
     * This method checks if an InventoryClickEvent is happening in a valid
     * goBrush menu.
     *
     * @param event The InventoryClickEvent that needs to be checked.
     * @return True if the event is happening in a goBrush menu, false
     * otherwise.
     */
    private boolean isInvalidInventory(InventoryClickEvent event, String inventoryName) {
        final InventoryView view = event.getView();
        final String title = view.getTitle();
        if (!title.contains(inventoryName)) return true;

        event.setCancelled(true);

        final Inventory topInventory = view.getTopInventory();
        final Inventory clickedInventory = event.getClickedInventory();
        return topInventory != clickedInventory;
    }

    /**
     * This method opens up the goBrush menu inventory. This method can be used
     * to refresh the inventory upon a change in configuration.
     *
     * @param player The player that needs to open the inventory again.
     */
    private void openMenu(Player player) {
        player.openInventory(GuiGenerator.generateMainMenu(Session.getBrushPlayer(player.getUniqueId())));
    }
}
