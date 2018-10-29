package visual;

import javafx.scene.image.Image;

public class Exit extends Target {

    public Exit(double x, double y, Exhibition exhibition) {
        super(exhibition);
        setX(x);
        setY(y);
        setImage(new Image("exit.png"));
    }
}
