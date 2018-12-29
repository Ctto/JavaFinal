package gui;

import battle.Battle;
import battle.BattleField;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.io.*;

public class BattleRecorder {
    Battle battle;
//    BattleField field;
    private Timeline timeline;
    private static int fileCount = 1;
    private ObjectOutputStream out;

    public BattleRecorder(Battle battle){
        this.battle = battle;
//        field = battle.getField();
    }

    private static int outCount = 0;
    public void startRecord(){
        try {
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Battle" + fileCount++ + ".out")));
//            out = new ObjectOutputStream(new FileOutputStream("Battle" + fileCount++ + ".out"));
            timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            Duration duration = Duration.millis(10);
            KeyFrame keyFrame = new KeyFrame(duration, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
//                        out.writeObject(field);
                        out.writeObject(battle.getCreatures());
//                        out.flush();
                        outCount++;
//                        System.err.println("out "+outCount);
                        if (!battle.isBattling()){
                            out.writeObject(null);
                            out.flush();
                            out.close();
                            System.err.println("out "+outCount);
                            timeline.stop();
                            System.err.println("Recorder: timeline stop...");
                        }
                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            });
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

//    public void recordPrepare(){
//        try {
//            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Battle" + fileCount++ + ".out")));
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//    public ObjectOutputStream getOutStream() {
//        return out;
//    }

//    public void endRecord() {
//        try {
//            timeline.stop();
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
