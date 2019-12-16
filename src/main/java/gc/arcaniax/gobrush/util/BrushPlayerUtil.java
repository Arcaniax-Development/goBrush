package gc.arcaniax.gobrush.util;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.math.Vector3;
import gc.arcaniax.gobrush.Session;
import gc.arcaniax.gobrush.object.BrushPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BrushPlayerUtil {
    public static Location getClosest(Player player, Location _loc, Location l, int brushSize) {
        Location loc = _loc.clone();
        EditSession editsession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld(player.getWorld().getName())).build();
        while ((loc.getBlock().getType() == XMaterial.AIR.parseMaterial()) || (
                (editsession.getMask() != null) && (!editsession.getMask().test(Vector3.at(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()).toBlockPoint())) &&
                        (loc.distance(l.clone().add(0.5D, 0.5D, 0.5D)) < brushSize / 4.0D))) {
            Vector v = player.getEyeLocation().getDirection();
            loc.add(v.multiply(0.5D));
            if (!BlockUtils.isLoaded(loc)) {
                return null;
            }
            if (loc.getBlockY() <= 0) {
                break;
            }
            if (loc.getBlockY() > 255) {
                return null;
            }
            if (loc.distance(_loc) > 200.0D) {
                return null;
            }
        }
        return loc;
    }

    public static Location getClosest(Player player) {
        Location loc = player.getEyeLocation();
        while (loc.getBlock().getType() == XMaterial.AIR.parseMaterial()) {
            Vector v = player.getEyeLocation().getDirection();
            loc.add(v.multiply(0.5D));
            if (!BlockUtils.isLoaded(loc)) {
                return null;
            }
            if (loc.getBlockY() <= 0) {
                break;
            }
            if (loc.getBlockY() > 255) {
                return null;
            }
            if (loc.distance(player.getEyeLocation()) > 200.0D) {
                return null;
            }
        }
        return loc;
    }

    public static String getCardinalDirection(Player player) {
        double rotation = (player.getLocation().getYaw() - 90.0F) % 360.0F;
        if (rotation < 0.0D) {
            rotation += 360.0D;
        }
        if ((0.0D <= rotation) && (rotation < 45.0D)) {
            return "W";
        }
        if ((45.0D <= rotation) && (rotation < 135.0D)) {
            return "N";
        }
        if ((135.0D <= rotation) && (rotation < 225.0D)) {
            return "E";
        }
        if ((225.0D <= rotation) && (rotation < 315.0D)) {
            return "S";
        }
        if ((315.0D <= rotation) && (rotation < 360.0D)) {
            return "W";
        }
        return null;
    }

    public static double getHeight(Player player, int x, int z, String cardinalDir) {
        BrushPlayer brushPlayer = Session.getBrushPlayer(player.getUniqueId());
        int size = brushPlayer.getBrush().getCroppedPattern().getHeight();
        int _x = x;
        int _z = z;
        if (brushPlayer.isAutoRotation()) {
            switch (cardinalDir) {
                case "S":
                    x = size - _x;
                    z = size - _z;
                    break;
                case "W":
                    x = size - _z;
                    z = _x;
                    break;
                case "E":
                    x = _z;
                    z = size - _x;
            }
        }
        int rgb = 0;
        if ((z < size) && (x < size) && (x >= 0) && (z >= 0)) {
            rgb = brushPlayer.getBrush().getCroppedPattern().getRGB(x, z);
        }
        int red = rgb >>> 16 & 0xFF;
        int green = rgb >>> 8 & 0xFF;
        int blue = rgb & 0xFF;
        if ((red == 0) && (green == 0) && (blue == 0)) {
            return 0.0D;
        }
        float grayScale = (red + blue + green) / 3.0F / 255.0F;

        double height = grayScale * brushPlayer.getBrushIntensity();
        return height;
    }
}
