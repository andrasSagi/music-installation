import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import music.BeadsGenerator;
import music.MusicGenerator;
import visual.*;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        MusicGenerator beadsGenerator = new BeadsGenerator();
        final Exhibition exhibition = new Exhibition(beadsGenerator);
        Button addVisitor = new Button("Create visitor");
        exhibition.getChildren().add(addVisitor);
        exhibition.setEntrance(new Entrance(400, 0, exhibition));
        exhibition.setExit(new Exit(800, 550, exhibition));
        exhibition.addShowPiece(new ShowPiece(300, 300, exhibition));
        addVisitor.setOnAction(e -> {
            new Visitor(exhibition.getEntrance().getX(), exhibition.getEntrance().getY(), exhibition);

        });
        primaryStage.setTitle("Enotronix");
        primaryStage.setScene(new Scene(exhibition, 1000, 700));
        primaryStage.show();
        exhibition.start();
    }
}
