/**
 * A concrete implementation of a Unit representing an Ambulance.
 * Responds to MEDICAL incidents and requires exactly 2 ticks 
 * of on-scene progress to resolve an emergency.
 */

package cityrescue;
import cityrescue.enums.UnitType;

public class Ambulance extends Unit {
    public Ambulance(int id, int x, int y) {
        super(id, UnitType.AMBULANCE, x, y);
    }

    @Override
    public int getOnSceneDuration() {
        return 2; // Ambulances take 2 ticks
    }
}