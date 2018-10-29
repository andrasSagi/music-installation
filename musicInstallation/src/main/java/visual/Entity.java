package visual;

import javafx.scene.image.ImageView;

abstract class Entity extends ImageView {

    Exhibition exhibition;

    Entity(Exhibition exhibition) {
        this.exhibition = exhibition;
        exhibition.getChildren().add(this);
    }
}
