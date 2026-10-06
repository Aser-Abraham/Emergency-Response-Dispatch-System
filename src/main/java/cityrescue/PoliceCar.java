package cityrescue;

import cityrescue.enums.UnitType;

/**
 * A concrete implementation of a Unit representing a Police Car.
 * Responds to CRIME incidents and requires exactly 3 ticks 
 * of on-scene progress to resolve an emergency.
 */
public class PoliceCar extends Unit {
    
    /**
     * Constructs a new PoliceCar unit.
     * * @param id The unique integer ID assigned to this unit.
     * @param x The starting X coordinate (usually the home station's X).
     * @param y The starting Y coordinate (usually the home station's Y).
     */
    public PoliceCar(int id, int x, int y) {
        super(id, UnitType.POLICE_CAR, x, y);
    }

    /**
     * Retrieves the number of ticks this unit must spend at the scene to resolve an incident.
     * * @return 3 ticks for a Police Car.
     */
    @Override
    public int getOnSceneDuration() {
        return 3; 
    }
}