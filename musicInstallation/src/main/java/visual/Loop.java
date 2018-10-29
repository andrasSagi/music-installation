package visual;

import javafx.animation.AnimationTimer;

public class Loop extends AnimationTimer {

    private Exhibition exhibition;

    Loop(Exhibition exhibition) {
        this.exhibition = exhibition;
    }

    @Override
    public void handle(long now) {
        for (Visitor visitor: exhibition.getVisitors()) {
            visitor.step();
        }
        exhibition.clearVisitors(exhibition.getExVisitors());
        exhibition.clearExVisitors();
    }
}
