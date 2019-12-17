package gc.arcaniax.gobrush.util;

import gc.arcaniax.gobrush.object.BrushPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains a utility used for generating the goBrush main menu
 * inventory.
 *
 * @author Arcaniax, McJeffr
 */
public class GuiGenerator {

    private static final String MAIN_MENU_INVENTORY_TITLE = "&1goBrush Menu";
    private static final ItemStack GRAY_GLASS_PANE = createItem(XMaterial.GRAY_STAINED_GLASS_PANE.parseMaterial(), (short) XMaterial.GRAY_STAINED_GLASS_PANE.data, "&6", "");
    private static final ItemStack GREEN_GLASS_PANE = createItem(XMaterial.GREEN_STAINED_GLASS_PANE.parseMaterial(), (short) XMaterial.GREEN_STAINED_GLASS_PANE.data, "&6", "");
    private static final ItemStack ORANGE_GLASS_PANE = createItem(XMaterial.ORANGE_STAINED_GLASS_PANE.parseMaterial(), (short) XMaterial.ORANGE_STAINED_GLASS_PANE.data, "&6", "");
    private static final ItemStack RED_GLASS_PANE = createItem(XMaterial.RED_STAINED_GLASS_PANE.parseMaterial(), (short) XMaterial.RED_STAINED_GLASS_PANE.data, "&6", "");
    private static final ItemStack WHITE_GLASS_PANE = createItem(XMaterial.WHITE_STAINED_GLASS_PANE.parseMaterial(), (short) XMaterial.WHITE_STAINED_GLASS_PANE.data, "&6", "");

    /**
     * This method generates the goBrush main menu based on the BrushPlayer's
     * settings.
     *
     * @param brushPlayer The BrushPlayer that this main menu belongs to.
     * @return The generated goBrush main menu inventory.
     */
    public static Inventory generateMainMenu(BrushPlayer brushPlayer) {
        Inventory mainMenu = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', MAIN_MENU_INVENTORY_TITLE));
        for (int i = 0; i < 27; i++) {
            mainMenu.setItem(i, GRAY_GLASS_PANE);
        }

        mainMenu.setItem(11, createItem(XMaterial.BROWN_MUSHROOM.parseMaterial(), (short) 0, "&6Size: &e" + brushPlayer.getBrushSize(), "&63D Size: &e" + (double) brushPlayer.getBrushSize() / 4.0 + "___&3___&7Left click to increase&3___&7Right click to decrease___&7Shift click to change by 10"));
        mainMenu.setItem(12, createItem(XMaterial.BLAZE_POWDER.parseMaterial(), (short) 0, "&6Intensity: &e" + brushPlayer.getBrushIntensity(), "&3___&7Left click to increase&3___&7Right click to decrease"));
        if (brushPlayer.getBrushSize() > brushPlayer.getMaxBrushSize()) {
            mainMenu.setItem(2, ORANGE_GLASS_PANE);
            mainMenu.setItem(20, ORANGE_GLASS_PANE);
        } else {
            mainMenu.setItem(2, WHITE_GLASS_PANE);
            mainMenu.setItem(20, WHITE_GLASS_PANE);
        }
        if (brushPlayer.getBrushIntensity() > brushPlayer.getMaxBrushIntensity()) {
            mainMenu.setItem(3, ORANGE_GLASS_PANE);
            mainMenu.setItem(21, ORANGE_GLASS_PANE);
        } else {
            mainMenu.setItem(3, WHITE_GLASS_PANE);
            mainMenu.setItem(21, WHITE_GLASS_PANE);
        }
        if (brushPlayer.isBrushEnabled()) {
            mainMenu.setItem(10, createItem(XMaterial.WRITABLE_BOOK.parseMaterial(), (short) 0, "&6Selected Brush: &e" + brushPlayer.getBrush().getName(), "&a&lEnabled___&7___&7Left click to change brush___&7Right click to toggle"));
            mainMenu.setItem(1, GREEN_GLASS_PANE);
            mainMenu.setItem(19, GREEN_GLASS_PANE);
        } else {
            mainMenu.setItem(10, createItem(XMaterial.WRITABLE_BOOK.parseMaterial(), (short) 0, "&6Selected Brush: &e" + brushPlayer.getBrush().getName(), "&c&lDisabled___&7___&7Left click to change brush___&7Right click to toggle"));
            mainMenu.setItem(1, RED_GLASS_PANE);
            mainMenu.setItem(19, RED_GLASS_PANE);
        }
        if (brushPlayer.isDirectionMode()) {
            mainMenu.setItem(13, headURL.create(headURL.upB64, "&6Pull Mode", "&7Click to change"));
            mainMenu.setItem(4, ORANGE_GLASS_PANE);
            mainMenu.setItem(22, ORANGE_GLASS_PANE);
        } else {
            mainMenu.setItem(13, headURL.create(headURL.downB64, "&6Push Mode", "&7Click to change"));
            mainMenu.setItem(4, ORANGE_GLASS_PANE);
            mainMenu.setItem(22, ORANGE_GLASS_PANE);
        }
        if (brushPlayer.is3DMode()) {
            mainMenu.setItem(14, headURL.create(headURL._3DB64, "&63D Mode", "&a&lEnabled___&7___&7Click to toggle"));
            mainMenu.setItem(5, GREEN_GLASS_PANE);
            mainMenu.setItem(23, GREEN_GLASS_PANE);
        } else {
            mainMenu.setItem(14, headURL.create(headURL._3DB64, "&63D Mode", "&c&lDisabled___&7___&7Click to toggle"));
            mainMenu.setItem(5, RED_GLASS_PANE);
            mainMenu.setItem(23, RED_GLASS_PANE);
        }
        if (brushPlayer.isFlatMode()) {
            mainMenu.setItem(15, createItem(XMaterial.HEAVY_WEIGHTED_PRESSURE_PLATE.parseMaterial(), (short) 0, "&6Flat Mode", "&a&lEnabled___&7___&7Click to toggle"));
            mainMenu.setItem(6, GREEN_GLASS_PANE);
            mainMenu.setItem(24, GREEN_GLASS_PANE);
        } else {
            mainMenu.setItem(15, createItem(XMaterial.HEAVY_WEIGHTED_PRESSURE_PLATE.parseMaterial(), (short) 0, "&6Flat Mode", "&c&lDisabled___&7___&7Click to toggle"));
            mainMenu.setItem(6, RED_GLASS_PANE);
            mainMenu.setItem(24, RED_GLASS_PANE);
        }

        if (brushPlayer.isAutoRotation()) {
            mainMenu.setItem(16, createItem(XMaterial.COMPASS.parseMaterial(), (short) 0, "&6Auto Rotation", "&a&lEnabled___&7___&7Click to toggle"));
            mainMenu.setItem(7, GREEN_GLASS_PANE);
            mainMenu.setItem(25, GREEN_GLASS_PANE);
        } else {
            mainMenu.setItem(16, createItem(XMaterial.COMPASS.parseMaterial(), (short) 0, "&6Auto Rotation", "&c&lDisabled___&7___&7Click to toggle"));
            mainMenu.setItem(7, RED_GLASS_PANE);
            mainMenu.setItem(25, RED_GLASS_PANE);
        }
        return mainMenu;
    }

    private static ItemStack createItem(Material material, short data, String name, String lore) {
        ItemStack is = new ItemStack(material);
        ItemMeta meta = is.getItemMeta();
        if (!lore.equals("")) {
            String[] loreListArray = lore.split("___");
            List<String> loreList = new ArrayList<String>();
            String[] arrayOfString1;
            int j = (arrayOfString1 = loreListArray).length;
            for (int i = 0; i < j; i++) {
                String s = arrayOfString1[i];
                loreList.add(ChatColor.translateAlternateColorCodes('&', s));
            }
            meta.setLore(loreList);
        }
        if (!name.equals("")) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        is.setItemMeta(meta);
        is.setDurability(data);
        return is;
    }
}
