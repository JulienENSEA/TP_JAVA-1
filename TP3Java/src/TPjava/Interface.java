package TPjava;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.PerspectiveCamera;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.net.URI;
import java.net.http.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class Interface extends Application {

    private World w;
    private Earth earth;

    private PerspectiveCamera camera;

    private double lastX, lastY;

    private final java.util.ArrayList<Flight> listOfFlight = new java.util.ArrayList<>();
    private final java.util.ArrayList<javafx.scene.shape.Sphere> yellowMarkers = new java.util.ArrayList<>();

    private static final String API_KEY = "a9aa1d52d49a8a6f1716bf4c89ef6ae4";


    @Override
    public void start(Stage primaryStage) {
        w = new World("./data/airport-codes_no_comma.csv");
        earth = new Earth(w);

        Scene scene = new Scene(earth, 900, 650, true);
        scene.setFill(Color.WHITE);

        camera = new PerspectiveCamera(true);
        camera.setTranslateZ(-1000);
        camera.setNearClip(0.1);
        camera.setFarClip(2000.0);
        camera.setFieldOfView(35);
        scene.setCamera(camera);

        installMouseHandlers(scene);

        primaryStage.setTitle("Catch me if you can!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void installMouseHandlers(Scene scene) {

        scene.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                lastX = event.getSceneX();
                lastY = event.getSceneY();
            }
        });

        scene.addEventHandler(MouseEvent.MOUSE_DRAGGED, event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                double dx = event.getSceneX() - lastX;
                double dy = event.getSceneY() - lastY;
                lastX = event.getSceneX();
                lastY = event.getSceneY();
                earth.rotateBy(dx, dy);
            }
        });

        scene.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.SECONDARY) {
                PickResult pick = event.getPickResult();
                if (pick != null && pick.getIntersectedNode() != null) {
                    Point2D uv = pick.getIntersectedTexCoord();
                    if (uv != null) {
                        double u = uv.getX();
                        double v = uv.getY();

                        double lat = 180.0 * (0.5 - v);
                        double lon = 360.0 * (u - 0.5);

                        Aeroport nearest = w.findNearestAirport(lon, lat);
                        System.out.println("Clic droit → lon=" + lon + ", lat=" + lat);
                        System.out.println("Aéroport le plus proche : " + nearest);
                        loadLiveFlightsAndDisplay(nearest);
                        earth.displayRedSphere(nearest);
                    }
                }
            }
        });

        // Zoom caméra molette
        scene.setOnScroll(e -> camera.setTranslateZ(camera.getTranslateZ() + e.getDeltaY()));
    }

    private String fetchFlightsJsonArrivals(String arrIata) throws Exception {
        String url = "http://api.aviationstack.com/v1/flights?access_key="
                + API_KEY
                + "&arr_iata=" + URLEncoder.encode(arrIata, StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest req = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        return client.send(req, HttpResponse.BodyHandlers.ofString()).body();
    }

    private void loadLiveFlightsAndDisplay(Aeroport arrivalAirport) {
        new Thread(() -> {
            try {
                String json = fetchFlightsJsonArrivals(arrivalAirport.getIATA());
                JsonFlightFiller filler = new JsonFlightFiller(json, w);

                java.util.ArrayList<Flight> flights = filler.getList();

                Platform.runLater(() -> {
                    listOfFlight.clear();
                    listOfFlight.addAll(flights);

                    // Nettoie anciens points jaunes
                    earth.getChildren().removeAll(yellowMarkers);
                    yellowMarkers.clear();

                    // Affiche départ + arrivée en jaune (tu peux n’en garder qu’un des deux)
                    for (Flight f : flights) {
                        if (f.getDeparture() != null) {
                            var s = earth.createSphere(f.getDeparture(), Color.YELLOW);
                            yellowMarkers.add(s);
                            earth.getChildren().add(s);
                        }
                        if (f.getArrival() != null) {
                            var s = earth.createSphere(f.getArrival(), Color.YELLOW);
                            yellowMarkers.add(s);
                            earth.getChildren().add(s);
                        }
                    }
                    System.out.println("Flights loaded: " + flights.size());
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
