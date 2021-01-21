package me.arcaniax.gobrush.object;

import me.arcaniax.gobrush.GoBrushPlugin;
import me.arcaniax.gobrush.Session;
import me.arcaniax.gobrush.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class contains the object of BrushPage. This object resembles one page
 * in the BrushMenu.
 *
 * @author McJeffr
 */
public class BrushPage {
    /* Attributes */
    private static final String BRUSH_MENU_INVENTORY_TITLE = "&1goBrush Brushes";
    private static final ItemStack GRAY_GLASS_PANE = createItem(XMaterial.GRAY_STAINED_GLASS_PANE.parseMaterial(), (short) XMaterial.GRAY_STAINED_GLASS_PANE.data, "&6", "");
    private static final ItemStack EXIT = createItem(XMaterial.BARRIER.parseMaterial(), (short) 0, "&cBack to main menu", "");
    private static final ItemStack PREVIOUS_PAGE = createItem(XMaterial.ARROW.parseMaterial(), (short) 0, "&6Previous page", "");
    private static final ItemStack NEXT_PAGE = createItem(XMaterial.ARROW.parseMaterial(), (short) 0, "&6Next page", "");
    private final Inventory INVENTORY;
    private final int PAGE_NUMBER;

    /**
     * Constructor of a BrushPage object. This constructor accepts a list
     * containing a maximum of 45 brushes and a page number that indicates the
     * page this BrushPage is in the BrushMenu. This constructor should not be
     * used, use the one of BrushMenu instead if you want to create an inventory
     * of brushes.
     *
     * @param brushes    A List containing a maximum of 45 brushes.
     * @param pageNumber The page number of this BrushPage.
     */
    @SuppressWarnings("unchecked")
    public BrushPage(List<Brush> brushes, int pageNumber, int pageCount) {
        this.PAGE_NUMBER = pageNumber;
        this.INVENTORY = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&',
                BRUSH_MENU_INVENTORY_TITLE + "&8 | &5Page " + (pageNumber + 1)));

        for (int i = 0; i < 54; i++) {
            INVENTORY.setItem(i, GRAY_GLASS_PANE);
        }
        if (pageNumber > 0) {
            INVENTORY.setItem(45, PREVIOUS_PAGE);
        }
        INVENTORY.setItem(49, EXIT);
        if (pageNumber + 1 != pageCount) {
            INVENTORY.setItem(53, NEXT_PAGE);
        }
        Collections.sort(brushes);
        for (int i = 0; i < brushes.size(); i++) {
            INVENTORY.setItem(i, createItem(XMaterial.MAP.parseMaterial(), (short) 0, "&e" + brushes.get(i).getName(), getImageLore(getBrush(brushes.get(i).getName()))));
        }
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

    private static ItemStack createItem(Material material, short data, String name, List<String> lore) {
        ItemStack is = new ItemStack(material);
        ItemMeta meta = is.getItemMeta();
        meta.setLore(lore);
        if (!name.equals("")) {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        is.setItemMeta(meta);
        is.setDurability(data);
        return is;
    }

    private List<String> getImageLore(BufferedImage img) {
        List<String> loreList = new ArrayList<String>();
        loreList.add("\u00A70");
        for (int z = 0; z < Session.getConfig().getImgLoreSize(); z++) {
            String s = "";
            for (int x = 0; x < Session.getConfig().getImgLoreSize(); x++) {
                s += getChatColor(getGrayScale(img, x, z));
                s += "\u2588";
            }
            loreList.add(s);
        }
        return loreList;
    }

    private String getChatColor(float grayScale) {
        if (grayScale <= 5) {
            return "\u00A70\u00A7n";
        } else if (grayScale <= 30) {
            return "\u00A78\u00A7n";
        } else if (grayScale <= 70) {
            return "\u00A77\u00A7n";
        } else {
            return "\u00A7f\u00A7n";
        }
    }

    private BufferedImage cropImage(BufferedImage src, int size) {
        BufferedImage resizedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(src, 0, 0, size, size, null);
        g.dispose();
        return resizedImage;
    }

    private BufferedImage getBrush(String s) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(GoBrushPlugin.getPlugin().getDataFolder() + "/brushes/" + s));
        } catch (Exception e) {
        }
        img = cropImage(img, Session.getConfig().getImgLoreSize());
        return img;
    }

    private float getGrayScale(BufferedImage img, int x, int z) {

        int size = img.getHeight();

        int rgb = 0;
        if (z < size && x < size && x >= 0 && z >= 0) {
            rgb = img.getRGB(x, z);
        }

        int red = (rgb >>> 16) & 0xFF;
        int green = (rgb >>> 8) & 0xFF;
        int blue = (rgb & 0xFF);

        if (red == 0 && green == 0 && blue == 0) {
            return 0;
        }
        return ((red + blue + green) / (float) 3) / (float) 2.55;
    }

    /**
     * Getter for the Inventory object of this BrushPage.
     *
     * @return The Inventory object of this BrushPage.
     */
    public Inventory getInventory() {
        return INVENTORY;
    }

    /**
     * Getter for the page number of this BrushPage.
     *
     * @return The page number of this BrushPage.
     */
    public int getPageNumber() {
        return PAGE_NUMBER;
    }
}
