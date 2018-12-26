package creature;

import battle.*;
import javafx.scene.image.Image;

public class Creature {
    String CName;
    private Factions factions;
    private int placeR, placeC;
    private char sign;
    private Image image;

    public Creature(String CName, Factions factions, char sign, String imagePath) {
        this.CName = CName;
        this.factions = factions;
        placeR = -1;
        placeC = -1;
        this.sign = sign;
        if (imagePath != null){
            image = new Image(Creature.class.getResourceAsStream(imagePath));
        }
    }

    public void stepOn(BattleField field, int r, int c) {
        placeC = c;
        placeR = r;
        field.bricks[r][c].setHolder(this);
        field.bricks[r][c].setSign(sign);
    }

    void leave(BattleField field){
        field.bricks[placeR][placeC].setHolder(null);
        field.bricks[placeR][placeC].setSign('_');
        placeR = -1;
        placeC = -1;
    }

    public String toString() {
        return CName;
    }

    public Image getImage(){
        return image;
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
