package gc.arcaniax.gobrush.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class headURL {
    public static String upB64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDFhOTNiZWQyZmQ2MjU2NTI0NWFjMTgyMWM3ZGQyZjg5OTFlMmU1ZGQzNGMwZDAzNzJiM2EyNGMwMmEyNDUifX19";
    public static String downB64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDM3MGFjNWYzY2JmZDk4ZDBhMWJkNDU2ZWM4MWFkNjIxZGI0YjZlZDQ1MTk0ZGU4MGJiY2I3OWFjNDA5NGMzIn19fQ==";
    public static String _3DB64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNmYTRmYzk4YzliMWIyNGE4YjAyZjY5NDM1MTNmYmIyMmRiMWQzNzRhODdmZGU5MWI3NTkzOWU1YThhMiJ9fX0=";

    public static ItemStack create(String data, String name, String lore) {
        ItemStack item = new ItemStack(XMaterial.PLAYER_HEAD.parseMaterial());
        item.setDurability((short) XMaterial.PLAYER_HEAD.data);
        SkullMeta headMeta = (SkullMeta) item.getItemMeta();
        if (!lore.equals("")) {
            String[] loreListArray = lore.split("___");
            List<String> loreList = new ArrayList();
            String[] arrayOfString1;
            int j = (arrayOfString1 = loreListArray).length;
            for (int i = 0; i < j; i++) {
                String s = arrayOfString1[i];
                loreList.add(ChatColor.translateAlternateColorCodes('&', s));
            }
            headMeta.setLore(loreList);
        }
        if (!name.equals("")) {
            headMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        }
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", data));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(headMeta, profile);
        } catch (Exception localException) {
        }
        item.setItemMeta(headMeta);
        return item;
    }
}
