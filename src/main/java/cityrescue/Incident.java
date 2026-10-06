

package cityrescue;

import cityrescue.enums.*;

/**
 * Represents an emergency incident reported in the city.
 * Tracks the incident's type, severity, location, and lifecycle status 
 * (e.g., REPORTED, DISPATCHED, IN_PROGRESS, RESOLVED, CANCELLED).
 */
public class Incident {
    
    private int id;
    private IncidentType type;
    private int severity;
    private int x;
    private int y;
    private IncidentStatus status;

    /**
     * Constructs a new emergency incident.
     * @param id The unique integer ID assigned to this incident.
     * @param type The specific type of the emergency (e.g., FIRE, MEDICAL, CRIME).
     * @param severity The severity level of the incident (usually 1-10).
     * @param x The X coordinate where the incident occurred.
     * @param y The Y coordinate where the incident occurred.
     */
    public Incident(int id, IncidentType type, int severity, int x, int y) {
        this.id = id;
        this.type = type;
        this.severity = severity;
        this.x = x;
        this.y = y;
        // All incidents start as REPORTED the moment they are created
        this.status = IncidentStatus.REPORTED; 
    }

    /**
     * Retrieves the unique ID of the incident.
     * @return The incident's ID.
     */
    public int getId() { return id; }
    
    /**
     * Retrieves the type of the incident.
     * @return The incident's type (e.g., MEDICAL).
     */
    public IncidentType getType() { return type; }
    
    /**
     * Retrieves the current severity level of the incident.
     * @return The severity level as an integer.
     */
    public int getSeverity() { return severity; }

    /**
     * Updates the severity level of the incident.
     * @param severity The new severity level.
     */
    public void setSeverity(int severity) { this.severity = severity; }
    
    /**
     * Retrieves the X coordinate of the incident.
     * @return The X coordinate.
     */
    public int getX() { return x; }

    /**
     * Retrieves the Y coordinate of the incident.
     * @return The Y coordinate.
     */
    public int getY() { return y; }
    
    /**
     * Retrieves the current status of the incident in its lifecycle.
     * @return The incident's status (e.g., REPORTED, RESOLVED).
     */
    public IncidentStatus getStatus() { return status; }

    /**
     * Updates the lifecycle status of the incident.
     * @param status The new status to apply.
     */
    public void setStatus(IncidentStatus status) { this.status = status; }

    /**
     * Returns a formatted string representation of the incident's details.
     * @return A string containing the incident's ID, type, status, severity, and location.
     */
    @Override
    public String toString() {
        return "Incident " + id + " (" + type + ") - Status: " + status + ", Severity: " + severity + " at (" + x + "," + y + ")";
    }
}