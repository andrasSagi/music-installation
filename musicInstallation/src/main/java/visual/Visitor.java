package visual;

import javafx.scene.image.Image;
import music.OscillatorController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Visitor extends Entity {

    private OscillatorController controller;
    private final static float waitingLimit = 2000;
    private float waitingTimer = 0;
    private Target target;
    private List<Target> targets = new ArrayList<>(exhibition.getShowPieces());
    private Random random;
    private double xMiddle;
    private String status;
    private List<Visitor> others = new ArrayList<>();

    public Visitor(Exhibition exhibition, OscillatorController oscillatorController) {
        super(exhibition);
        setX(exhibition.getEntrance().getX());
        setY(exhibition.getEntrance().getY());
        setImage(new Image("visitor2.png"));
        exhibition.addVisitor(this);
        controller = oscillatorController;
        targets.add(exhibition.getExit());
        xMiddle = exhibition.getActualWidth() / 2;
        random = new Random();
        setTarget();
        status = "moving";
    }

    private void setTarget() {
        double gauss = random.nextGaussian() + Math.round(targets.size() / 2);
        if (gauss < 0) {
            target = targets.get(0);
        } else if (gauss > targets.size() - 1) {
            target = targets.get(targets.size() - 1);
        } else {
            target = targets.get((int) Math.round(gauss));
        }
    }

    void step() {
        setRotate(getAngle());
        for (Visitor visitor: exhibition.getVisitors()) {
            if (visitor.equals(this))
                continue;

            if (getBoundsInParent().intersects(visitor.getBoundsInParent())
                    && !getBoundsInParent().intersects(exhibition.getEntrance().getBoundsInParent())) {
                evade(visitor);
            }
        }
        if (status.equals("moving")) {
            for (Visitor visitor: exhibition.getVisitors()) {
                visitor.others.remove(this);
            }
            controller.getPanner().setPos((float) getPositionOnX());
            setX(target.getX() > getX() ? getX() + 0.3 : getX() - 0.3);
            setY(target.getY() > getY() ? getY() + 0.3 : getY() - 0.3);
            double entranceDistance = getDistanceFrom(exhibition.getEntrance());
            double exitDistance = getDistanceFrom(exhibition.getExit());
            double average = (exitDistance + entranceDistance) / 2;
            double min = entranceDistance < exitDistance ? entranceDistance : exitDistance;
            double value = (min * 100.0f) / average;
            controller.getGlide().setValue((float) value / 100);
            if (getBoundsInParent().intersects(target.getBoundsInParent())) {
                if (target instanceof Exit) {
                    destroy();
                } else if (target instanceof ShowPiece) {
                    others.addAll(((ShowPiece) target).getActualWatchers());
                    ((ShowPiece) target).addWatcher(this);
                    status = "waiting";
                    waitingTimer = 0;
                }
            }
        } else if (status.equals("waiting")) {
            waitingTimer += 1;
            if (waitingTimer >= waitingLimit) {
                status = "moving";
                targets.remove(target);
                ((ShowPiece) target).removeWatcher(this);
                setTarget();
            }
        }
    }

    private void evade(Visitor visitor) {
        double originalDistance = getDistanceFrom(target);
        setX(visitor.getX() > getX() ? getX() - 0.1 : getX() + 0.1);
        setY(visitor.getY() > getY() ? getY() - 0.1 : getY() + 0.1);
        double newDistance = getDistanceFrom(target);
        if (newDistance > originalDistance) {
            setX(target.getX() > getX() ? getX() + 0.1 : getX() - 0.1);
            setY(target.getY() > getY() ? getY() + 0.1 : getY() - 0.1);
        } else if (newDistance < originalDistance) {
            setX(target.getX() > getX() ? getX() - 0.1 : getX() + 0.1);
            setY(target.getY() > getY() ? getY() - 0.1 : getY() + 0.1);
        }
    }

    private double getPositionOnX() {
        double distance = getX() - xMiddle;
        return distance / (xMiddle / 100) / 100;
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

    private Target getTarget() {
        return target;
    }
}
