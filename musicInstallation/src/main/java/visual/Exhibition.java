import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class Exhibition extends Pane {

    private Loop loop;
    private MusicGenerator musicGenerator;
    private Entrance entrance;
    private Exit exit;
    private List<Visitor> audience = new ArrayList<>();
    private List<Visitor> exVisitors = new ArrayList<>();

    public Exhibition(MusicGenerator musicGenerator) {
        this.musicGenerator = musicGenerator;
    }

    public Entrance getEntrance() {
        return entrance;
    }

    public Exit getExit() {
        return exit;
    }

    public void setEntrance(Entrance entrance) {
        this.entrance = entrance;
    }

    public void setExit(Exit exit) {
        this.exit = exit;
    }

    public void addVisitor(Visitor visitor) {
        audience.add(visitor);
    }

    public void start() {
        loop = new Loop(this);
        loop.start();
    }

    public List<Visitor> getVisitors() {
        return audience;
    }

    public void addExVisitor(Visitor visitor) {
        exVisitors.add(visitor);
    }

    public void clearVisitors(List<Visitor> visitors) {
        audience.removeAll(visitors);
    }

    public void clearExVisitors() {
        exVisitors.clear();
    }

    public List<Visitor> getExVisitors() {
        return exVisitors;
    }
}
