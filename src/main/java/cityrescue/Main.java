package cityrescue;

import cityrescue.enums.*;
import cityrescue.exceptions.*;

public class Main {
    public static void main(String[] args) {
        try {
            // 1. Initialise the engine
            CityRescue engine = new CityRescueImpl();
            engine.initialise(10, 10);
            
            System.out.println(" CITY RESCUE SIMULATION ONLINE\n");

            //THE EDGE CASE PROOF  
    
            engine.addObstacle(1, 0); 
            System.out.println("Edge Case: Obstacle added at (1,0) to test deterministic pathing.\n");

            // 2. Build a hospital at coordinate (0, 0)
          
            int hospitalId = engine.addStation("Central Hospital", 0, 0);
            
            // 3. Add an Ambulance
            engine.addUnit(hospitalId, UnitType.AMBULANCE);

            // 4. Report the incident
            engine.reportIncident(IncidentType.MEDICAL, 8, 5, 5);

            // 5. Dispatch
            engine.dispatch();

            // 6. Simulation Loop
            for (int i = 1; i <= 12; i++) {
                engine.tick(); 
                System.out.println(engine.getStatus());
                Thread.sleep(500); 
            }

            System.out.println("SIMULATION COMPLETE!");

        } catch (Exception e) {
            // This will tell us EXACTLY what went wrong if it fails again
            System.out.println(" ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
