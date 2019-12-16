package gc.arcaniax.gobrush.listener;

import gc.arcaniax.gobrush.Session;
import gc.arcaniax.gobrush.enumeration.MainMenuSlot;
import gc.arcaniax.gobrush.object.Brush;
import gc.arcaniax.gobrush.object.BrushMenu;
import gc.arcaniax.gobrush.object.BrushPlayer;
import gc.arcaniax.gobrush.util.GuiGenerator;
import gc.arcaniax.gobrush.util.XMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener
        implements Listener {
    private static final String PERMISSION_BYPASS_MAXSIZE = "gobrush.bypass.maxsize";
    private static final String PERMISSION_BYPASS_MAXINTENSITY = "gobrush.bypass.maxintensity";
    private static final String MAIN_MENU_INVENTORY_TITLE = "goBrush Menu";
    private static final String BRUSH_MENU_INVENTORY_TITLE = "goBrush Brushes";

    @EventHandler(priority = EventPriority.NORMAL)
    public void mainMenuClickEvent(InventoryClickEvent event) {
        if (!isValidInventory(event, "goBrush Menu")) {
            return;
        }
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
                if (player.hasPermission("gobrush.bypass.maxintensity")) {
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
                if (player.hasPermission("gobrush.bypass.maxsize")) {
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
                if (player.hasPermission("gobrush.bypass.maxsize")) {
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
                player.openInventory(Session.getBrushMenu().getPage(0).getInventory());
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void brushMenuClickEvent(InventoryClickEvent event) {
        if (!isValidInventory(event, "goBrush Brushes")) {
            return;
        }
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
            case 45:
                if (event.getCurrentItem().getType().equals(XMaterial.ARROW.parseMaterial())) {
                    if (pageNumber == 0) {
                        player.openInventory(brushMenu.getPage(brushMenu.getAmountOfPages() - 1).getInventory());
                    } else {
                        player.openInventory(brushMenu.getPage(pageNumber - 1).getInventory());
                    }
                }
                break;
            case 49:
                openMenu(player);
                break;
            case 53:
                if (event.getCurrentItem().getType().equals(XMaterial.ARROW.parseMaterial())) {
                    if (pageNumber == brushMenu.getAmountOfPages() - 1) {
                        player.openInventory(brushMenu.getPage(0).getInventory());
                    } else {
                        player.openInventory(brushMenu.getPage(pageNumber + 1).getInventory());
                    }
                }
                break;
            default:
                if ((event.getCurrentItem() != null) &&
                        (event.getCurrentItem().getType().equals(XMaterial.MAP.parseMaterial()))) {
                    String name = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
                    int size = brushPlayer.getBrushSize();
                    Brush brush = Session.getBrush(name);
                    brushPlayer.setBrush(brush);
                    brushPlayer.getBrush().resize(size);
                    openMenu(player);
                }
                break;
        }
    }

    private boolean isValidInventory(InventoryClickEvent event, String inventoryName) {
        return event.getView().getTitle().contains(inventoryName);
    }

    private void openMenu(Player player) {
        player.openInventory(GuiGenerator.generateMainMenu(Session.getBrushPlayer(player.getUniqueId())));
    }
}
