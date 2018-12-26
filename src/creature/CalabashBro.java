package creature;


public class CalabashBro extends Creature {
    private Color color;
    private int placeInQue;
    private int seq;

    CalabashBro(String CName, Factions factions, Color color, int seq, int placeInQue, char sign, String imagePath) {
        super(CName, factions, sign, imagePath);
        this.color = color;
        this.seq = seq;
        this.placeInQue = placeInQue;
    }

    public int getSeq() {
        return seq;
    }

    /*
    String getName() {
        return CName;
    }
    */

    public Color getColor() {
        return color;
    }

    public void changePlaceInQue(int idx, boolean report) {
        int oldPlace = placeInQue;
        placeInQue = idx;
        if (report){
            System.out.println(CName + ": " + oldPlace + " -> " + placeInQue);
        }
    }
}
