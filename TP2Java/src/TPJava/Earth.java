package TPJava;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;

public class Earth extends Group {

    private final Sphere globe;
    private final double R = 300.0;                 // rayon de la sphère
    private final Rotate rotY = new Rotate(0, Rotate.Y_AXIS);

    public Earth(String texturePath) {
        globe = new Sphere(R);

        // Texture de la Terre
        PhongMaterial mat = new PhongMaterial();
        mat.setDiffuseMap(new Image("file:" + texturePath));
        globe.setMaterial(mat);

        // Rotation autour de Y
        globe.getTransforms().add(rotY);

        getChildren().add(globe);

        // Animation : 1 tour en ~15 secondes
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double t = now / 1_000_000_000.0;      // secondes
                double angle = (t / 15.0) * 360.0;     // 15s -> 360°
                rotY.setAngle(angle % 360.0);
            }
        };
        timer.start();
    }

    /** Retourne (u,v) si on a cliqué sur la sphère, sinon null. */
    public Point2D getUV(PickResult pr) {
        if (pr == null) return null;
        if (pr.getIntersectedNode() != globe) return null;
        return pr.getIntersectedTexCoord(); // u,v ∈ [0..1]
    }

    /** Affiche une petite sphère rouge à l'emplacement d'un aéroport. */
    public void displayRedSphere(Aeroport a) {
        if (a == null) return;

        Sphere mark = new Sphere(3);
        mark.setMaterial(new PhongMaterial(Color.RED));

        double[] p = latLonToXYZ(a.getLatitude(), a.getLongitude());
        mark.setTranslateX(p[0]);
        mark.setTranslateY(p[1]);
        mark.setTranslateZ(p[2]);

        getChildren().add(mark);
    }

    /** Conversion latitude/longitude (degrés) -> position 3D sur la sphère. */
    private double[] latLonToXYZ(double latDeg, double lonDeg) {
        double theta = Math.toRadians(latDeg); // latitude
        double phi   = Math.toRadians(lonDeg); // longitude

        double x = R * Math.cos(theta) * Math.sin(phi);
        double y = -R * Math.sin(theta);
        double z = -R * Math.cos(theta) * Math.cos(phi);

        return new double[]{x, y, z};
    }
}