package battle;

public class Brick<T> {
    char sign;
    T holder;
    Brick() {
        sign = '_';
        holder = null;
    }
    public String toString(){
        return Character.toString(sign);
    }

//    synchronized public void setSign(char sign){ this.sign = sign; }

//    synchronized public void setHolder(T holder){ this.holder = holder; }

    synchronized public T getHolder(){
        return holder;
    }

    synchronized public boolean setHolder(T holder, char sign) {
        // leave
        if (holder == null){
            this.holder = null;
            this.sign = '_';
            return true;
        }
        // stepOn
        if (this.holder != null)
            return false;
        this.holder = holder;
        this.sign = sign;
        return true;
    }


}
