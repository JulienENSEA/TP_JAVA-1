package TPjava;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class Earth extends Group {

    private static final double R = 300.0;
    private final World world;

    private final Sphere earthSphere;

    // Rotations appliquées à la Terre
    private final Rotate rx = new Rotate(0, Rotate.X_AXIS);
    private final Rotate ry = new Rotate(0, Rotate.Y_AXIS);

    // Offsets contrôlés par l'utilisateur (drag)
    private double userRotX = 0.0;
    private double userRotY = 0.0;

    private Sphere lastRed;

    public Earth(World w) {
        this.world = w;

        earthSphere = new Sphere(R);

        PhongMaterial mat = new PhongMaterial();
        mat.setDiffuseMap(new Image("file:./data/earth.png"));
        earthSphere.setMaterial(mat);

        // IMPORTANT: on applique les rotations à la sphère Terre
        this.getTransforms().addAll(rx, ry);

        this.getChildren().add(earthSphere);

    }

    /** Appelé par l’Interface quand l’utilisateur drag avec clic gauche. */
    public void rotateBy(double deltaX, double deltaY) {
        // Ajuste ces facteurs si tu veux plus/moins sensible
        userRotY += deltaX * 0.3;  // tourner autour de Y quand on va gauche/droite
        userRotX -= deltaY * 0.3;  // tourner autour de X quand on va haut/bas

        // Optionnel: limiter pour éviter de retourner complètement en X
        userRotX = clamp(userRotX, -90, 90);

        ry.setAngle(userRotY);
        rx.setAngle(userRotX);
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    public Sphere createSphere(Aeroport a, Color color) {
        double lonDeg = a.getLongitude();
        double latDeg = a.getLatitude();

        double lon = Math.toRadians(lonDeg);
        double lat = Math.toRadians(latDeg);

        double r = R + 1.5;
        double x = r * Math.cos(lat) * Math.sin(lon);
        double y = -r * Math.sin(lat);
        double z = -r * Math.cos(lat) * Math.cos(lon);

        Sphere s = new Sphere(2);
        s.setMaterial(new PhongMaterial(color));
        s.setTranslateX(x);
        s.setTranslateY(y);
        s.setTranslateZ(z);

        return s;
    }

    public void displayRedSphere(Aeroport a) {
        if (lastRed != null) this.getChildren().remove(lastRed);
        lastRed = createSphere(a, Color.RED);
        this.getChildren().add(lastRed);
    }
}

