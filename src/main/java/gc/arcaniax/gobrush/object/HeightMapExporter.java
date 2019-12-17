package gc.arcaniax.gobrush.object;

import com.boydti.fawe.FaweAPI;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.regions.Region;
import gc.arcaniax.gobrush.Main;
import gc.arcaniax.gobrush.Session;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HeightMapExporter {

    boolean worldEditSelection;
    int minX;
    int minZ;
    int maxX;
    int maxZ;
    World w;
    Player p;

    public HeightMapExporter(Player p) throws IncompleteRegionException {
        Region selection = Session.getWorldEdit().getSession(p).getSelection(FaweAPI.getWorld(p.getWorld().getName()));
        if (selection != null) {
            worldEditSelection = true;
            minX = selection.getMinimumPoint().getBlockX();
            minZ = selection.getMinimumPoint().getBlockZ();
            maxX = selection.getMaximumPoint().getBlockX();
            maxZ = selection.getMaximumPoint().getBlockZ();
            w = p.getWorld();
            this.p = p;
        } else {
            worldEditSelection = false;
        }
    }

    public boolean hasWorldEditSelection() {
        return worldEditSelection;
    }


    public void exportImage(int size, String name) {
        String prefix = "§bgoBrush> ";
        int AdjustX = 0;
        int AdjustZ = 0;
        int blockSize = maxX - minX;
        if (maxZ - minZ > blockSize) {
            blockSize = maxZ - minZ;
            AdjustX = (blockSize - (maxX - minX)) / 2;
        } else {
            AdjustZ = (blockSize - (maxZ - minZ)) / 2;
        }
        if (blockSize < 20) {
            p.sendMessage(prefix + "§cPlease make a bigger selection");
            return;
        }
        int highest = 0;
        int lowest = 254;
        for (int x = 0; x < (maxX - minX); x++) {
            for (int z = 0; z < (maxZ - minZ); z++) {
                EditSession editsession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld(p.getWorld().getName())).build();
                int y = editsession.getHighestTerrainBlock(x + minX, z + minZ, 0, 255);
                if (y > highest) {
                    highest = y;
                }
                if (y < lowest) {
                    lowest = y;
                }
            }
        }
        int height = highest - lowest;
        if (height < 10) {
            p.sendMessage(prefix + "§cPlease select a bigger mountain");
            return;
        }
        BufferedImage img = new BufferedImage(blockSize, blockSize, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < (maxX - minX); x++) {
            for (int z = 0; z < (maxZ - minZ); z++) {
                EditSession editsession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld(p.getWorld().getName())).build();
                int y = editsession.getHighestTerrainBlock(x + minX, z + minZ, 0, 255);
                int i = (int) (((double) (y - lowest) / (double) height) * (double) 255);
                int rgb = i; //red
                rgb = (rgb << 8) + i;//green
                rgb = (rgb << 8) + i;//blue
                img.setRGB(x + AdjustX, z + AdjustZ, rgb);
            }
        }

        img = resize(img, 500);
        File f = new File(Main.getPlugin().getDataFolder() + "/brushes/" + name + ".png");
        try {
            ImageIO.write(img, "PNG", f);
        } catch (IOException e) {
        }
    }

    private BufferedImage resize(BufferedImage bi, int size) {
        BufferedImage pattern = new BufferedImage(size, size, 1);
        Graphics2D graphic = pattern.createGraphics();
        graphic.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        graphic.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        graphic.drawImage(bi, 0, 0, size, size, null);
        graphic.dispose();
        return pattern;
    }
}
