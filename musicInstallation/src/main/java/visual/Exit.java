import javafx.scene.image.Image;
import javafx.scene.layout.Pane;

public class Exit extends Entity {

    public Exit(double x, double y, Exhibition exhibition) {
        super(exhibition);
        setX(x);
        setY(y);
        setImage(new Image("exit.png"));
    }
}
