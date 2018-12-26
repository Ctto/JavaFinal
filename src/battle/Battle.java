package battle;

import creature.CBQueue;
import creature.Creature;
import creature.Factions;
import creature.VillainQueue;
import tools.Sorter;

public class Battle {
    private static final int fieldRowNum = 11, fieldColNum = 20;
    private static BattleField field;
    private static CBQueue cbqueue;
    private static VillainQueue vlQueue;
    private static Creature grandpa;
    private static Creature snake;

    static {
        field = new BattleField(fieldRowNum, fieldColNum);
        cbqueue = new CBQueue();
        vlQueue = new VillainQueue(20); // with the first one as "蝎子精
        grandpa = new Creature("爷爷", Factions.JUSTICE, 'T', "./pic/grandpa.jpg");
        snake = new Creature("蛇精", Factions.EVIL, 'S', "./pic/snake.jpg");
    }

    public void battlePrepare(){
//    public static void CbQueueSorting(){
        Sorter sorter = new Sorter();
        System.out.println("creature.CalabashBro queuing...");
        cbqueue.randomQueue();
        System.out.println("Before sorting by name:");
        cbqueue.countOffAcName();
        sorter.SortByName(cbqueue);
        System.out.println("After sorting by name:");
        cbqueue.countOffAcName();
        System.out.println();
        cbqueue.JumpOntoField(field, Formation.HYDRA);

        grandpa.stepOn(field, 9, 4);
        snake.stepOn(field, 9, 16);

        System.out.println("符号说明：");
        System.out.println("葫芦娃：1-7，爷爷：T（拐杖嘛）");
        System.out.println("小喽啰：v（一把钢叉），蝎子精：w（两把钢叉），蛇精：S（魔鬼身材）");
        System.out.println();
    }

    public void setVlQueueFormation(Formation form){
        vlQueue.leaveField(field);
        vlQueue.JumpOntoField(field, form);
        System.out.println("长蛇阵 vs " + form.Cname + "阵！ 激战！（最后一句真的中二……");
        System.out.println(field);
        System.out.println();
    }

    public Brick<Creature>[][] getBricks(){
        return field.bricks;
    }
//    public static void main(String[] args) {
//        battlePrepare();
//
//        vlQueue.JumpOntoField(field, Formation.HOOKEDSPEAR);
//        System.out.println("长蛇阵 vs 偃月阵！ 激战！（最后一句好中二……");
//        //field.ShowField();
//        System.out.println(field);
//        System.out.println();
//
//        vlQueue.leaveField(field);
//        vlQueue.JumpOntoField(field, Formation.ARROW);
//        System.out.println("长蛇阵 vs 锋矢阵！ 激战！（最后一句真的中二……");
//        //field.ShowField();
//        System.out.println(field);
//    }

    public int getFieldRowNum(){
        return fieldRowNum;
    }

    public int getFieldColNum(){
        return fieldColNum;
    }
}