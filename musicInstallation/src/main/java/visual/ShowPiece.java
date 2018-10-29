package visual;

import javafx.scene.image.Image;

public class ShowPiece extends Target {

    public ShowPiece(double x, double y, Exhibition exhibition) {
        super(exhibition);
        setX(x);
        setY(y);
        setImage(new Image("thinker1.png"));
    }
}
