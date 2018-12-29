package gui;

import com.sun.org.apache.bcel.internal.generic.IADD;
import creature.Creature;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import battle.*;

import java.io.IOException;


public class Main extends Application {

    private static double wdWidth = 1000, wdHeight = 750;
    private MainController controller;

    public void start(Stage primaryStage) {
        try {
            // FXMLLoader.load ... static method:can't get controller
//            Parent root = FXMLLoader.load(getClass().getResource("MainWindow.fxml"));
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
            Parent root = fxmlLoader.load();
            controller = fxmlLoader.getController();
//            Bounds bounds = root.getLayoutBounds(); // at run time...all zero?
//            wdHeight = bounds.getHeight();
//            wdWidth = bounds.getWidth();

            Scene scene = new Scene(root, wdWidth, wdHeight);
            controller.init(scene, wdHeight, wdWidth);
            primaryStage.setTitle("Calabash Battle!");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            controller.showBattleField();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
