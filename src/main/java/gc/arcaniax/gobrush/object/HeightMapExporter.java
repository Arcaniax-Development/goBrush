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

    public HeightMapExporter(Player p)
            throws IncompleteRegionException {
        Region selection = Session.getWorldEdit().getSession(p).getSelection(FaweAPI.getWorld(p.getWorld().getName()));
        if (selection != null) {
            this.worldEditSelection = true;
            this.minX = selection.getMinimumPoint().getBlockX();
            this.minZ = selection.getMinimumPoint().getBlockZ();
            this.maxX = selection.getMaximumPoint().getBlockX();
            this.maxZ = selection.getMaximumPoint().getBlockZ();
            this.w = p.getWorld();
            this.p = p;
        } else {
            this.worldEditSelection = false;
        }
    }

    public boolean hasWorldEditSelection() {
        return this.worldEditSelection;
    }

    public void exportImage(int size, String name) {
        String prefix = "§bgoBrush> ";
        int AdjustX = 0;
        int AdjustZ = 0;
        int blockSize = this.maxX - this.minX;
        if (this.maxZ - this.minZ > blockSize) {
            blockSize = this.maxZ - this.minZ;
            AdjustX = (blockSize - (this.maxX - this.minX)) / 2;
        } else {
            AdjustZ = (blockSize - (this.maxZ - this.minZ)) / 2;
        }
        if (blockSize < 20) {
            this.p.sendMessage(prefix + "§cPlease make a bigger selection");
            return;
        }
        int highest = 0;
        int lowest = 254;
        for (int x = 0; x < this.maxX - this.minX; x++) {
            for (int z = 0; z < this.maxZ - this.minZ; z++) {
                EditSession editsession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld(this.p.getWorld().getName())).build();
                int y = editsession.getHighestTerrainBlock(x + this.minX, z + this.minZ, 0, 255);
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
            this.p.sendMessage(prefix + "§cPlease select a bigger mountain");
            return;
        }
        BufferedImage img = new BufferedImage(blockSize, blockSize, 1);
        for (int x = 0; x < this.maxX - this.minX; x++) {
            for (int z = 0; z < this.maxZ - this.minZ; z++) {
                EditSession editsession = FaweAPI.getEditSessionBuilder(FaweAPI.getWorld(this.p.getWorld().getName())).build();
                int y = editsession.getHighestTerrainBlock(x + this.minX, z + this.minZ, 0, 255);
                int i = (int) ((y - lowest) / height * 255.0D);
                int rgb = i;
                rgb = (rgb << 8) + i;
                rgb = (rgb << 8) + i;
                img.setRGB(x + AdjustX, z + AdjustZ, rgb);
            }
        }
        img = resize(img, 500);
        File f = new File(Main.getPlugin().getDataFolder() + "/brushes/" + name + ".png");
        try {
            ImageIO.write(img, "PNG", f);
        } catch (IOException localIOException) {
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
