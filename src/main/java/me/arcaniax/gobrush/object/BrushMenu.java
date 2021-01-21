package me.arcaniax.gobrush.object;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class contains the object of BrushMenu. This object is the menu of
 * brushes, made up of BrushPage objects.
 *
 * @author McJeffr
 */
public class BrushMenu {

    /* Attributes */
    private final List<BrushPage> PAGES;
    private final int AMOUNT_OF_PAGES;

    /**
     * Constructor of a BrushMenu object. This constructor makes a menu of
     * brushes that contains BrushPage objects which are the individual pages of
     * brushes.
     *
     * @param brushes A List containing the brushes that need to be added.
     */
    @SuppressWarnings("unchecked")
    public BrushMenu(List<Brush> brushes) {
        this.PAGES = new ArrayList<>();
        Collections.sort(brushes);
        this.AMOUNT_OF_PAGES = (int) Math.ceil((brushes.size() + 1) / 45.0);
        for (int i = 0; i < this.AMOUNT_OF_PAGES; i++) {
            int start = (i * 45);
            int end = ((i + 1) * 45);
            BrushPage page;
            if (brushes.size() >= end) {
                List<Brush> subList = brushes.subList(start, end);
                page = new BrushPage(subList, i, AMOUNT_OF_PAGES);
            } else {
                List<Brush> subList = brushes.subList(start, brushes.size());
                page = new BrushPage(subList, i, AMOUNT_OF_PAGES);
            }
            this.PAGES.add(page);
        }
    }

    /**
     * This method gets a BrushPage object at the given index.
     *
     * @param index The index of the BrushPage that needs to be retrieved.
     * @return The BrushPage if the index exists, null otherwise.
     */
    public BrushPage getPage(int index) {
        if (index < this.PAGES.size()) {
            return this.PAGES.get(index);
        } else {
            return null;
        }
    }

    /**
     * Getter for the amount of pages the BrushMenu contains.
     *
     * @return The amount of pages the BrushMenu contains.
     */
    public int getAmountOfPages() {
        return AMOUNT_OF_PAGES;
    }

}
