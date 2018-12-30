package gui;

import battle.Battle;
import battle.BattleField;
import creature.Creature;
import creature.Position;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BattleRecorder {
    Battle battle;
    //    BattleField field;
    private Timeline timeline;
    private static int fileCount = 1;
//    private ObjectOutputStream out;
//    private PrintWriter printWriter;
    private DataOutputStream out;

    public BattleRecorder(Battle battle) {
        this.battle = battle;
        outCount = 0;
//        field = battle.getField();
    }

    public void startRecord() {
        try {
            out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Battle" + fileCount++ + ".out")));
//            out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream("Battle" + fileCount++ + ".out")));
//            printWriter = new PrintWriter("Battle" + fileCount + ".txt");
//            out = new ObjectOutputStream(new FileOutputStream("Battle" + fileCount++ + ".out"));

            timeline = new Timeline();
            timeline.setCycleCount(Timeline.INDEFINITE);
            Duration duration = Duration.millis(10);
            KeyFrame keyFrame = new KeyFrame(duration, new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    outputObject();
                }
            });
            timeline.getKeyFrames().add(keyFrame);
            timeline.play();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int outCount = 0;

    void outputObject() {
        try {
//            out.writeInt(outCount++);
            List<Creature> creatureList = battle.getCreatures();
//            System.err.println(creatureList);     // here, OK, different
//            printWriter.println(creatureList);
//            out.writeObject(creatureList);          // but here...only write the first one...?( creatureList doesn't change..?)
            for (Creature creature : creatureList) {
                out.writeChar(creature.getSign());
                Position position = creature.getPosition();
                out.writeInt(position.getPlaceR());
                out.writeInt(position.getPlaceC());
                out.writeBoolean(creature.isLive());
            }
            out.writeChar(';');
            outCount++;
            if (!battle.isBattling()) {
                out.writeChar('|');
                out.flush();
                out.close();
//                printWriter.flush();
//                printWriter.close();
                System.err.println("out " + outCount);
                timeline.stop();
                System.err.println("Recorder: timeline stop...");
            }
        } catch (IOException e) {
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
