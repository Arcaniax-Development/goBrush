package gc.arcaniax.gobrush.enumeration;

public enum MainMenuSlot {
    BRUSH_SELECTOR(1, 10, 19), BRUSH_SIZE(2, 11, 20), BRUSH_INTENSITY(3, 12, 21), MODE_DIRECTION(4, 13, 22), MODE_3D(5, 14, 23), MODE_FLAT(6, 15, 24), FEATURE_AUTOROTATION(7, 16, 25);

    private final int UPPER;
    private final int MIDDLE;
    private final int LOWER;

    MainMenuSlot(int upper, int middle, int lower) {
        this.UPPER = upper;
        this.MIDDLE = middle;
        this.LOWER = lower;
    }

    public boolean isValidSlot(int slot) {
        return (slot == this.UPPER) || (slot == this.MIDDLE) || (slot == this.LOWER);
    }

    public int getUpper() {
        return this.UPPER;
    }

    public int getMiddle() {
        return this.MIDDLE;
    }

    public int getLower() {
        return this.LOWER;
    }
}
