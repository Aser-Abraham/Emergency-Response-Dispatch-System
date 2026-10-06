package cityrescue;

/**
 * Represents the geographical grid of the city simulation.
 * Responsible for maintaining the grid dimensions and tracking the locations
 * of obstacles to ensure valid movement and placement of entities.
 */
public class CityMap {
    private final int width;
    private final int height;
    private final boolean[][] blocked;

    /**
     * Constructs a new CityMap with the specified dimensions.
     * * @param width The total width of the city grid.
     * @param height The total height of the city grid.
     */
    public CityMap(int width, int height) {
        this.width = width;
        this.height = height;
        this.blocked = new boolean[width][height];
    }

    /**
     * Retrieves the width of the city grid.
     * * @return The width as an integer.
     */
    public int getWidth() { return width; }

    /**
     * Retrieves the height of the city grid.
     * * @return The height as an integer.
     */
    public int getHeight() { return height; }

    /**
     * Places or removes an obstacle at a specific grid coordinate.
     * * @param x The X coordinate to modify.
     * @param y The Y coordinate to modify.
     * @param isBlocked true to place an obstacle, false to remove one.
     */
    public void setBlocked(int x, int y, boolean isBlocked) {
        blocked[x][y] = isBlocked;
    }

    /**
     * Checks if a specific coordinate on the grid is blocked by an obstacle.
     * * @param x The X coordinate to check.
     * @param y The Y coordinate to check.
     * @return true if the location is blocked, false otherwise.
     */
    public boolean isBlocked(int x, int y) {
        return blocked[x][y];
    }

    /**
     * Verifies if a given coordinate falls within the boundaries of the city grid.
     * * @param x The X coordinate to verify.
     * @param y The Y coordinate to verify.
     * @return true if the coordinates are within bounds, false if they are outside.
     */
    public boolean isValidLocation(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }
}