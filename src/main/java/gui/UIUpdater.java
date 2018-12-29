package gui;

import battle.Battle;
import battle.BattleField;
import battle.Brick;
import creature.Creature;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.DuplicateFormatFlagsException;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.floor;

public class UIUpdater{
//public class UIUpdater implements Runnable{
    private static double wdWidth, wdHeight;
    private static int fieldRowNum, fieldColNum;
    private static double startPtX, startPtY = 20, imageSz;

    private Brick<Creature>[][] bricks;
    private GraphicsContext gc;
    Timeline timeline;

    int count = 0;

    UIUpdater(Battle battle, GraphicsContext gc, double height, double width){
        this.gc = gc;
        gc.setFill(new Color(0, 0, 0, 0.5));    // for dead effect
        this.bricks = battle.getBricks();
        fieldColNum = battle.getFieldColNum();
        fieldRowNum = battle.getFieldRowNum();
        setSizeInfo(height, width);

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        Duration duration = Duration.millis(10);
        KeyFrame keyFrame = new KeyFrame(duration, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showBattleField();
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private void setSizeInfo(double height, double width) {
        wdHeight = height;
        wdWidth = width;
        imageSz = (wdHeight - 2*startPtY)/fieldRowNum;
        startPtX = (wdWidth - fieldColNum * imageSz)/5;
    }

    public void showBattleField(){
        gc.clearRect(0,0,wdWidth, wdHeight);
        Creature creature;
//        Image bgImage = new Image(UIUpdater.class.getResourceAsStream("./res/background.jpg"));
        for (int r = 0; r < fieldRowNum; r++) {
            for (int c = 0; c < fieldColNum; c++) {
                if ((creature = bricks[r][c].getHolder()) != null){
                    Image image = creature.getImage();
                    gc.drawImage(image, startPtX + imageSz * c, startPtY + imageSz * r, imageSz, imageSz);
                    if (!creature.isLive()) {
                        gc.fillRect(startPtX + imageSz * c, startPtY + imageSz * r, imageSz, imageSz);
                    }
                }
//                else {
//                    gc.drawImage(bgImage, startPtX + imageSz * c, startPtY + imageSz * r, imageSz, imageSz);
//                }
            }
        }
    }
}
