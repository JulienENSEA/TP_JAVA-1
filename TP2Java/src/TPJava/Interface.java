package TPJava;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.scene.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.PickResult;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Interface extends Application {

    private World world;
    private Earth earth;

    @Override
    public void start(Stage stage) {
        stage.setTitle("DataFlight - Part 2");

        // 1) Charger les aéroports
        world = new World("data/airport-codes_no_comma.csv");

        // 2) Créer la Terre
        earth = new Earth("data/earth.png"); // <-- adapte le nom si besoin

        Pane root = new Pane(earth);

        // depthBuffer=true pour la 3D
        Scene scene = new Scene(root, 1000, 700, true);

        // 3) Caméra 3D
        PerspectiveCamera cam = new PerspectiveCamera(true);
        cam.setTranslateZ(-900);
        cam.setNearClip(0.1);
        cam.setFarClip(5000);
        scene.setCamera(cam);

        // 4) Zoom molette
        scene.addEventFilter(ScrollEvent.SCROLL, e -> {
            cam.setTranslateZ(cam.getTranslateZ() + (-e.getDeltaY()));
        });

        // 5) Clic droit = picking UV -> lat/lon -> nearest airport -> boule rouge
        scene.setOnMouseClicked(e -> {
            if (e.getButton() != MouseButton.SECONDARY) return;

            PickResult pr = e.getPickResult();
            Point2D uv = earth.getUV(pr);
            if (uv == null) return;

            double u = uv.getX();
            double v = uv.getY();

            // Formules du sujet
            double lat = 180.0 * (0.5 - v);
            double lon = 360.0 * (u - 0.5);

            Aeroport nearest = world.findNearestAirport(lon, lat);
            System.out.println("Clicked lat/lon = (" + lat + ", " + lon + ")");
            System.out.println("Nearest airport = " + nearest);

            Platform.runLater(() -> earth.displayRedSphere(nearest));
        });

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}