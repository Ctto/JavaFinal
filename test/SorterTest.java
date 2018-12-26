import creature.CBQueue;
import org.junit.Test;
import tools.Sorter;

public class SorterTest {

    @Test(timeout = 20)     // ?timeout = 20ms, with 31ms...Test passed?
    public void testSortByName(){
//    public void testSortByName() throws Exception{
        CBQueue cbQueue = new CBQueue();
        cbQueue.randomQueue();
        System.out.println("Before sorting by name:");
        cbQueue.countOffAcName();
        new Sorter().SortByName(cbQueue); // tools.BubbleSort
        System.out.println("After sorting by name:");
        cbQueue.countOffAcName();
        System.out.println();
//        Thread.sleep(100);
    }

    @Test(timeout = 20)
    public void testSortByColor() {
        CBQueue cbQueue = new CBQueue();
        cbQueue.randomQueue();
        System.out.println("Before sorting by color:");
        cbQueue.countOffAcName();
        cbQueue.countOffAcColor();
        new Sorter().SortByColor(cbQueue);    // tools.BiSort
        System.out.println("After sorting by color:");
        cbQueue.countOffAcName();
        cbQueue.countOffAcColor();
    }
}