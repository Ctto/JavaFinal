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

    public void setSign(char sign){
        this.sign = sign;
    }

    public void setHolder(T holder){
        this.holder = holder;
    }

    public T getHolder(){
        return holder;
    }
}
