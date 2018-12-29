package lt.mano.audition.models;

public class ScanDataItem {
    public int id;
    public double latitude;
    public double longitude;
    public String name;
    public String date;
    public int scanPoints;
    public int mode;
    public boolean hasBathymetry;

    public ScanDataItem() {}

    public ScanDataItem(int id, double latitude, double longitude, String name, String date,
            int scanPoints, int mode, boolean hasBathymetry) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
        this.date = date;
        this.scanPoints = scanPoints;
        this.mode = mode;
        this.hasBathymetry = hasBathymetry;
    }
}
