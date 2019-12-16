package gc.arcaniax.gobrush.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BrushMenu
{
  private final List<BrushPage> PAGES;
  private final int AMOUNT_OF_PAGES;
  
  public BrushMenu(List<Brush> brushes)
  {
    this.PAGES = new ArrayList();
    Collections.sort(brushes);
    this.AMOUNT_OF_PAGES = ((int)Math.ceil((brushes.size() + 1) / 45.0D));
    for (int i = 0; i < this.AMOUNT_OF_PAGES; i++)
    {
      int start = i * 45;
      int end = (i + 1) * 45;
      BrushPage page;
      BrushPage page;
      if (brushes.size() >= end)
      {
        List<Brush> subList = brushes.subList(start, end);
        page = new BrushPage(subList, i, this.AMOUNT_OF_PAGES);
      }
      else
      {
        List<Brush> subList = brushes.subList(start, brushes.size());
        page = new BrushPage(subList, i, this.AMOUNT_OF_PAGES);
      }
      this.PAGES.add(page);
    }
  }
  
  public BrushPage getPage(int index)
  {
    if (index < this.PAGES.size()) {
      return (BrushPage)this.PAGES.get(index);
    }
    return null;
  }
  
  public int getAmountOfPages()
  {
    return this.AMOUNT_OF_PAGES;
  }
}
