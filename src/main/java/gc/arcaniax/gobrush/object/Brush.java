package gc.arcaniax.gobrush.object;

import gc.arcaniax.gobrush.Main;
import gc.arcaniax.gobrush.Session;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import javax.imageio.ImageIO;
import org.bukkit.plugin.java.JavaPlugin;

public class Brush
  implements Comparable
{
  private static final String DEFAULT_NAME = Session.getConfig().getDefaultBrushName();
  private static final int DEFAULT_SIZE = 15;
  private String name;
  private BufferedImage unmodifiedPattern;
  private BufferedImage croppedPattern;
  private int size;
  
  public Brush()
  {
    this.name = DEFAULT_NAME;
    this.unmodifiedPattern = getPattern(DEFAULT_NAME);
    this.size = 15;
    
    BufferedImage pattern = new BufferedImage(this.size, this.size, 1);
    Graphics2D graphic = pattern.createGraphics();
    graphic.drawImage(this.unmodifiedPattern, 0, 0, this.size, this.size, null);
    graphic.dispose();
    this.croppedPattern = pattern;
  }
  
  public Brush(String name)
  {
    this.name = name;
    this.unmodifiedPattern = getPattern(name);
    this.croppedPattern = null;
    this.size = 0;
  }
  
  public Brush(String name, int size)
  {
    this.name = name;
    this.unmodifiedPattern = getPattern(name);
    System.out.println(this.unmodifiedPattern);
    this.size = size;
    
    BufferedImage pattern = new BufferedImage(size, size, 1);
    Graphics2D graphic = pattern.createGraphics();
    graphic.drawImage(this.unmodifiedPattern, 0, 0, size, size, null);
    graphic.dispose();
    this.croppedPattern = pattern;
  }
  
  private BufferedImage getPattern(String name)
  {
    BufferedImage pattern;
    try
    {
      pattern = ImageIO.read(new File(Main.getPlugin().getDataFolder() + "/brushes/" + name));
    }
    catch (IOException ex)
    {
      BufferedImage pattern;
      pattern = null;
    }
    return pattern;
  }
  
  public void resize(int size)
  {
    this.size = size;
    BufferedImage pattern = new BufferedImage(size, size, 1);
    Graphics2D graphic = pattern.createGraphics();
    graphic.drawImage(this.unmodifiedPattern, 0, 0, size, size, null);
    graphic.dispose();
    this.croppedPattern = pattern;
  }
  
  public String getName()
  {
    return this.name;
  }
  
  public int getSize()
  {
    return this.size;
  }
  
  public BufferedImage getCroppedPattern()
  {
    return this.croppedPattern;
  }
  
  public int compareTo(Object o)
  {
    return getName().compareTo(((Brush)o).getName());
  }
}
