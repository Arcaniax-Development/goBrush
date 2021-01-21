package com.arcaniax.gobrush.enumeration;

public enum MainMenuSlot {

    /* Constants */
    BRUSH_SELECTOR(1, 10, 19),
    BRUSH_SIZE(2, 11, 20),
    BRUSH_INTENSITY(3, 12, 21),
    MODE_DIRECTION(4, 13, 22),
    MODE_3D(5, 14, 23),
    MODE_FLAT(6, 15, 24),
    FEATURE_AUTOROTATION(7, 16, 25);

    /* Attributes */
    private final int UPPER, MIDDLE, LOWER;

    /**
     * Constructor of a MainMenuSlot enum object.
     *
     * @param upper  The upper slot number.
     * @param middle The middle slot number.
     * @param lower  The lower slot number.
     */
    MainMenuSlot(int upper, int middle, int lower) {
        this.UPPER = upper;
        this.MIDDLE = middle;
        this.LOWER = lower;
    }

    /**
     * This method checks if the provided slot is an upper, middle or lower
     * slot.
     *
     * @param slot The slot number that needs to be checked.
     * @return True if the provided slot number is valid in the range of the
     * enum object, false otherwise.
     */
    public boolean isValidSlot(int slot) {
        return slot == UPPER || slot == MIDDLE || slot == LOWER;
    }

    /**
     * Getter for the upper slot number.
     *
     * @return The upper slot number.
     */
    public int getUpper() {
        return UPPER;
    }

    /**
     * Getter for the middle slot number.
     *
     * @return The middle slot number.
     */
    public int getMiddle() {
        return MIDDLE;
    }

    /**
     * Getter for the lower slot number.
     *
     * @return The lower slot number.
     */
    public int getLower() {
        return LOWER;
    }

}
