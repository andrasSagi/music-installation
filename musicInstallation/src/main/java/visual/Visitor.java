import javafx.scene.image.Image;

public class Visitor extends Entity {

    private Entity target = exhibition.getExit();

    public Visitor(double x, double y, Exhibition exhibition) {
        super(exhibition);
        setX(x);
        setY(y);
        setImage(new Image("visitor2.png"));
        exhibition.addVisitor(this);
    }

    public void step() {
        setX(target.getX() > getX() ? getX() + 0.7 : getX() - 0.7);
        setY(target.getY() > getY() ? getY() + 0.7 : getY() - 0.7);
        setRotate(getAngle());
        if (getBoundsInParent().intersects(target.getBoundsInParent())) {
            destroy();
        }
    }

    public void destroy() {
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
