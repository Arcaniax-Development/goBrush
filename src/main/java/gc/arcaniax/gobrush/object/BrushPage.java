package gc.arcaniax.gobrush.object;

import gc.arcaniax.gobrush.Main;
import gc.arcaniax.gobrush.Session;
import gc.arcaniax.gobrush.util.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BrushPage {
    private static final String BRUSH_MENU_INVENTORY_TITLE = "&1goBrush Brushes";
    private static final ItemStack GRAY_GLASS_PANE = createItem(XMaterial.GRAY_STAINED_GLASS_PANE.parseMaterial(), (short) XMaterial.GRAY_STAINED_GLASS_PANE.data, "&6", "");
    private static final ItemStack EXIT = createItem(XMaterial.BARRIER.parseMaterial(), (short) 0, "&cBack to main menu", "");
    private static final ItemStack PREVIOUS_PAGE = createItem(XMaterial.ARROW.parseMaterial(), (short) 0, "&6Previous page", "");
    private static final ItemStack NEXT_PAGE = createItem(XMaterial.ARROW.parseMaterial(), (short) 0, "&6Next page", "");
    private final Inventory INVENTORY;
    private final int PAGE_NUMBER;

    public BrushPage(List<Brush> brushes, int pageNumber, int pageCount) {
        this.PAGE_NUMBER = pageNumber;
        this.INVENTORY = Bukkit.createInventory(null, 54, ChatColor.translateAlternateColorCodes('&', "&1goBrush Brushes&8 | &5Page " + (pageNumber + 1)));
        for (int i = 0; i < 54; i++) {
            this.INVENTORY.setItem(i, GRAY_GLASS_PANE);
        }
        if (pageNumber > 0) {
            this.INVENTORY.setItem(45, PREVIOUS_PAGE);
        }
        this.INVENTORY.setItem(49, EXIT);
        if (pageNumber + 1 != pageCount) {
            this.INVENTORY.setItem(53, NEXT_PAGE);
        }
        Collections.sort(brushes);
        for (int i = 0; i < brushes.size(); i++) {
            this.INVENTORY.setItem(i, createItem(XMaterial.MAP.parseMaterial(), (short) 0, "&e" + brushes.get(i).getName(), getImageLore(getBrush(brushes.get(i).getName()))));
        }
    }

    private static ItemStack createItem(Material material, short data, String name, String lore) {
        ItemStack is = new ItemStack(material);
        ItemMeta meta = is.getItemMeta();
        if (!lore.equals("")) {
            String[] loreListArray = lore.split("___");
            List<String> loreList = new ArrayList();
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
        List<String> loreList = new ArrayList();
        loreList.add("§0");
        for (int z = 0; z < Session.getConfig().getImgLoreSize(); z++) {
            String s = "";
            for (int x = 0; x < Session.getConfig().getImgLoreSize(); x++) {
                s = s + getChatColor(getGrayScale(img, x, z));
                s = s + "█";
            }
            loreList.add(s);
        }
        return loreList;
    }

    private String getChatColor(float grayScale) {
        if (grayScale <= 5.0F) {
            return "§0§n";
        }
        if (grayScale <= 30.0F) {
            return "§8§n";
        }
        if (grayScale <= 70.0F) {
            return "§7§n";
        }
        return "§f§n";
    }

    private BufferedImage cropImage(BufferedImage src, int size) {
        BufferedImage resizedImage = new BufferedImage(size, size, 1);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(src, 0, 0, size, size, null);
        g.dispose();
        return resizedImage;
    }

    private BufferedImage getBrush(String s) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(Main.getPlugin().getDataFolder() + "/brushes/" + s));
        } catch (Exception localException) {
        }
        img = cropImage(img, Session.getConfig().getImgLoreSize());
        return img;
    }

    private float getGrayScale(BufferedImage img, int x, int z) {
        int size = img.getHeight();

        int rgb = 0;
        if ((z < size) && (x < size) && (x >= 0) && (z >= 0)) {
            rgb = img.getRGB(x, z);
        }
        int red = rgb >>> 16 & 0xFF;
        int green = rgb >>> 8 & 0xFF;
        int blue = rgb & 0xFF;
        if ((red == 0) && (green == 0) && (blue == 0)) {
            return 0.0F;
        }
        return (red + blue + green) / 3.0F / 2.55F;
    }

    public Inventory getInventory() {
        return this.INVENTORY;
    }

    public int getPageNumber() {
        return this.PAGE_NUMBER;
    }
}
