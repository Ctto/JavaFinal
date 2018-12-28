package creature;

import battle.*;
import javafx.scene.image.Image;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.yield;

class Position {
    int placeR = -1, placeC = -1;
    void setPosition(int r, int c) {
        placeC = c;
        placeR = r;
    }
}

public class Creature implements Runnable{
    String CName;
    private Factions factions;
    //    private int placeR, placeC;
    private Position position;
    private char sign;
    private Image image;

    private BattleField field;
    private int borderR, borderC;
    private boolean live;
    private Creature tgEnemy;
    private List<Creature> enemyList;

    public Creature(String CName, Factions factions, char sign, String imagePath) {
        this.CName = CName;
        this.factions = factions;
        position = new Position();
        this.sign = sign;
        if (imagePath != null){
            image = new Image(Creature.class.getResourceAsStream(imagePath));
        }
        live = true;
    }

    public boolean stepOn(BattleField field, int r, int c) {
        Brick<Creature> brick = field.getBrick(r, c);
//        System.err.print(CName + ":" + r + ", " + c);
        if (brick.setHolder(this, sign)){
            position.setPosition(r,c);
//            System.err.println(" OK");
            return true;
        }
        else {
//            System.err.println(" fail");
            return false;
        }
//        synchronized (brick){
//            if (brick.getHolder() == null){
//                brick.setHolder(this);
//                brick.setSign(sign);
//            }
//        }
    }

    void leave(BattleField field) {
        Brick<Creature> brick = field.getBrick(position.placeR, position.placeC);
        brick.setHolder(null, '_');
        position.setPosition(-1, -1);
    }

    public String toString() {
        return CName;
    }

    public Image getImage(){
        return image;
    }

    Position getPosition(){
        return position;
    }

    // for battle
    public void setBattleInfo(BattleField field, List<Creature> creatureList){
        this.field = field;
        borderR = field.getRow();
        borderC = field.getCol();
        enemyList = new ArrayList<>();
        for(Creature creature:creatureList){
            if (creature.factions != factions){
                enemyList.add(creature);
            }
        }
    }

    Creature searchForEnemy(List<Creature> enemyList) {
//        for (Iterator<Creature> it = enemyList.iterator(); it.hasNext(); ) {
//            if (!it.next().isLive())
//                it.remove();
//        }
        enemyList.removeIf(creature -> !creature.isLive());
        if (enemyList.isEmpty())
            return null;
        else
            return enemyList.get(0);
//        for (Creature enemy: enemyList){
//            if (enemy.isLive()){
//                return enemy;
//            }
//        }
    }

    synchronized void stepForward(Creature enemy) {
        int orgPlaceR = position.placeR, orgPlaceC = position.placeC;
        int r = orgPlaceR, c = orgPlaceC;
        leave(field);

        Position pos = enemy.getPosition();
        if (pos.placeR > orgPlaceR) r = orgPlaceR + 1;
        else if (pos.placeR < orgPlaceR) r = orgPlaceR - 1;
        else if (pos.placeC > orgPlaceC) c = orgPlaceC + 1;
        else if (pos.placeC < orgPlaceC) c = orgPlaceC - 1;

        while (true) {
//            System.err.println(CName+"StepForward to " + enemy);
            if (stepOn(field, r, c))
                return;

            Random rd = new Random();
            int act = rd.nextInt(5);
            switch (act){
                case 0: r = orgPlaceR + 1 < borderR ? orgPlaceR + 1: orgPlaceR;
                    c = orgPlaceC;
                    break;
                case 1: r = orgPlaceR - 1 >= 0 ? orgPlaceC - 1:orgPlaceC;
                    c = orgPlaceC;
                    break;
                case 2: c = orgPlaceC + 1 < borderR ? orgPlaceR + 1: orgPlaceR;
                    r = orgPlaceR;
                    break;
                case 3: c = orgPlaceC - 1 >= 0 ? orgPlaceC - 1:orgPlaceC;
                    r = orgPlaceR;
                    break;
                case 4:
                    r = orgPlaceR;
                    c = orgPlaceC;
                    break;
            }
        }
    }

    synchronized public boolean isLive() {
        return live;
    }

    synchronized void beKilled() {
        live = false;
        System.out.println(CName + " is killed.");
    }

    synchronized public void setLive(boolean live) {
        this.live = live;
    }

    void checkAndAttack(){
        for (int diffR = -1; diffR <= 1; diffR++){
            for (int diffC = -1; diffC <= 1; diffC++) {
                Brick<Creature> brick = field.getBrick(position.placeR+diffR, position.placeC+diffC);
                Creature creature;
                if (brick != null && (creature = brick.getHolder()) != null && creature.factions != factions){
                    synchronized (this) {
                        if (!isLive())
                            return;
                        Random random = new Random();
                        if (!creature.fight(random.nextInt())) {
                            beKilled();
                            return;
                        }
                    }
                }
            }
        }
    }

    synchronized boolean fight(int pkNum){
        Random random = new Random();
        int num = random.nextInt();
        if (num < pkNum) {
            beKilled();
            return true;    // enemy win
        }
        return false;
    }

    public void run() {
        try{
            while (isLive() && !enemyList.isEmpty()){
                if (tgEnemy == null || !tgEnemy.isLive())
                    tgEnemy = searchForEnemy(enemyList);
                if (tgEnemy == null)
                    continue;
                if (!isLive())
                    break;
                stepForward(tgEnemy);
                checkAndAttack();
                TimeUnit.SECONDS.sleep(1);
//                yield();
            }
        } catch (InterruptedException e){
//        } catch (Exception e){
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

/*
class Underlings extends creature.Creature{
    Underlings() {
        super("小喽啰", creature.Factions.EVIL, 'v');
    }
    // implements the tools.Generator interface using anonymous inner classes
    public static tools.Generator<Underlings> generator() {
        return new tools.Generator<Underlings>() {
            public Underlings next() {
                return new Underlings();
            }
        };
    }
}
*/
