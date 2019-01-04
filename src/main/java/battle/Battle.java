package battle;

import creature.CBQueue;
import creature.Creature;
import creature.Factions;
import creature.VillainQueue;
import tools.Sorter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.exit;

public class Battle {
    private static final int fieldRowNum = 11, fieldColNum = 13;
    private static BattleField field;
    private static CBQueue cbqueue;
    private static VillainQueue vlQueue;
    private static Creature grandpa;
    private static Creature snake;
    private static List<Creature> creatures;
    private static Formation formation;
    private static boolean battling = false;

    static {
        field = new BattleField(fieldRowNum, fieldColNum);
        cbqueue = new CBQueue();
        vlQueue = new VillainQueue(20); // with the first one as "蝎子精
        grandpa = new Creature("爷爷", Factions.JUSTICE, 't', "pic/grandpa.jpg");
        snake = new Creature("蛇精", Factions.EVIL, 's', "pic/snake.jpg");
        creatures = new ArrayList<>();
        creatures.add(grandpa);
        creatures.addAll(cbqueue.getBroQueue());
        creatures.add(snake);
        creatures.addAll(vlQueue.getVlQueue());
        for (Creature creature: creatures){
            creature.battlePrepare(field, creatures);
        }
        formation = Formation.ARROW;
    }

    public void battlePrepare(boolean sort){
        for (Creature creature: creatures){
            creature.battlePrepare(field, creatures);
        }
        field.clearField();

        if (sort) {
            Sorter sorter = new Sorter();
            System.out.println("creature.CalabashBro queuing...");
            cbqueue.randomQueue();
            System.out.println("Before sorting by name:");
            cbqueue.countOffAcName();
            sorter.SortByName(cbqueue);
            System.out.println("After sorting by name:");
            cbqueue.countOffAcName();
            System.out.println();
        }
        cbqueue.JumpOntoField(field, Formation.HYDRA);
        grandpa.stepOn(field, 9, 1);
        snake.stepOn(field, 9, 9);

        System.out.println("符号说明：");
        System.out.println("葫芦娃：1-7，爷爷：t（拐杖嘛）");
        System.out.println("小喽啰：v（一把钢叉），蝎子精：w（两把钢叉），蛇精：s（魔鬼身材）");
        System.out.println();

        setVlQueueFormation(formation);
    }

    void setVlQueueFormation(Formation form){
        vlQueue.leaveField(field);
        vlQueue.JumpOntoField(field, form);
        System.out.println("长蛇阵 vs " + form.Cname + "阵！ 激战！（最后一句真的中二……");
        System.out.println(field);
        System.out.println();
        System.out.flush();
    }

    public Brick<Creature>[][] getBricks(){
        return field.bricks;
    }

    public int getFieldRowNum(){
        return fieldRowNum;
    }

    public int getFieldColNum(){
        return fieldColNum;
    }

    public void battleBegin() {
        battling = true;
        ExecutorService exec = Executors.newCachedThreadPool();
        for (Creature creature: creatures){
            if (creature.isLive())
                exec.execute(creature);
        }
        exec.shutdown();
        while (true){
            if (exec.isTerminated()){
                break;
            }
        }
        System.out.println("Battle end");
        battling = false;
//        exit(0);
    }

    public boolean isBattling(){
        return battling;
    }

    public List<Creature> getCreatures() {return creatures;}

    public BattleField getField() { return field; }

    public void changeFormation(boolean next){
        if (next)
            formation = Formation.values()[(formation.ordinal() + 1) % 8];
        else
            formation = Formation.values()[formation.ordinal()==0? 7:((formation.ordinal() - 1) % 8)];
//        setVlQueueFormation(formation);
        battlePrepare(false);
    }
}