package visual;

import javafx.scene.image.Image;
import music.OscillatorController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Visitor extends Entity {

    private OscillatorController controller;
    private boolean waiting = false;
    private final float waitingLimit = 600;
    private float waitingTimer = 0;
    private Target target;
    private List<Target> targets = new ArrayList<>(exhibition.getShowPieces());
    private Random random;

    public Visitor(double x, double y, Exhibition exhibition, OscillatorController oscillatorController) {
        super(exhibition);
        setX(x);
        setY(y);
        setImage(new Image("visitor2.png"));
        exhibition.addVisitor(this);
        controller = oscillatorController;
        targets.add(exhibition.getExit());
        random = new Random();
        target = targets.get(random.nextInt(targets.size()));
    }

    void step() {
        if (waiting) {
            waitingTimer += 1;
            if (waitingTimer >= waitingLimit) {
                waiting = false;
            }
        } else {
            setX(target.getX() > getX() ? getX() + 0.3 : getX() - 0.3);
            setY(target.getY() > getY() ? getY() + 0.3 : getY() - 0.3);
            setRotate(getAngle());
            double entranceDistance = getDistanceFrom(exhibition.getEntrance());
            double exitDistance = getDistanceFrom(exhibition.getExit());
            double average = (exitDistance + entranceDistance) / 2;
            double min = entranceDistance < exitDistance ? entranceDistance : exitDistance;
            double value = (min * 100.0f) / average;
            controller.getGlide().setValue((float) value / 100 - controller.getBeadsGenerator().getOscillatorNumber() * 5);
            if (getBoundsInParent().intersects(target.getBoundsInParent())) {
                if (target instanceof Exit) {
                    destroy();
                } else {
                    targets.remove(target);
                    target = targets.get(random.nextInt(targets.size()));
                    waiting = true;
                    waitingTimer = 0;
                }
            }
        }
    }

    private double getDistanceFrom(Entity entity) {
        double a = Math.abs(getX() - entity.getX());
        double b = Math.abs(getY() - entity.getY());
        return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

    private void destroy() {
        exhibition.getChildren().remove(this);
        exhibition.addExVisitor(this);
        controller.removeOscillator();
    }

    private double getAngle() {
        double angle = Math.toDegrees(Math.atan2(target.getY() - getY(), target.getX() - getX()));

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }
}
