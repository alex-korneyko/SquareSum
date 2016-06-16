import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Phaser;
import java.util.stream.IntStream;

/**
 * Используя Phaser и Executors реализовать класс, который бы считал сумму
 * квадратов элементов массива параллельно в заданном количестве потоков
 * <p>
 * Created by Alex Korneyko on 16.06.2016 13:10.
 */
public class SimpleSquareSum implements SquareSum {
    @Override
    public long getSquareSum(int[] values, int numberOfThreads) {
        Phaser phaser = new Phaser(1);
        List<SquareSumThread> threads = new ArrayList<>();

        IntStream.range(0, numberOfThreads).forEach((i) ->
                threads.add(new SquareSumThread(partitionOfArray(values, numberOfThreads, i), phaser)));
        threads.forEach(Thread::start);

        phaser.arriveAndAwaitAdvance();
        phaser.arriveAndAwaitAdvance();

        return threads.stream().mapToLong(SquareSumThread::getResult).sum();
    }

    private int[] partitionOfArray(int[] source, int partsCount, int partForReturn) {
        int[][] result = new int[partsCount][];
        int partLength = source.length / partsCount;
        int lastPartLength = partLength + source.length % partsCount;

        for (int i = 0; i < partsCount; i++) {
            result[i] = new int[i == partsCount - 1 ? lastPartLength : partLength];
            System.arraycopy(source, partLength * i, result[i], 0, i == partsCount - 1 ? lastPartLength : partLength);
        }

        return result[partForReturn];
    }
}

class SquareSumThread extends Thread {

    private int[] values;
    private Phaser phaser;
    private long result;

    SquareSumThread(int[] values, Phaser phaser) {
        this.values = Arrays.copyOf(values, values.length);
        this.phaser = phaser;
        phaser.register();
    }

    @Override
    public void run() {
        IntStream.range(0, values.length).forEach((i) -> values[i] *= values[i]);
        System.out.println(Thread.currentThread().getName() + ": squaring completed");
        phaser.arriveAndAwaitAdvance();

        result = Arrays.stream(values).sum();
        System.out.println(Thread.currentThread().getName() + ": summation completed");
        phaser.arriveAndDeregister();
    }

    public long getResult() {
        return result;
    }
}
