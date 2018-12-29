package lt.mano.audition.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeoData {
    @SerializedName("type")
    private String mType;
    @SerializedName("bbox")
    private List<Double> mBbox;
    public List<Feature> features;

    public GeoData(String type, List<Double> bbox, List<Feature> features) {
        mType = type;
        mBbox = bbox;
        this.features = features;
    }

    public class Feature {
        @SerializedName("type")
        private String mType;
        public Properties properties;
        public Geometry geometry;

        public Feature(String type, Properties properties, Geometry geometry) {
            mType = type;
            this.properties = properties;
            this.geometry = geometry;
        }
    }

    public class Properties {
        @SerializedName("depth")
        public double depth;
        @SerializedName("id")
        private String mId;
    }

    public class Geometry {
        @SerializedName("type")
        private String mType;
        @SerializedName("bbox")
        private List<Double> mBbox;
        @SerializedName("coordinates")
        public List<List<List<Double>>> coordinates;

        public Geometry(String type, List<Double> bbox, List<List<List<Double>>> coordinates) {
            mType = type;
            mBbox = bbox;
            this.coordinates = coordinates;
        }
    }
}
