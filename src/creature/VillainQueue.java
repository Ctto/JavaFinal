package creature;

import tools.*;
import battle.*;

import java.util.ArrayList;
import java.util.List;

class UnderlingsGenerator implements Generator<Creature> {
    public Creature next()  {
        return new Creature("小喽啰", Factions.EVIL, 'v', "./pic/bat.jpg");
    }
}

public class VillainQueue implements CreatureQueueBehaviors {
    private List<Creature> vlQueue;
    private int numTotal, numOnField;

    public VillainQueue(int n) {
        vlQueue = new ArrayList<>();
        vlQueue.add(new Creature("蝎子精", Factions.EVIL, 'w', "./pic/scorpion.jpg"));

        /*
        creature.Creature[] underlings = new creature.Creature[n-1];
        for (creature.Creature c:underlings)
            c = new creature.Creature("小喽啰", creature.Factions.EVIL, 'v');    //Arrays.fill()?
        Collections.addAll(vlQueue, underlings);
        */
        Generators.fill(vlQueue, new UnderlingsGenerator(), n-1);
        //tools.Generators.fill(vlQueue, Underlings.generator(), n-1);
        //Incompatible equality constraint: Underlings and creature.Creature
        // gen: tools.Generator<T> - Underlings.generator()(tools.Generator<Underlings>)
        numTotal = n;
    }

    public void JumpOntoField(BattleField field, Formation form){
        int idx = 0;
        for (int r = 0; r < 11; r++) {
            for (int c = 0; c < 10; c++) {
                if (form.form[r][c]){
                    vlQueue.get(idx).stepOn(field, r, c+10);
                    idx++;
                }
            }
        }
        numOnField = idx;
    }

    public void leaveField(BattleField field) {
        for (int i = 0; i < numOnField; i++) {
            vlQueue.get(i).leave(field);
        }
        numOnField = 0;
    }
}
