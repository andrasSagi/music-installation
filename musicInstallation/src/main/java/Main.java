import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import music.BeadsGenerator;
import music.OscillatorController;
import visual.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BeadsGenerator beadsGenerator = new BeadsGenerator();
        final Exhibition exhibition = new Exhibition(beadsGenerator);
        Button addVisitor = new Button("Create visitor");
        exhibition.getChildren().add(addVisitor);
        exhibition.setEntrance(new Entrance(400, 0, exhibition));
        exhibition.setExit(new Exit(800, 550, exhibition));
        exhibition.addShowPiece(new ShowPiece(300, 300, exhibition));
        exhibition.addShowPiece(new ShowPiece(100, 500, exhibition));
        exhibition.addShowPiece(new ShowPiece(700, 100, exhibition));
        exhibition.addShowPiece(new ShowPiece(100, 150, exhibition));
        exhibition.addShowPiece(new ShowPiece(650, 420, exhibition));
        addVisitor.setOnAction(e -> {
            new Visitor(exhibition.getEntrance().getX(), exhibition.getEntrance().getY(), exhibition, new OscillatorController(beadsGenerator));

        });
        primaryStage.setTitle("Enotronix");
        primaryStage.setScene(new Scene(exhibition, 1000, 700));
        primaryStage.show();
        beadsGenerator.start();
        exhibition.start();
    }
}
