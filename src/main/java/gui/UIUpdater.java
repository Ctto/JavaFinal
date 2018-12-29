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

import java.io.EOFException;
import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.util.DuplicateFormatFlagsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.floor;

public class UIUpdater{
//public class UIUpdater implements Runnable{
    private static double wdWidth, wdHeight;
    private static int fieldRowNum, fieldColNum;
    private static double startPtX, startPtY = 20, imageSz;
    private static Map<Character, Image> imageMap = new HashMap<>();

    private Brick<Creature>[][] bricks;
    private GraphicsContext gc;
    Timeline timeline;
    boolean replaying = false;
    ObjectInputStream in;

    int count = 0;

    UIUpdater(Battle battle, GraphicsContext gc, double height, double width){
        List<Creature> creatureList = battle.getCreatures();
        for (Creature creature : creatureList) {
            char sign = creature.getSign();
            if (!imageMap.containsKey(sign))
                imageMap.put(sign, creature.getImage());
        }

        this.gc = gc;
        gc.setFill(new Color(0, 0, 0, 0.5));    // for dead effect
        bricks = battle.getBricks();
        fieldColNum = battle.getFieldColNum();
        fieldRowNum = battle.getFieldRowNum();
        setSizeInfo(height, width);

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        Duration duration = Duration.millis(10);
        KeyFrame keyFrame = new KeyFrame(duration, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!replaying)
                    showBattleField(bricks);
                else {
                    showBattleFieldRePlaying();
                }
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

    void showBattleField(Brick<Creature>[][] bricks){
        gc.clearRect(0,0,wdWidth, wdHeight);
        Creature creature;
        for (int r = 0; r < fieldRowNum; r++) {
            for (int c = 0; c < fieldColNum; c++) {
                if ((creature = bricks[r][c].getHolder()) != null){
//                    Image image = creature.getImage();
                    Image image = imageMap.get(creature.getSign());
                    gc.drawImage(image, startPtX + imageSz * c, startPtY + imageSz * r, imageSz, imageSz);
                    if (!creature.isLive()) {
                        gc.fillRect(startPtX + imageSz * c, startPtY + imageSz * r, imageSz, imageSz);
                    }
                }
            }
        }
    }

    boolean isReplaying() { return replaying; }

    void replayBegin(ObjectInputStream in) {
        this.in = in;
        replaying = true;
    }

    void showBattleFieldRePlaying(){
        try{
            BattleField field = (BattleField)in.readObject();
            if (field != null) {
                Brick<Creature>[][] bricks = field.getBricks();
                showBattleField(bricks);
            } else {
                in.close();
                replaying = false;
                System.err.println("Battle replay over.");
            }
//        } catch (EOFException e){
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
