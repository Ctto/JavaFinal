package tools;

import java.util.*;

public class Generators {
    //static <T> Collection<T> fill(Collection<T> clt, tools.Generator<T> gen, int num) {
    public static <T> void fill(Collection<T> clt, Generator<T> gen, int num) {
        for (int i = 0; i < num; i++)
            clt.add(gen.next());
        //return clt;
    }
}