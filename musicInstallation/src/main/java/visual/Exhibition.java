package visual;

import javafx.scene.layout.Pane;
import music.BeadsGenerator;

import java.util.ArrayList;
import java.util.List;

public class Exhibition extends Pane {

    private double actualWidth = 1000;
    private double actualHeight = 700;
    private Loop loop;
    private BeadsGenerator musicGenerator;
    private Entrance entrance;
    private Exit exit;
    private List<Visitor> audience = new ArrayList<>();
    private List<Visitor> exVisitors = new ArrayList<>();
    private List<ShowPiece> showPieces = new ArrayList<>();

    public Exhibition(BeadsGenerator musicGenerator) {
        this.musicGenerator = musicGenerator;
    }

    public Entrance getEntrance() {
        return entrance;
    }

    Exit getExit() {
        return exit;
    }

    public void setEntrance(Entrance entrance) {
        this.entrance = entrance;
    }

    public void setExit(Exit exit) {
        this.exit = exit;
    }

    void addVisitor(Visitor visitor) {
        audience.add(visitor);
    }

    public void start() {
        loop = new Loop(this);
        loop.start();
    }

    List<Visitor> getVisitors() {
        return audience;
    }

    void addExVisitor(Visitor visitor) {
        exVisitors.add(visitor);
    }

    void clearVisitors(List<Visitor> visitors) {
        audience.removeAll(visitors);
    }

    void clearExVisitors() {
        exVisitors.clear();
    }

    List<Visitor> getExVisitors() {
        return exVisitors;
    }

    public void addShowPiece(ShowPiece showPiece) {
        showPieces.add(showPiece);
    }

    List<ShowPiece> getShowPieces() {
        return showPieces;
    }

    public double getActualWidth() {
        return actualWidth;
    }

    public double getActualHeight() {
        return actualHeight;
    }
}
