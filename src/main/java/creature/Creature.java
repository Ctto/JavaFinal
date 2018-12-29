package creature;

import battle.*;
import gui.UIUpdater;
import javafx.scene.image.Image;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.yield;

class Position implements Serializable{
    int placeR = -1, placeC = -1;
    void setPosition(int r, int c) {
        placeC = c;
        placeR = r;
    }
}

class LifeState implements Serializable{
    private boolean live = true;
    synchronized void setLive(boolean live){
        this.live = live;
    }
    synchronized boolean isLive(){
        return live;
    }
}

public class Creature implements Runnable, Serializable {
    transient String CName;
    transient private Factions factions;
    private final LifeState lifeState;
    private final Position position;
    private char sign;
    transient private Image image;

    transient private BattleField field;
    transient private int borderR, borderC;
    transient private Creature tgEnemy;
    transient private List<Creature> enemyList;

//    UIUpdater uiUpdater;

    public Creature(String CName, Factions factions, char sign, String imagePath) {
        this.CName = CName;
        this.factions = factions;
        position = new Position();
        this.sign = sign;
        if (imagePath != null){
            image = new Image(Creature.class.getResourceAsStream(imagePath));
        }
        lifeState = new LifeState();
    }

    public boolean stepOn(BattleField field, int r, int c) {
        synchronized (this.position) {
            Brick<Creature> brick = field.getBrick(r, c);
            try {
                if (brick.setHolder(this, sign)) {
                    position.setPosition(r, c);
                    return true;
                } else {
                    return false;
                }
            } catch (NullPointerException e) {
                System.err.println(CName + "is stepping on brick:" + r + ", " + c + ", no such brick");
                e.printStackTrace();
            }
            return false;
        }
    }

    void leave(BattleField field) {
        synchronized (this.position) {
            if (position.placeC != -1 || position.placeR != -1) {
                Brick<Creature> brick = field.getBrick(position.placeR, position.placeC);
                brick.setHolder(null, '_');
            }
            position.setPosition(-1, -1);
        }
    }

    public String toString() {
        return CName;
    }

    public Image getImage(){
        return image;
    }

    public char getSign() {
        return sign;
    }

    Position getPosition(){
        synchronized (this.position) {
            return position;
        }
    }

    // for battle
    public void battlePrepare(BattleField field, List<Creature> creatureList){
        leave(field);   // if on field, leave
        setLive(true);  // if dead, rebirth
        this.field = field;     // set field & border info
        borderR = field.getRow();
        borderC = field.getCol();
        if (enemyList == null)     // get enemyList
            enemyList = new ArrayList<>();
        else
            enemyList.clear();
        for(Creature creature:creatureList){
            if (creature.factions != factions){
                enemyList.add(creature);
            }
        }
    }

    Creature searchForEnemy(List<Creature> enemyList) {
        enemyList.removeIf(creature -> !creature.isLive());
        if (enemyList.isEmpty())
            return null;
        else
            return enemyList.get(0);
    }

//    synchronized void stepForward(Creature enemy) {
    void stepForward(Creature enemy) {
       synchronized (this.position) {
            int orgPlaceR = position.placeR, orgPlaceC = position.placeC;
            int r = orgPlaceR, c = orgPlaceC;
            leave(field);

            Position pos = enemy.getPosition();
            if (pos.placeR > orgPlaceR) r = orgPlaceR + 1 < borderR ? orgPlaceR + 1 : orgPlaceR;
            else if (pos.placeR < orgPlaceR) r = orgPlaceR - 1 >= 0 ? orgPlaceR - 1 : orgPlaceR;
            else if (pos.placeC > orgPlaceC) c = orgPlaceC + 1 < borderC ? orgPlaceC + 1 : orgPlaceC;
            else if (pos.placeC < orgPlaceC) c = orgPlaceC - 1 >= 0 ? orgPlaceC - 1 : orgPlaceC;

            while (true) {
//            System.err.println(CName+"StepForward to " + enemy);
                if (stepOn(field, r, c))
                    return;

                Random rd = new Random();
                int act = rd.nextInt(5);
                switch (act) {
                    case 0:
                        r = orgPlaceR + 1 < borderR ? orgPlaceR + 1 : orgPlaceR;
                        c = orgPlaceC;
                        break;
                    case 1:
                        r = orgPlaceR - 1 >= 0 ? orgPlaceR - 1 : orgPlaceR;
                        c = orgPlaceC;
                        break;
                    case 2:
                        c = orgPlaceC + 1 < borderC ? orgPlaceC + 1 : orgPlaceC;
                        r = orgPlaceR;
                        break;
                    case 3:
                        c = orgPlaceC - 1 >= 0 ? orgPlaceC - 1 : orgPlaceC;
                        r = orgPlaceR;
                        break;
                    case 4:
                        r = orgPlaceR;
                        c = orgPlaceC;
                        break;
                }
            }
        }
    }

    public boolean isLive() {
        return lifeState.isLive();
    }

    void beKilled() {
        lifeState.setLive(false);
        System.err.print(CName + " is killed");
    }

    public void setLive(boolean live) {
        lifeState.setLive(live);
    }

    void checkAndAttack(){
        Creature creature;
        for (int diffR = -1; diffR <= 1; diffR++){
            for (int diffC = -1; diffC <= 1; diffC++) {
                Brick<Creature> brick = field.getBrick(position.placeR+diffR, position.placeC+diffC);
                if (isLive() && brick != null && (creature = brick.getHolder()) != null && creature.factions != factions && creature.isLive()){
                    synchronized (this.lifeState) {
                        System.err.println(CName + " - to fight - " + creature.CName);
                        if (!isLive()) {
                            System.err.println("die before fight...");
                            return;
                        }
                        Random random = new Random();
                        if (!creature.fight(random.nextInt())) {
                            beKilled();
                            System.err.println(" by" + creature.CName);
                            return;
                        }
                        else {
                            System.err.println(" by" + CName);
                            return;
                        }
                    }
                }
            }
        }
    }

    boolean fight(int pkNum){
        synchronized (this.lifeState) {
            Random random = new Random();
            int num = random.nextInt();
            if (num < pkNum) {
                beKilled();
                return true;    // enemy win
            }
            return false;
        }
    }

    public void run() {
        try{
            while (isLive() && !enemyList.isEmpty()){
//                System.err.println(Thread.currentThread());
                if (tgEnemy == null || !tgEnemy.isLive())
                    tgEnemy = searchForEnemy(enemyList);
                if (tgEnemy == null)
                    continue;
                if (!isLive())
                    break;
                stepForward(tgEnemy);
                checkAndAttack();
                TimeUnit.MILLISECONDS.sleep(1000);
            }
            if (!isLive()){
                TimeUnit.MILLISECONDS.sleep(100);
                leave(field);
            }
        } catch (InterruptedException e) {
            System.err.println("Interrupted.");
        }
//        finally {
//            System.err.println("at the end of run");
//        }
    }

}


interface CreatureQueueBehaviors {
    void JumpOntoField(BattleField field, Formation form);
}