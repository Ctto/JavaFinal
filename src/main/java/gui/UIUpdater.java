package gui;

import battle.Battle;
import battle.BattleField;
import battle.Brick;
import creature.Creature;
import creature.Position;
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

import java.io.*;
import java.lang.reflect.Field;
import java.util.DuplicateFormatFlagsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.floor;
import static java.lang.System.exit;

public class UIUpdater{
//public class UIUpdater implements Runnable{
    private static double wdWidth, wdHeight;
    private static int fieldRowNum, fieldColNum;
    private static double startPtX, startPtY = 20, imageSz;
    private static Map<Character, Image> imageMap = new HashMap<>();

    Battle battle;
//    BattleField field;
    private Brick<Creature>[][] bricks;
    private GraphicsContext gc;
    Timeline timeline;
    boolean replaying = false;
    DataInputStream in;


    UIUpdater(Battle battle, GraphicsContext gc, double height, double width) {
        this.battle = battle;
//        field = battle.getField();
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
//        Duration duration = Duration.millis(100);
        KeyFrame keyFrame = new KeyFrame(duration, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!replaying) {
                    showBattleField();
                } else {
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
//        startPtX = (wdWidth - fieldColNum * imageSz)/5;
        startPtX = (wdWidth - fieldColNum * imageSz)/2;
    }

    void showBattleField(){
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

    void replayBegin(DataInputStream in) {
        this.in = in;
        replaying = true;
        inObjectCount = 0;
    }

    private int inObjectCount = 0;
    void showBattleFieldRePlaying(){
        try {
//            @SuppressWarnings("unchecked")
            char sign = in.readChar();
            int r, c;
            boolean live;
             if (sign != '|') {
                gc.clearRect(0, 0, wdWidth, wdHeight);
                while (sign != '|' && sign != ';') {
                    r = in.readInt();
                    c = in.readInt();
                    live = in.readBoolean();
                    if (r != -1 && c != -1) {
                        Image image = imageMap.get(sign);
                        gc.drawImage(image, startPtX + imageSz * c, startPtY + imageSz * r, imageSz, imageSz);
                        if (!live) {
                            gc.fillRect(startPtX + imageSz * c, startPtY + imageSz * r, imageSz, imageSz);
                        }
                    }
                    sign = in.readChar();
                }
            }
            if (sign == '|') {
                in.close();
                replaying = false;
                System.err.println("inObjectCount:" + inObjectCount + ", Battle replay over.");
            } // == ';', ctn
        } catch (EOFException e) {
            try {
                in.close();
                replaying = false;
                System.err.println("inObjectCount:" + inObjectCount + ", Battle replay over.");
//                exit(-1);
            } catch (IOException ioE){
                System.err.println("Record file close error.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    int count = 0;
//    void setOutStream(ObjectOutputStream out) {
//        this.out = out;
//        count = 0;
//    }
}
