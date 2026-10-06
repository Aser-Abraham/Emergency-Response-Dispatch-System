package cityrescue;

import cityrescue.enums.*;

/**
 * An abstract base class representing an emergency response vehicle.
 * Encapsulates common state such as location, status, and target incident.
 * Subclasses must define specific behaviors, such as the required on-scene 
 * duration for their specific unit type.
 */
public abstract class Unit {
    
    private int id;
    private UnitType type;
    private UnitStatus status;
    private int x;
    private int y;
    private int homeStationId;
    
    // These variables are for simulation tracking
    private int targetIncidentId;
    private int ticksAtScene;

    /**
     * Constructs a new emergency response unit.
     * @param id The unique integer ID assigned to this unit.
     * @param type The specific type of the unit (e.g., AMBULANCE, POLICE_CAR).
     * @param x The starting X coordinate of the unit.
     * @param y The starting Y coordinate of the unit.
     */
    public Unit(int id, UnitType type, int x, int y) {
        this.id = id;
        this.type = type;
        this.x = x;
        this.y = y;
        this.status = UnitStatus.IDLE; // Units always start IDLE
        this.targetIncidentId = -1;    // -1 means no target yet
        this.ticksAtScene = 0;
    }

    /**
     * Retrieves the required number of ticks this unit must spend at an incident scene to resolve it.
     * @return The duration in ticks.
     */
    public abstract int getOnSceneDuration();

    /**
     * Retrieves the unique ID of the unit.
     * @return The unit's ID.
     */
    public int getId() { return id; }
    
    /**
     * Retrieves the type of the unit.
     * @return The unit's type (e.g., FIRE_ENGINE).
     */
    public UnitType getType() { return type; }
    
    /**
     * Retrieves the current operational status of the unit.
     * @return The unit's status (e.g., IDLE, EN_ROUTE, AT_SCENE).
     */
    public UnitStatus getStatus() { return status; }

    /**
     * Updates the current operational status of the unit.
     * @param status The new status to apply.
     */
    public void setStatus(UnitStatus status) { this.status = status; }
    
    /**
     * Retrieves the current X coordinate of the unit.
     * @return The X coordinate.
     */
    public int getX() { return x; }

    /**
     * Updates the current X coordinate of the unit.
     * @param x The new X coordinate.
     */
    public void setX(int x) { this.x = x; }
    
    /**
     * Retrieves the current Y coordinate of the unit.
     * @return The Y coordinate.
     */
    public int getY() { return y; }

    /**
     * Updates the current Y coordinate of the unit.
     * @param y The new Y coordinate.
     */
    public void setY(int y) { this.y = y; }
    
    /**
     * Retrieves the ID of the station this unit belongs to.
     * @return The home station's ID.
     */
    public int getHomeStationId() { return homeStationId; }

    /**
     * Updates the home station ID for this unit.
     * @param homeStationId The new home station's ID.
     */
    public void setHomeStationId(int homeStationId) { this.homeStationId = homeStationId; }
    
    /**
     * Retrieves the ID of the incident this unit is currently assigned to.
     * @return The target incident ID, or -1 if unassigned.
     */
    public int getTargetIncidentId() { return targetIncidentId; }

    /**
     * Assigns a target incident ID to this unit.
     * @param targetIncidentId The incident ID to track.
     */
    public void setTargetIncidentId(int targetIncidentId) { this.targetIncidentId = targetIncidentId; }
    
    /**
     * Retrieves the number of ticks this unit has currently spent at the scene of an incident.
     * @return The number of ticks spent at the scene.
     */
    public int getTicksAtScene() { return ticksAtScene; }

    /**
     * Updates the number of ticks this unit has spent at the scene of an incident.
     * @param ticksAtScene The new tick count.
     */
    public void setTicksAtScene(int ticksAtScene) { this.ticksAtScene = ticksAtScene; }

    /**
     * Returns a formatted string representation of the unit's status.
     * @return A string containing the unit's ID, type, status, and current location.
     */
    @Override
    public String toString() {
        return "Unit " + id + " (" + type + ") - Status: " + status + " at (" + x + "," + y + ")";
    }
}