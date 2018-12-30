package gui;

import battle.Battle;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainController  {
    private Scene mainScene;
    @FXML
    private AnchorPane mainPane;
    @FXML
    private Group mainGroup;
    @FXML
    private Canvas mainCanvas;
    @FXML
    private Button btnLastForm, btnNextForm, btnBattleBegin, btnLoadRecord, btnBattleReplay;
    private List<Button> buttonList = new ArrayList<>();
    {
        Collections.addAll(buttonList, btnLastForm, btnNextForm, btnBattleBegin, btnLoadRecord, btnBattleReplay);
    }


    @FXML
    private Rectangle rectangle;

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
        uiUpdater.showBattleField();

        mainStage = stage;
        mainScene = scene;
//        mainScene.
        mainScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
//                System.err.println("code:"+event.getCode() + " ,text:" + event.getText());
                KeyCode keyCode = event.getCode();
                if (keyCode == KeyCode.SPACE) {
                    onBattleBegin();
                } else if (keyCode == KeyCode.UP || keyCode == KeyCode.LEFT) {
                    onLastForm();
                } else if (keyCode == KeyCode.DOWN || keyCode == KeyCode.RIGHT) {
                    onNextForm();
                } else if (keyCode == KeyCode.L) {
                    onLoadRecord();
                } else if (keyCode == KeyCode.P){
                    onBattleReplay();
                }
                event.consume();
            }
        });
        mainScene.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mainCanvas.requestFocus();
            }
        });
    }

    @FXML
    void disableKey(){ }

    @FXML
    void onLastForm(){
        if (!battle.isBattling() && !uiUpdater.isReplaying()){
            uiUpdater.setReplayReady(false);
            changeFormation(false);
        }
    }

    @FXML
    void onNextForm(){
        if (!battle.isBattling() && !uiUpdater.isReplaying()){
            uiUpdater.setReplayReady(false);
            changeFormation(true);
        }
    }

    @FXML
    void onBattleBegin(){
        if (!battle.isBattling() && !uiUpdater.isReplaying()){
            uiUpdater.setReplayReady(false);
            startBattle();
        }
    }

    @FXML
    void onLoadRecord(){
        if (!battle.isBattling() && !uiUpdater.isReplaying()) {
            loadRecord();
        }
    }

    @FXML
    void onBattleReplay(){
        if (!battle.isBattling() && !uiUpdater.isReplaying()) {
            replayBattle();
        }
    }

    /* OnKeyPressed - SPACE */
    void startBattle(){
        try {
            battleRecorder = new BattleRecorder(battle);
            battleRecorder.startRecord();
            TimeUnit.MILLISECONDS.sleep(50);
            new Thread() {
                @Override
                public void run() {
                    battle.battleBegin();
                }
            }.start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /* OnKeyPressed - UP、DOWN、LEFT、RIGHT */
    void changeFormation(boolean next) {
        battle.changeFormation(next);
    }

    /* OnKeyPressed - L */
    void loadRecord() {
        recordLoader = new RecordLoader(mainStage);
        recordLoader.openFile();
        if (recordLoader == null || !recordLoader.replayPrepare())
            return;
        uiUpdater.replayPrepare(recordLoader.getInputStream());
        System.err.println("recordLoader OK, uiUpdater ready to replay");
    }

    /* OnKeyPressed - P */
    void replayBattle() {
        if (recordLoader == null || recordLoader.isClosed())
            return;
        uiUpdater.replayBegin();
    }
}
