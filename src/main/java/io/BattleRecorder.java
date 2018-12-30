package io;

import battle.Battle;
import creature.Creature;
import creature.Position;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.io.*;
import java.util.List;

public class BattleRecorder {
    Battle battle;
    private Timeline timeline;
    private static int fileCount = 1;
    private DataOutputStream out;

    public BattleRecorder(Battle battle) {
        this.battle = battle;
    }

    public void startRecord() {
        try {
            out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("Battle" + fileCount++ + ".out")));

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


    void outputObject() {
        try {
            List<Creature> creatureList = battle.getCreatures();
            for (Creature creature : creatureList) {
                out.writeChar(creature.getSign());
                Position position = creature.getPosition();
                out.writeInt(position.getPlaceR());
                out.writeInt(position.getPlaceC());
                out.writeBoolean(creature.isLive());
            }
            out.writeChar(';');
            if (!battle.isBattling()) {
                out.writeChar('|');
                out.flush();
                out.close();
                timeline.stop();
                System.err.println("Recorder: recording stop...");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
