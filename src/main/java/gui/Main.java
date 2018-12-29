package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class Main extends Application {

    private static double wdWidth = 1000, wdHeight = 750;

    public void start(Stage primaryStage) {
        try {
            // FXMLLoader.load ... static method:can't get controller
//            Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
            Parent root = fxmlLoader.load();
            MainController controller = fxmlLoader.getController();
//            Bounds bounds = root.getLayoutBounds(); // at run time...all zero?
//            wdHeight = bounds.getHeight();
//            wdWidth = bounds.getWidth();

            Scene scene = new Scene(root, wdWidth, wdHeight);
            controller.init(primaryStage, scene, wdHeight, wdWidth);
            primaryStage.setTitle("Calabash Battle!");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

//            controller.showBattleField();

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
