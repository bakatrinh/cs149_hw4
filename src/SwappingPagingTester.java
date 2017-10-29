import java.util.ArrayList;
import java.util.List;

public class SwappingPagingTester {

    public static void main(String[] args) {
        SetUp process = new SetUp();
        List<Process> processList = new ArrayList<>();
        processList = process.ProcessSetup();
        int count = 0;
        for (Process a : processList) {
            System.out.println(a + " ");
            count++;
        }
        System.out.println("count: " + count);
    }

}
