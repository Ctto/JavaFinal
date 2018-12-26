package gui;

import com.sun.org.apache.bcel.internal.generic.IADD;
import creature.Creature;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import battle.*;


public class Main extends Application {
    private static Battle battle;
    static {
        battle = new Battle();
        battle.battlePrepare();
        battle.setVlQueueFormation(Formation.ARROW);
    }

    private static double fieldWidth, fieldHeight, imageSz;
    private static final int fieldRowNum, fieldColNum;
    private static double startPtX, startPtY = 20;
    static {
        fieldColNum = battle.getFieldColNum();
        fieldRowNum = battle.getFieldRowNum();
    }


    public void start(Stage primaryStage) {
//        StackPane root = new StackPane();
//        StackPane pane = new StackPane();
//        BackgroundImage backgroundImage= new BackgroundImage(
//                new Image(Main.class.getResourceAsStream("./res/background.jpg")),
//                BackgroundRepeat.REPEAT,
//                BackgroundRepeat.NO_REPEAT,
//                BackgroundPosition.CENTER,
//                BackgroundSize.DEFAULT);
//        root.setBackground(new Background(backgroundImage));
//        pane.setBackground(new Background(backgroundImage));

        Image image = new Image(Main.class.getResourceAsStream("./res/background.jpg"));
        fieldWidth = image.getWidth();
        fieldHeight = image.getHeight();
        imageSz = (fieldHeight - 2*startPtY)/fieldRowNum;
        startPtX = (fieldWidth - fieldColNum * imageSz)/2;
        ImageView imageView = new ImageView(image);
        Group root = new Group();
        root.getChildren().add(imageView);

        Canvas canvas = new Canvas(fieldWidth, fieldHeight);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        Scene scene = new Scene(root);
        primaryStage.setTitle("Calabash Battle!");
        primaryStage.setScene(scene);
        primaryStage.show();

        showBattleField(gc);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private static void showBattleField(GraphicsContext gc){
        Brick<Creature>[][] bricks = battle.getBricks();
        Creature creature;
        for (int r = 0; r < fieldRowNum; r++) {
            for (int c = 0; c < fieldColNum; c++) {
                if ((creature = bricks[r][c].getHolder()) != null){
                    Image image = creature.getImage();
                    gc.drawImage(image, startPtX + imageSz * c, startPtY + imageSz * r, imageSz, imageSz);
                }
            }
        }
    }
}
