package TP1;

public class Aeroport {

    // --- Attributs ---
    private String IATA;
    private String name;
    private String country;
    private double latitude;
    private double longitude;

    // --- Constructeurs ---
    public Aeroport(String IATA, String name, String country, double latitude, double longitude) {
        this.IATA = IATA;
        this.name = name;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Aeroport() {}

    // --- Getters ---
    public String getIATA() {
        return IATA;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

/*
 * 
 * Fonction de calcul de la distance
 * 
 */
    public double calculDistance(Aeroport a) {
        double theta1 = Math.toRadians(this.latitude);
        double phi1 = Math.toRadians(this.longitude);
        double theta2 = Math.toRadians(a.latitude);
        double phi2 = Math.toRadians(a.longitude);

        // norme = (Θ2 − Θ1)^2 + ((Φ2 − Φ1) * cos((Θ2 + Θ1)/2))^2
        double norme = Math.pow(theta2 - theta1, 2)
                + Math.pow((phi2 - phi1) * Math.cos((theta2 + theta1) / 2), 2);

        return norme;
    }

/*
 * 
 * Override de la fonction toString
 * 
 */
    @Override
    public String toString() {
        return "Aeroport{" +
                "IATA='" + IATA + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }

/*
    public static void main(String[] args) {
        Aeroport a = new Aeroport("CDG", "Charles de Gaulle", "France", 49.0097, 2.5479);
        System.out.println(a);
    }
        */

}


