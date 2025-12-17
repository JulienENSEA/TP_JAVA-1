import TPjava.Aeroport;
import TPjava.World;

public class Main {
    public static void main(String[] args) {
        World w = new World("./data/airport-codes_no_comma.csv");
        System.out.println("Found " + w.getList().size() + " airports.");

        Aeroport parisNearest = w.findNearestAirport(2.316, 48.866);
        System.out.println("Nearest from Paris coords: " + parisNearest);

        Aeroport cdg = w.findByCode("CDG");
        if (cdg != null) {
            double d = w.distance(2.316, 48.866, cdg.getLongitude(), cdg.getLatitude());
            System.out.println("Distance Paris -> CDG: " + d + " km");
        }
    }
}
