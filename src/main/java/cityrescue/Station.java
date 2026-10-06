package cityrescue;

/**
 * Represents an emergency response station within the city.
 * Manages its own unique ID, geographical location, and the capacity
 * constraints for housing emergency units.
 */
public class Station {
    
    private int id;
    private String name;
    private int x;
    private int y;
    private int maxUnits;
    private int unitCount;
 
    /**
     * Constructs a new emergency station.
     * @param id The unique integer ID assigned to this station.
     * @param name The name of the station.
     * @param x The X coordinate on the city grid.
     * @param y The Y coordinate on the city grid.
     */
    public Station(int id, String name, int x, int y) {
        this.id = id;
        this.name = name;
        this.x = x;
        this.y = y;
        // Default capacity (for the testing of when im making the coursework)
        this.maxUnits = 5; 
        this.unitCount = 0;
    }

    /**
     * Retrieves the unique ID of the station.
     * @return The station's ID.
     */
    public int getId() { return id; }

    /**
     * Retrieves the name of the station.
     * @return The station's name.
     */
    public String getName() { return name; }
    
    /**
     * Retrieves the X coordinate of the station.
     * @return The X coordinate.
     */
    public int getX() { return x; }

    /**
     * Retrieves the Y coordinate of the station.
     * @return The Y coordinate.
     */
    public int getY() { return y; }
    
    /**
     * Retrieves the maximum number of units this station can hold.
     * @return The maximum capacity.
     */
    public int getMaxUnits() { return maxUnits; }

    /**
     * Updates the maximum capacity of units for this station.
     * @param maxUnits The new maximum capacity.
     */
    public void setMaxUnits(int maxUnits) { this.maxUnits = maxUnits; }
    
    /**
     * Retrieves the current number of units assigned to this station.
     * @return The current unit count.
     */
    public int getUnitCount() { return unitCount; }

    /**
     * Increments the count of units currently assigned to this station by one.
     */
    public void incrementUnitCount() { this.unitCount++; }

    /**
     * Decrements the count of units currently assigned to this station by one.
     */
    public void decrementUnitCount() { this.unitCount--; }

    /**
     * Returns a formatted string representation of the station's status.
     * @return A string containing the station's ID, name, location, and capacity.
     */
    @Override
    public String toString() {
        return "Station " + id + " (" + name + ") at (" + x + "," + y + ") - Capacity: " + unitCount + "/" + maxUnits;
    }
}