/**
 * A concrete implementation of a Unit representing a Fire Engine.
 * Responds to FIRE incidents and requires exactly 4 ticks 
 * of on-scene progress to resolve an emergency.
 */

package cityrescue;
import cityrescue.enums.UnitType;

public class FireEngine extends Unit {
    public FireEngine(int id, int x, int y) {
        super(id, UnitType.FIRE_ENGINE, x, y);
    }

    @Override
    public int getOnSceneDuration() {
        return 4; // Fire Engines take 4 ticks
    }
}