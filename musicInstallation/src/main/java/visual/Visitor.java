package visual;

import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class Visitor extends Entity {

    private boolean waiting = false;
    private final float waitingLimit = 180;
    private float waitingTimer = 0;
    private Target target;
    private List<Target> targets = new ArrayList<>(exhibition.getShowPieces());

    public Visitor(double x, double y, Exhibition exhibition) {
        super(exhibition);
        setX(x);
        setY(y);
        setImage(new Image("visitor2.png"));
        exhibition.addVisitor(this);
        targets.add(exhibition.getExit());
        target = targets.get(0);
    }

    void step() {
        if (waiting) {
            waitingTimer += 1;
            if (waitingTimer >= waitingLimit) {
                waiting = false;
            }
        } else {
            setX(target.getX() > getX() ? getX() + 0.7 : getX() - 0.7);
            setY(target.getY() > getY() ? getY() + 0.7 : getY() - 0.7);
            setRotate(getAngle());
            if (getBoundsInParent().intersects(target.getBoundsInParent())) {
                if (target instanceof Exit) {
                    destroy();
                } else {
                    targets.remove(target);
                    target = targets.get(0);
                    waiting = true;
                    waitingTimer = 0;
                }
            }
        }
    }

    private void destroy() {
        exhibition.getChildren().remove(this);
        exhibition.addExVisitor(this);
    }

    private double getAngle() {
        double angle = Math.toDegrees(Math.atan2(target.getY() - getY(), target.getX() - getX()));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }
}
