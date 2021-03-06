import java.util.stream.IntStream;

/**
 * Created by Alex Korneyko on 16.06.2016 13:13.
 */
public class Main {
    public static void main(String[] args) {

        SimpleSquareSum simpleSquareSum = new SimpleSquareSum();
        System.out.println("Result: "+simpleSquareSum.getSquareSum(rndIntArray(100), 3));
    }

    private static int[] rndIntArray(int size) {
        int[] result = new int[size];
        IntStream.range(0, size).forEach((i) -> result[i] = (int) (Math.random() * 100));
        System.out.println("Array was generated");
        return result;
    }
}
