package creature;

import java.io.Serializable;

public class Position implements Serializable {
    int placeR = -1, placeC = -1;
    synchronized void setPosition(int r, int c) {
        placeC = c;
        placeR = r;
    }
    public int getPlaceR(){
        return placeR;
    }

    public int getPlaceC(){
        return placeC;
    }

    public String toString(){
        return "(" + placeR + "," + placeC + ")";
    }
}
