package TPjava;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class World {
    private final ArrayList<Aeroport> list = new ArrayList<>();

    public World(String fileName) {
        try (BufferedReader buf = new BufferedReader(new FileReader(fileName))) {
            String s = buf.readLine();
            while (s != null) {

                s = s.replace("\"", "");

                String[] fields = s.split(",", -1);

                String type = safe(fields, 1);

                if (!"large_airport".equals(type)) {
                    s = buf.readLine();
                    continue;
                }

                String name = safe(fields, 2);
                String country = safe(fields, 5);
                String iata = safe(fields, 9);

                double lon = Double.NaN, lat = Double.NaN;
                double[] lonLat = parseLonLatFromLine(s);
                lon = lonLat[0];
                lat = lonLat[1];

                if (!Double.isNaN(lat) && !Double.isNaN(lon) && !iata.isBlank()) {
                    list.add(new Aeroport(iata, name, country, lat, lon));
                }

                s = buf.readLine();
            }
        } catch (Exception e) {
            System.out.println("Maybe the file isn't there ?");
            e.printStackTrace();
        }
    }

    public List<Aeroport> getList() { return list; }

    public Aeroport findByCode(String code) {
        if (code == null) return null;
        String c = code.trim().toUpperCase();
        for (Aeroport a : list) {
            if (a.getIATA().equalsIgnoreCase(c)) return a;
        }
        return null;
    }

    public Aeroport findNearestAirport(double longitude, double latitude) {
        Aeroport best = null;
        double bestD = Double.POSITIVE_INFINITY;
        for (Aeroport a : list) {
            double d = Aeroport.haversineKm(latitude, longitude, a.getLatitude(), a.getLongitude());
            if (d < bestD) {
                bestD = d;
                best = a;
            }
        }
        return best;
    }

    public double distance(double lon1, double lat1, double lon2, double lat2) {
        return Aeroport.haversineKm(lat1, lon1, lat2, lon2);
    }

    private static String safe(String[] f, int idx) {
        if (idx < 0 || idx >= f.length) return "";
        return f[idx] == null ? "" : f[idx].trim();
    }

    private static double[] parseLonLatFromLine(String s) {
        String cleaned = s.replace("\"", " ");
        String[] parts = cleaned.split(",");
        Double last = null, secondLast = null;
        for (String p : parts) {
            try {
                double v = Double.parseDouble(p.trim());
                secondLast = last;
                last = v;
            } catch (Exception ignored) {}
        }
        double lon = secondLast == null ? Double.NaN : secondLast;
        double lat = last == null ? Double.NaN : last;
        return new double[]{lon, lat};
    }
}

