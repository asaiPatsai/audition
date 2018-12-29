package lt.mano.audition.models;

import java.util.List;

public class Coordinates {
    public double latitude;
    public double longitude;
    public double depth;

    public Coordinates(List<Double> coordinates) {
        latitude = coordinates.get(1);
        longitude = coordinates.get(0);
        depth = coordinates.get(2);
    }
}
