import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Exhibition exhibition = new Exhibition();
        primaryStage.setTitle("Enotronix");
        primaryStage.setScene(new Scene(exhibition, 1000, 700));
        primaryStage.show();
    }
}
