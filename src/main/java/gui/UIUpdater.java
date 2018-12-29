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

    private BattleField field;
    private Brick<Creature>[][] bricks;
    GraphicsContext gc;
    Timeline timeline;

    int count = 0;

    UIUpdater(Battle battle, GraphicsContext gc, double height, double width){
        field = battle.getField();
        this.gc = gc;
        this.bricks = battle.getBricks();
        fieldColNum = battle.getFieldColNum();
        fieldRowNum = battle.getFieldRowNum();
        setSizeInfo(height, width);

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        Duration duration = Duration.millis(100);
        KeyFrame keyFrame = new KeyFrame(duration, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showBattleField();
//                System.err.println(Thread.currentThread());   // Thread[JavaFX Application Thread,5,main]
//                timelineDrawTest(); // once game starts, fixed...
//                System.err.println(field);
            }
        });
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();
    }

    private void setSizeInfo(double height, double width) {
        wdHeight = height;
        wdWidth = width;
        imageSz = (wdHeight - 2*startPtY)/fieldRowNum;
        startPtX = (wdWidth - fieldColNum * imageSz)/2;

    }

    public void showBattleField(){
        gc.clearRect(0,0,wdWidth, wdHeight);
        Creature creature;
        for (int r = 0; r < fieldRowNum; r++) {
            for (int c = 0; c < fieldColNum; c++) {
                if ((creature = bricks[r][c].getHolder()) != null && creature.isLive()){
                    Image image = creature.getImage();
                    gc.drawImage(image, startPtX + imageSz * c, startPtY + imageSz * r, imageSz, imageSz);
                }
            }
        }
    }

    void timelineDrawTest() {
        Image image = new Image(getClass().getResourceAsStream("../creature/pic/grandpa.jpg"));
        gc.clearRect(0,0,wdWidth, wdHeight);
        gc.drawImage(image,count ,0,imageSz, imageSz);
        count = (count + 1) % ((int)(wdWidth-imageSz));
    }

//    public void run() {
//        try {
//            showBattleField();
//            TimeUnit.MICROSECONDS.sleep(100);
//        } catch (InterruptedException e){
//            e.printStackTrace();
//        }
//    }
}
