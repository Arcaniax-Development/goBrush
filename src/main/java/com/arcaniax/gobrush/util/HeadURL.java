/*
 * goBrush is designed to streamline and simplify your mountain building experience.
 * Copyright (C) Arcaniax-Development
 * Copyright (C) Arcaniax team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.arcaniax.gobrush.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.Material;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class HeadURL {

    public static String upB64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDFhOTNiZWQyZmQ2MjU2NTI0NWFjMTgyMWM3ZGQyZjg5OTFlMmU1ZGQzNGMwZDAzNzJiM2EyNGMwMmEyNDUifX19";
    public static String downB64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDM3MGFjNWYzY2JmZDk4ZDBhMWJkNDU2ZWM4MWFkNjIxZGI0YjZlZDQ1MTk0ZGU4MGJiY2I3OWFjNDA5NGMzIn19fQ==";
    public static String _3DB64 = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNmYTRmYzk4YzliMWIyNGE4YjAyZjY5NDM1MTNmYmIyMmRiMWQzNzRhODdmZGU5MWI3NTkzOWU1YThhMiJ9fX0=";

    public static ItemStack create(String data, String name, String lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta headMeta = (SkullMeta) item.getItemMeta();
        try {
                PlayerProfile playerProfile = Bukkit.getServer().createPlayerProfile(UUID.randomUUID(), "goBrush");
                PlayerTextures texture = playerProfile.getTextures();
                String url = null;
                byte[] decoded = Base64.getDecoder().decode(data);
                try {
                    url = new String(decoded, StandardCharsets.UTF_8);
                } catch (Exception ignored) {}
                url = url.replace("{\"textures\":{\"SKIN\":{\"url\":\"", "").replace("\"}}}", "");
                texture.setSkin(new URL(url));
                headMeta.setOwnerProfile(playerProfile);
            } catch (Exception ignored) {
            }
        List<String> loreList = new ArrayList<>();
        if (!lore.isEmpty()) {
            loreList.add(lore);
        }
        headMeta.setLore(loreList);
        if (!name.isEmpty()) {
            headMeta.setDisplayName(name);
        }
        item.setItemMeta(headMeta);
        return item;
    }

}
