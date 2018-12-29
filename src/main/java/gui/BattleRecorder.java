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
    BattleField field;
    private Timeline timeline;
    private static int fileCount = 1;
    private ObjectOutputStream out;

    public BattleRecorder(Battle battle){
        this.battle = battle;
        field = battle.getField();
    }

    public void startRecord(){
        try {
            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Battle" + fileCount + ".out")));
            timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            Duration duration = Duration.millis(10);
            KeyFrame keyFrame = new KeyFrame(duration, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        out.writeObject(field);
                        out.flush();
                        if (!battle.isBattling()){
                            out.writeObject(null);
                            out.flush();
                            out.close();
                            timeline.stop();
//                            System.err.println("Recorder: timeline stop...");
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

//    public void endRecord() {
//        try {
//            timeline.stop();
//            out.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
