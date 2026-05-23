package unimagdalena.edu.omht.projections;

import java.util.UUID;

public interface OfficeOccupancyProjection {
    UUID getOfficeId();
    String getLocation();
    Long getOccupiedMinutes();
}
