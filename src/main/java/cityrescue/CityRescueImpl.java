package cityrescue;

import cityrescue.enums.*;
import cityrescue.exceptions.*;

/**
 * CityRescueImpl (Starter)
 *
 * Your task is to implement the full specification.
 * You may add additional classes in any package(s) you like.
 */
public class CityRescueImpl implements CityRescue {

    // TODO: add fields (map, arrays for stations/units/incidents, counters, tick, etc.)


    // Storage arrays 
    private Station[] stations;
    private Unit[] units;
    private Incident[] incidents;

    // Counters for the current number of objects
    private int stationCount;
    private int unitCount;
    private int incidentCount;

    // Dimensions and tick counter 
    private CityMap map;
    private int tick;

    // ID generators
    private int nextStationId;
    private int nextUnitId;
    private int nextIncidentId;

    // Defining Constants 
    private static final int MAX_STATIONS = 20;
    private static final int MAX_UNITS = 50;
    private static final int MAX_INCIDENTS = 200;

    @Override
        public void initialise(int width, int height) throws InvalidGridException {
            if (width <= 0 || height <= 0) { // Ensuring the dimentions are within the boundries 
                throw new InvalidGridException("Invalid grid dimensions.");
            }

            this.map = new CityMap(width, height);  // We use the new map object to handle dimensions

            this.stations = new Station[MAX_STATIONS];
            this.units = new Unit[MAX_UNITS];
            this.incidents = new Incident[MAX_INCIDENTS];
            // ...

            // Reset all counters and IDs
            this.stationCount = 0;
            this.unitCount = 0;
            this.incidentCount = 0;
            this.nextStationId = 1;
            this.nextUnitId = 1;
            this.nextIncidentId = 1;
            this.tick = 0;
        }

  @Override
    public int[] getGridSize() {
        return new int[] { map.getWidth(), map.getHeight() }; // Requesting the map object for dimensions
    }
   @Override
    public void addObstacle(int x, int y) throws InvalidLocationException {

        if (!map.isValidLocation(x, y)) { // Checking if location is valid 
            throw new InvalidLocationException("Location out of bounds.");
        }

        map.setBlocked(x, y, true); // Blocking a set coordinate (obstacle)
    }


    @Override
        public void removeObstacle(int x, int y) throws InvalidLocationException {

            if (!map.isValidLocation(x, y)) { 
                throw new InvalidLocationException("Location out of bounds.");
            }
            map.setBlocked(x, y, false); // Unblocking a set coordinate (removing obstacle)
        }

    @Override
    public int addStation(String name, int x, int y)
            throws InvalidNameException, InvalidLocationException {

        if (name == null || name.isBlank()) {
            throw new InvalidNameException("Station name invalid.");
        }

        if (!map.isValidLocation(x, y)) {
            throw new InvalidLocationException("Location out of bounds.");
        }


        if (map.isBlocked(x, y)) {  // check if there is an obstacle 
            throw new InvalidLocationException("Location is blocked by an obstacle.");
        }

        if (stationCount == MAX_STATIONS) {  // if the number of station count is = to the max station count then not allowed to add
            throw new CapacityExceededException("Max stations reached.");
        }

        int id = nextStationId++;  // else then add one
        stations[stationCount++] = new Station(id, name.trim(), x, y); // assigns id, name and coordinate to the new station
        return id;
    }

    @Override
    public void removeStation(int stationId)
            throws IDNotRecognisedException, IllegalStateException {

        int index = -1;
        for (int i = 0; i < stationCount; i++) {
            if (stations[i].getId() == stationId) {
                index = i;
                break;
            }
        }

        if (index == -1) throw new IDNotRecognisedException("Station not found.");


        if (stations[index].getUnitCount() > 0) {  // As you cant remove station if you have units this makes sure number of units is zero before removing
            throw new IllegalStateException("Station still owns units.");
        }

        // shifting all the stations left as one has just become empty 
        for (int i = index; i < stationCount - 1; i++) {
            stations[i] = stations[i + 1];
        }
        stations[--stationCount] = null;
    }

    @Override
    public void setStationCapacity(int stationId, int maxUnits) 
            throws IDNotRecognisedException, InvalidCapacityException {

        if (maxUnits < 0) throw new InvalidCapacityException("Negative capacity.");

        boolean found = false;
        for (int i = 0; i < stationCount; i++) {
            if (stations[i].getId() == stationId) {
                stations[i].setMaxUnits(maxUnits);
                found = true;
                break;
            }
        }   
        if (!found) throw new IDNotRecognisedException("ID not found.");
    }

    @Override
    public int[] getStationIds() {
        int[] ids = new int[stationCount];
        for (int i = 0; i < stationCount; i++) {
            ids[i] = stations[i].getId();
        }
        return ids;
    }

    @Override
    public int addUnit(int stationId, UnitType type) 
            throws IDNotRecognisedException, InvalidUnitException, IllegalStateException {

        //  Validation check
        if (type == null) throw new InvalidUnitException("Type is null");
        if (unitCount == MAX_UNITS) {
            throw new CapacityExceededException("Max units reached.");
        }
        if (unitCount == MAX_UNITS) throw new IllegalStateException("City full");

        Station target = findStation(stationId); 

        if (target.getUnitCount() == target.getMaxUnits()) {
            throw new IllegalStateException("Station full");
        }

        // Creates the unit
        Unit newUnit = createUnitSubclass(nextUnitId, type, target);
        newUnit.setHomeStationId(stationId);

        int assignedId = nextUnitId; // Save the ID to return it
        units[unitCount] = newUnit;  

        // Increment everything at the end
        unitCount++;
        nextUnitId++;
        target.incrementUnitCount(); 

        return assignedId;
    }

    private Station findStation(int stationId) throws IDNotRecognisedException {
    for (int i = 0; i < stationCount; i++) {
        if (stations[i].getId() == stationId) {
            return stations[i];
        }
    }
    throw new IDNotRecognisedException("Station " + stationId + " not found."); 
}

    private Unit createUnitSubclass(int id, UnitType type, Station s) {
    switch(type) {
        // (Ambulance=2, Police=3, Fire=4) 
        case AMBULANCE:   return new Ambulance(id, s.getX(), s.getY());
        case POLICE_CAR:  return new PoliceCar(id, s.getX(), s.getY());
        case FIRE_ENGINE: return new FireEngine(id, s.getX(), s.getY());
        default: return null;
    }
}
    @Override
    public void decommissionUnit(int unitId) throws IDNotRecognisedException, IllegalStateException {

        // Find the unit 
        int index = findUnitIndex(unitId);
        Unit targetUnit = units[index];

        // Checks if its not in IDLE
        if (targetUnit.getStatus() != UnitStatus.IDLE) {
            throw new IllegalStateException("Unit is currently active.");
        }

        // Updates the Station
        Station home = findStation(targetUnit.getHomeStationId());
        home.decrementUnitCount();

        // Shifts all units following the decommissioned one one position to the left
        for (int i = index; i < unitCount - 1; i++) {
            units[i] = units[i + 1];
        }

        // Cleaning up
        units[unitCount - 1] = null; 
        unitCount--;
    }

    private int findUnitIndex(int unitId) throws IDNotRecognisedException {
        // Searches the units array for a specific ID
        for (int i = 0; i < unitCount; i++) {
            if (units[i].getId() == unitId) {
                return i;
            }
        }
        throw new IDNotRecognisedException("Unit ID " + unitId + " not recognised.");
    }


    @Override
    public void transferUnit(int unitId, int newStationId) throws IDNotRecognisedException, IllegalStateException {

        // Find the unit 
        int uIndex = findUnitIndex(unitId);
        Unit targetUnit = units[uIndex];

        if (targetUnit.getStatus() != UnitStatus.IDLE) {
            throw new IllegalStateException("Unit must be IDLE to transfer.");
        }

        // Finds the target station 
        Station newStation = findStation(newStationId);

        // checks if there is space
        if (newStation.getUnitCount() == newStation.getMaxUnits()) {
            throw new IllegalStateException("New station is at maximum capacity.");
        }

        // Finds the old station to update its count
        Station oldStation = findStation(targetUnit.getHomeStationId());

        // Update the station capacities
        oldStation.decrementUnitCount();
        newStation.incrementUnitCount();


        // Updates the units ID and gives it a new postion on the grid 
        targetUnit.setHomeStationId(newStationId);
        targetUnit.setX(newStation.getX());
        targetUnit.setY(newStation.getY());
    }

    @Override
    public void setUnitOutOfService(int unitId, boolean outOfService) 
            throws IDNotRecognisedException, IllegalStateException {

        // Finds the unit
        int index = findUnitIndex(unitId);
        Unit targetUnit = units[index];

        if (outOfService) {
            // checks if IDLE is allowed to be taken offline
            if (targetUnit.getStatus() != UnitStatus.IDLE) {
                throw new IllegalStateException("Unit must be IDLE to be taken out of service.");
            }
            // State changing
            targetUnit.setStatus(UnitStatus.OUT_OF_SERVICE);
        } 
        // To bring the unit back into service
        else {
            // checks if out of service before it could be brought back online
            if (targetUnit.getStatus() != UnitStatus.OUT_OF_SERVICE) {
                throw new IllegalStateException("Unit is not currently out of service.");
            }
            // State changes
            targetUnit.setStatus(UnitStatus.IDLE);
        }
    }

    @Override
    public int[] getUnitIds() {
        // Create a new array with same count as units
        int[] ids = new int[unitCount];

        // Loops through and takes the id
        for (int i = 0; i < unitCount; i++) {
            ids[i] = units[i].getId();
        }

        return ids;
    }   

    @Override
    public String viewUnit(int unitId) throws IDNotRecognisedException {
        int index = findUnitIndex(unitId);
        Unit targetUnit = units[index];

        // Location = LOC 
        return "U#" + targetUnit.getId() + 
            ", TYPE=" + targetUnit.getType() + 
            ", STATUS=" + targetUnit.getStatus() + 
            ", LOC=(" + targetUnit.getX() + "," + targetUnit.getY() + ")" +
            ", STATION=" + targetUnit.getHomeStationId();
    }

    @Override
    public int reportIncident(IncidentType type, int severity, int x, int y) 
            throws InvalidSeverityException, InvalidLocationException {

        // Checks if inputed severity is between the valid range
        if (severity < 1 || severity > 10) {
            throw new InvalidSeverityException("Severity must be between 1 and 10.");
        }

        if (!map.isValidLocation(x, y)) {
            throw new InvalidLocationException("Coordinates (" + x + ", " + y + ") are out of bounds.");
        }

        int assignedId = nextIncidentId;
        Incident newIncident = new Incident(assignedId, type, severity, x, y);

        incidents[incidentCount] = newIncident;
        incidentCount++;
        nextIncidentId++;

        return assignedId;
    }

    @Override
    public void cancelIncident(int incidentId) throws IDNotRecognisedException, IllegalStateException {

        // Finds the incident 
        int index = findIncidentIndex(incidentId);
        Incident targetIncident = incidents[index];

        // checls if it can be cancelled given the parameters 
        if (targetIncident.getStatus() != IncidentStatus.REPORTED) {
            throw new IllegalStateException("Cannot cancel. Incident is currently: " + targetIncident.getStatus());
        }

        // State Change
        targetIncident.setStatus(IncidentStatus.CANCELLED);
    }

    private int findIncidentIndex(int incidentId) throws IDNotRecognisedException {
        for (int i = 0; i < incidentCount; i++) {
            if (incidents[i].getId() == incidentId) {
                return i;
            }
        }
        throw new IDNotRecognisedException("Incident ID " + incidentId + " not recognised.");
    }

    @Override
        public void escalateIncident(int incidentId, int newSeverity) 
                throws IDNotRecognisedException, InvalidSeverityException, IllegalStateException {

            // Finds the incident
            int index = findIncidentIndex(incidentId);
            Incident targetIncident = incidents[index];

            // check if incident is active
            IncidentStatus status = targetIncident.getStatus();
            if (status == IncidentStatus.RESOLVED || status == IncidentStatus.CANCELLED) {
                throw new IllegalStateException("Cannot escalate an incident that is already " + status);
            }

            if (newSeverity < 1 || newSeverity > 10) {
                throw new InvalidSeverityException("Severity must be between 1 and 10.");
            }

            if (newSeverity <= targetIncident.getSeverity()) {
                throw new IllegalStateException("New severity must be higher than the current severity.");
            }

            // State Changes in this case update severity
            targetIncident.setSeverity(newSeverity);
        }

    @Override
        public int[] getIncidentIds() {
            // Create a new array exactly the size of current incident count
            int[] ids = new int[incidentCount];

            // Loop through the internal incidents array
            for (int i = 0; i < incidentCount; i++) {
                // takes the ID and copies it to the new array
                ids[i] = incidents[i].getId();
            }

            return ids;
        }

    @Override
    public String viewIncident(int incidentId) throws IDNotRecognisedException {
        int index = findIncidentIndex(incidentId);
        Incident targetIncident = incidents[index];

        String output = "I#" + targetIncident.getId() + 
                        ", TYPE=" + targetIncident.getType() + 
                        ", STATUS=" + targetIncident.getStatus() + 
                        ", SEVERITY=" + targetIncident.getSeverity() +
                        ", LOC=(" + targetIncident.getX() + "," + targetIncident.getY() + ")";

        for (int i = 0; i < unitCount; i++) {
            Unit u = units[i];
            if (u.getTargetIncidentId() == incidentId) {
                output += ", UNIT=" + u.getId();
                break; 
            }
        }

        return output;
    }



    @Override
        public void dispatch() {
            // Loops through all incidents in order
            for (int i = 0; i < incidentCount; i++) {
                Incident incident = incidents[i];

                // only process incidents that need help
                if (incident.getStatus() == IncidentStatus.REPORTED) {

                    Unit bestUnit = null;
                    int shortestDistance = Integer.MAX_VALUE;

                    // Searches for the required unit
                    for (int j = 0; j < unitCount; j++) {
                        Unit unit = units[j];

                        // Check if it's in idle and the correct type
                        if (unit.getStatus() == UnitStatus.IDLE && isMatchingType(unit.getType(), incident.getType())) {

                            int distance = calculateDistance(unit.getX(), unit.getY(), incident.getX(), incident.getY());

                            if (distance < shortestDistance) {
                                shortestDistance = distance;
                                bestUnit = unit;
                            }
                        }
                    }

                    // State Changes
                    if (bestUnit != null) {
                        bestUnit.setStatus(UnitStatus.EN_ROUTE);
                        incident.setStatus(IncidentStatus.DISPATCHED);

                        // ensures the response unit tracks the required location
                         bestUnit.setTargetIncidentId(incident.getId());
                    }
                }
            }
        }


    // Calculates Manhattan Distance
    private int calculateDistance(int x1, int y1, int x2, int y2) {
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }

    // Matches the emergency type to the vehicle type
    private boolean isMatchingType(UnitType uType, IncidentType iType) {
        if (iType == IncidentType.MEDICAL && uType == UnitType.AMBULANCE) return true;
        if (iType == IncidentType.FIRE && uType == UnitType.FIRE_ENGINE) return true;
        if (iType == IncidentType.CRIME && uType == UnitType.POLICE_CAR) return true;
        return false;
    }

   @Override
    public void tick() {
        for (int i = 0; i < unitCount; i++) {
            Unit unit = units[i];

            // on route
            if (unit.getStatus() == UnitStatus.EN_ROUTE) {
                Incident target = getIncident(unit.getTargetIncidentId());

                if (target != null) {
                    if (unit.getX() != target.getX()) {
                        // Move left or right
                        int newX = unit.getX() < target.getX() ? unit.getX() + 1 : unit.getX() - 1;
                        unit.setX(newX);
                    } 
                    else if (unit.getY() != target.getY()) {
                        // Move up or down
                        int newY = unit.getY() < target.getY() ? unit.getY() + 1 : unit.getY() - 1;
                        unit.setY(newY);
                    }

                    // Check if we arrived this tick
                    if (unit.getX() == target.getX() && unit.getY() == target.getY()) {
                        unit.setStatus(UnitStatus.AT_SCENE);
                        target.setStatus(IncidentStatus.IN_PROGRESS);
                        unit.setTicksAtScene(0); // Start the timer!
                    }
                }
            }

            // at scene 
            else if (unit.getStatus() == UnitStatus.AT_SCENE) {
                Incident target = getIncident(unit.getTargetIncidentId());

                // adds to the timer 
                unit.setTicksAtScene(unit.getTicksAtScene() + 1);

                if (unit.getTicksAtScene() >= unit.getOnSceneDuration()) {
                    // resolved
                    target.setStatus(IncidentStatus.RESOLVED);
                    unit.setStatus(UnitStatus.IDLE);

                    // Clears the target so they are ready for the next one
                    unit.setTargetIncidentId(-1); 
                }
            }

        } 

        // adds a tick once after the loop
        this.tick++; 
    }

        // gets the Incident object from an ID
    private Incident getIncident(int incidentId) {
        try {
            int index = findIncidentIndex(incidentId);
            return incidents[index];
        } catch (IDNotRecognisedException e) {
            return null; 
        }
    }

    @Override
        public String getStatus() {
        StringBuilder sb = new StringBuilder();

        sb.append("City Rescue Status Report\n");
        sb.append("TICK=").append(tick).append("\n");
        sb.append("=========================\n");
    // Loops through all Stations in order
            sb.append("STATIONS (").append(stationCount).append("):\n"); // Changed to caps
            for (int i = 0; i < stationCount; i++) {
                sb.append(stations[i].toString()).append("\n"); 
            }

            // Loops through all Units in order
            sb.append("UNITS (").append(unitCount).append("):\n"); // Changed to caps
            for (int i = 0; i < unitCount; i++) {
                sb.append(units[i].toString()).append("\n");
            }

            // Loops through all Incidents in order
            sb.append("INCIDENTS (").append(incidentCount).append("):\n"); // Changed to caps
            for (int i = 0; i < incidentCount; i++) {
                sb.append(incidents[i].toString()).append("\n");
            }


            // Joins all the lines into one final report to be printed.
            return sb.toString();
        }
    }
