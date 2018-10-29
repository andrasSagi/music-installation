package visual;

import javafx.scene.image.Image;

public class Entrance extends Entity {

    public Entrance(double x, double y, Exhibition exhibition) {
        super(exhibition);
        setX(x);
        setY(y);
        setImage(new Image("entrance.png"));
    }
}
