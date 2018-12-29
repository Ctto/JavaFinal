package gui;

import battle.Battle;
import battle.Brick;
import creature.Creature;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.concurrent.TimeUnit;

public class UIUpdater implements Runnable{
    private static double wdWidth, wdHeight;
    private static int fieldRowNum, fieldColNum;
    private static double startPtX, startPtY = 20, imageSz;

    private Brick<Creature>[][] bricks;
    GraphicsContext gc;

    UIUpdater(Battle battle, GraphicsContext gc, double height, double width){
        this.gc = gc;
        this.bricks = battle.getBricks();
        fieldColNum = battle.getFieldColNum();
        fieldRowNum = battle.getFieldRowNum();
        setSizeInfo(height, width);
    }

    private void setSizeInfo(double height, double width) {
        wdHeight = height;
        wdWidth = width;
        imageSz = (wdHeight - 2*startPtY)/fieldRowNum;
        startPtX = (wdWidth - fieldColNum * imageSz)/2;

    }

    void showBattleField(){
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

    public void run() {
        try {
            showBattleField();
            TimeUnit.MICROSECONDS.sleep(100);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
