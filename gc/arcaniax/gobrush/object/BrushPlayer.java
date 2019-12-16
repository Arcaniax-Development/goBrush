package gc.arcaniax.gobrush.object;

import gc.arcaniax.gobrush.Session;
import java.util.UUID;

public class BrushPlayer
{
  private UUID uuid;
  private int brushSize;
  private int brushIntensity;
  private boolean brushEnabled;
  private boolean flatMode;
  private boolean directionMode;
  private boolean _3dmode;
  private boolean autoRotation;
  private Brush brush;
  
  public BrushPlayer(UUID uuid)
  {
    this.uuid = uuid;
    this.brushSize = Session.getConfig().getDefaultBrushSize();
    this.brushIntensity = Session.getConfig().getDefaultBrushIntensity();
    this.directionMode = Session.getConfig().isDefaultDirectionMode();
    this.flatMode = Session.getConfig().isDefault3DMode();
    this._3dmode = Session.getConfig().isDefaultBoundingBox();
    this.autoRotation = Session.getConfig().isDefaultAutoRotation();
    this.brushEnabled = Session.getConfig().isDefaultBrushEnabled();
    this.brush = new Brush();
  }
  
  public void toggleBrushEnabled()
  {
    this.brushEnabled = (!this.brushEnabled);
  }
  
  public void toggleFlatMode()
  {
    this.flatMode = (!this.flatMode);
  }
  
  public void toggle3DMode()
  {
    this._3dmode = (!this._3dmode);
  }
  
  public void toggleAutoRotation()
  {
    this.autoRotation = (!this.autoRotation);
  }
  
  public UUID getUuid()
  {
    return this.uuid;
  }
  
  public void setUuid(UUID uuid)
  {
    this.uuid = uuid;
  }
  
  public int getBrushSize()
  {
    return this.brushSize;
  }
  
  public void setBrushSize(int brushSize)
  {
    this.brushSize = brushSize;
    this.brush.resize(brushSize);
  }
  
  public int getBrushIntensity()
  {
    return this.brushIntensity;
  }
  
  public void setBrushIntensity(int brushIntensity)
  {
    this.brushIntensity = brushIntensity;
  }
  
  public boolean isBrushEnabled()
  {
    return this.brushEnabled;
  }
  
  public boolean isFlatMode()
  {
    return this.flatMode;
  }
  
  public boolean isDirectionMode()
  {
    return this.directionMode;
  }
  
  public boolean is3DMode()
  {
    return this._3dmode;
  }
  
  public boolean isAutoRotation()
  {
    return this.autoRotation;
  }
  
  public Brush getBrush()
  {
    return this.brush;
  }
  
  public void setBrush(Brush brush)
  {
    this.brush = brush;
  }
  
  public int getMaxBrushSize()
  {
    return Session.getConfig().getMaxBrushSize();
  }
  
  public int getMaxBrushIntensity()
  {
    return Session.getConfig().getMaxBrushIntensity();
  }
  
  public void toggleDirectionMode()
  {
    this.directionMode = (!this.directionMode);
  }
}
