package TPjava;

import javax.json.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.ArrayList;

public class JsonFlightFiller {

    private final ArrayList<Flight> list = new ArrayList<>();

    public ArrayList<Flight> getList() {
        return list;
    }

    public JsonFlightFiller(String jsonString, World w) {
        try {
            InputStream is = new ByteArrayInputStream(jsonString.getBytes(StandardCharsets.UTF_8));
            JsonReader rdr = Json.createReader(is);
            JsonObject obj = rdr.readObject();
            JsonArray results = obj.getJsonArray("data");

            if (results == null) return;

            for (JsonObject result : results.getValuesAs(JsonObject.class)) {
                try {
                    JsonObject dep = result.getJsonObject("departure");
                    JsonObject arr = result.getJsonObject("arrival");
                    JsonObject airline = result.getJsonObject("airline");
                    JsonObject flight = result.getJsonObject("flight");

                    String depIata = getString(dep, "iata");
                    String arrIata = getString(arr, "iata");
                    if (depIata == null || arrIata == null) continue;

                    Aeroport depAirport = w.findByCode(depIata);
                    Aeroport arrAirport = w.findByCode(arrIata);
                    if (depAirport == null || arrAirport == null) continue;

                    Flight f = new Flight(depAirport, arrAirport);

                    f.setAirlineName(getString(airline, "name"));
                    f.setAirLineCode(getString(airline, "iata"));

                    f.setFlightIata(getString(flight, "iata"));
                    f.setNumber(parseIntSafe(getString(flight, "number")));

                    f.setDepartureTime(parseDate(getString(dep, "estimated"), getString(dep, "scheduled")));
                    f.setArrivalTime(parseDate(getString(arr, "estimated"), getString(arr, "scheduled")));

                    list.add(f);

                } catch (Exception ignoredOne) {
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getString(JsonObject o, String key) {
        if (o == null || !o.containsKey(key) || o.isNull(key)) return null;
        JsonValue v = o.get(key);
        return (v.getValueType() == JsonValue.ValueType.STRING) ? ((JsonString) v).getString() : null;
    }

    private static int parseIntSafe(String s) {
        if (s == null) return 0;
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }

    private static LocalDateTime parseDate(String preferred, String fallback) {
        String s = (preferred != null && !preferred.isBlank()) ? preferred : fallback;
        if (s == null || s.isBlank()) return null;
        try { return OffsetDateTime.parse(s).toLocalDateTime(); } catch (Exception e) { return null; }
    }
}

