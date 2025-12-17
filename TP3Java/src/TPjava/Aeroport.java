package TPjava;

public class Aeroport {
    private final String iata;
    private final String name;
    private final String country;
    private final double latitude;   // Θ
    private final double longitude;  // Φ

    public Aeroport(String iata, String name, String country, double latitude, double longitude) {
        this.iata = iata == null ? "" : iata.trim();
        this.name = name == null ? "" : name.trim();
        this.country = country == null ? "" : country.trim();
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getIATA() { return iata; }
    public String getName() { return name; }
    public String getCountry() { return country; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    /** Distance (km) via Haversine (robuste). */
    public double calculDistance(Aeroport other) {
        return haversineKm(this.latitude, this.longitude, other.latitude, other.longitude);
    }

    public static double haversineKm(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371.0; // km
        double phi1 = Math.toRadians(lat1);
        double phi2 = Math.toRadians(lat2);
        double dPhi = Math.toRadians(lat2 - lat1);
        double dLam = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dPhi/2) * Math.sin(dPhi/2)
                + Math.cos(phi1) * Math.cos(phi2) * Math.sin(dLam/2) * Math.sin(dLam/2);
        double c = 2.0 * Math.atan2(Math.sqrt(a), Math.sqrt(1.0 - a));
        return R * c;
    }

    @Override
    public String toString() {
        return "Aeroport{" +
                "iata='" + iata + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", lat=" + latitude +
                ", lon=" + longitude +
                '}';
    }
}
