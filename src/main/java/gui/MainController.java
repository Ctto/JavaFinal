package gui;

import battle.Battle;
import battle.Brick;
import battle.Formation;
import creature.Creature;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.concurrent.TimeUnit;

public class MainController  {
    private Scene mainScene;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Group mainGroup;
    @FXML
    private Canvas mainCanvas;
    private GraphicsContext gc;
    private UIUpdater uiUpdater;

    private static Battle battle;
    static {
        battle = new Battle();
        battle.battlePrepare(true);
    }

    void init(Scene scene, double height, double width) {
//        gc = mainCanvas.getGraphicsContext2D();
        uiUpdater = new UIUpdater(battle, mainCanvas.getGraphicsContext2D(), height, width);
        uiUpdater.showBattleField();

        mainScene = scene;
        mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
//                System.err.println("code:"+event.getCode() + " ,text:" + event.getText());
                if (event.getCode() == KeyCode.SPACE){
                    startBattle();
                }
            }
        });
//        Thread t = new Thread(uiUpdater);
//        t.setDaemon(true);
//        t.start();

//        for (Creature creature: battle.getCreatures()){
//            creature.setUiUpdater(uiUpdater);
//        }
    }

    UIUpdater getUiUpdater() { return uiUpdater; }

    void showBattleField(){
        uiUpdater.showBattleField();
    }

    void startBattle(){
        if (!battle.isBattling()) {
            battle.battlePrepare(false);
            new Thread() {
                @Override
                public void run() {
                    battle.battleBegin();
                }
            }.start();
        }
    }

    //no response - should be set on scene...
//    @FXML
//    public void keyPressHandle(KeyEvent event){
//        System.err.println(event.getCode());
//        if (event.getCode() == KeyCode.SPACE){
//            System.err.println("press Key Space");
//        }
//    }
}
