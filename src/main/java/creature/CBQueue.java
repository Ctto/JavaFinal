package creature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import battle.*;

public class CBQueue implements CreatureQueueBehaviors {
    List<CalabashBro> broQueue;

    public CBQueue() {
        broQueue = new ArrayList<>();
        broQueue.add(new CalabashBro("老大", Factions.JUSTICE, Color.RED, 1, 0, '1', "./pic/red.jpg"));
        broQueue.add(new CalabashBro("老二", Factions.JUSTICE, Color.ORANGE, 2, 1, '2', "./pic/orange.jpg"));
        broQueue.add(new CalabashBro("老三", Factions.JUSTICE, Color.YELLOW, 3, 2, '3', "./pic/yellow.jpg"));
        broQueue.add(new CalabashBro("老四", Factions.JUSTICE, Color.GREEN, 4, 3, '4', "./pic/green.jpg"));
        broQueue.add(new CalabashBro("老五", Factions.JUSTICE, Color.CYAN, 5, 4, '5', "./pic/cyan.jpg"));
        broQueue.add(new CalabashBro("老六", Factions.JUSTICE, Color.BLUE, 6, 5, '6', "./pic/blue.jpg"));
        broQueue.add(new CalabashBro("老七", Factions.JUSTICE, Color.VIOLET, 7, 6, '7', "./pic/violet.jpg"));

    }

    public void randomQueue() {
        /*
        // Knuth Shuffle
        Random rd = new Random();
        for (int i = 6; i > 0; i--) {
            int idx = rd.nextInt(i+1);
            creature.CalabashBro temp = broQueue[idx];
            broQueue[idx] = broQueue[i];
            broQueue[idx].changePlaceInQue(idx, false); // here... changePlace ... need to improve...->Object Oriented
            broQueue[i] = temp;
            broQueue[i].changePlaceInQue(i, false);
        }
        */
        System.out.println("Shuffling...");
        Collections.shuffle(broQueue, new Random(47));
        for (CalabashBro bro : broQueue) {
            int place = broQueue.indexOf(bro);
            bro.changePlaceInQue(place, false);
        }
    }

    public void countOffAcName() {
        for (int i = 0; i < 6; i++) {
            //System.out.print(broQueue.get(i).getName()+" ");
            System.out.print(broQueue.get(i)+" ");
        }
        //System.out.println(broQueue.get(6).getName());
        System.out.println(broQueue.get(6));
    }

    public void countOffAcColor() {
        for (int i = 0; i < 6; i++) {
            System.out.print(broQueue.get(i).getColor().CName+" ");
        }
        System.out.println(broQueue.get(6).getColor().CName);
    }

    public void JumpOntoField(BattleField field, Formation form){
        int idx = 0;
        for (int r = 0; r < 11; r++) {
            for (int c = 2; c < 3; c++) {
                if (form.form[r][c+3]){
                    broQueue.get(idx).stepOn(field, r, c);
                    idx++;
                }
            }
        }
    }

    public List<CalabashBro> getBroQueue() {
        return broQueue;
    }
}
