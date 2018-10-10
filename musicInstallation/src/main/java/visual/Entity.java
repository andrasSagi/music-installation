import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

public abstract class Entity extends ImageView {

    protected Exhibition exhibition;

    public Entity(Exhibition exhibition) {
        this.exhibition = exhibition;
        exhibition.getChildren().add(this);
    }
}
