package gui;

import battle.Battle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MainController  {
    private Scene mainScene;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Group mainGroup;
    @FXML
    private Canvas mainCanvas;
    private GraphicsContext gc;
    private Stage mainStage;
    private UIUpdater uiUpdater;
    private BattleRecorder battleRecorder;
    private RecordLoader recordLoader;

    private static Battle battle;
    static {
        battle = new Battle();
        battle.battlePrepare(true);
    }

    void init(Stage stage, Scene scene, double height, double width) {
        gc = mainCanvas.getGraphicsContext2D();
        uiUpdater = new UIUpdater(battle, gc, height, width);
        uiUpdater.showBattleField(battle.getBricks());

        mainStage = stage;
        mainScene = scene;
        mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
//                System.err.println("code:"+event.getCode() + " ,text:" + event.getText());
                KeyCode keyCode = event.getCode();
                if (!battle.isBattling() && !uiUpdater.isReplaying()) {
                    if (keyCode == KeyCode.SPACE) {
                        startBattle();
                    } else if (keyCode == KeyCode.UP || keyCode == KeyCode.LEFT) {
                        changeFormation(false);
                    } else if (keyCode == KeyCode.DOWN || keyCode == KeyCode.RIGHT) {
                        changeFormation(true);
                    } else if (keyCode == KeyCode.L) {
                        loadRecord();
                    } else if (keyCode == KeyCode.P){
                        replayBattle();
                    }
                }
            }
        });
    }

    /* OnKeyPressed - SPACE */
    void startBattle(){
        battle.battlePrepare(false);
        battleRecorder = new BattleRecorder(battle);
        battleRecorder.startRecord();
        new Thread() {
                @Override
                public void run() {
                    battle.battleBegin();
                }
            }.start();
    }

    /* OnKeyPressed - UP、DOWN、LEFT、RIGHT */
    void changeFormation(boolean next) {
        battle.changeFormation(next);
    }

    /* OnKeyPressed - L */
    void loadRecord() {
        recordLoader = new RecordLoader(mainStage);
        recordLoader.openFile();
    }

    /* OnKeyPressed - P */
    void replayBattle() {
        if (recordLoader == null || !recordLoader.replayPrepare())
            return;
        System.err.println("recordLoader OK, uiUpdater prepare to replay");
        uiUpdater.replayBegin(recordLoader.getInputStream());
    }
}
