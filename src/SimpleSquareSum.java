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
        long result = 0;
        Phaser phaser = new Phaser(1);
        List<SquareSumThread> threads = new ArrayList<>();
        IntStream.range(0, numberOfThreads).forEach((i) -> threads.add(new SquareSumThread(partitionOfArray(values, numberOfThreads, i), phaser)));
        threads.stream().forEach(Thread::start);
        phaser.arriveAndAwaitAdvance();
        System.out.println("Возведение в квадрат завершено");
        phaser.arriveAndAwaitAdvance();
        System.out.println("Суммы квадратов посчитаны");

        for (SquareSumThread thread : threads) {
            result += thread.getResult();
        }

        return result;
    }

    private int[] partitionOfArray(int[] source, int partsCount, int partForReturn) {
        int[] result = new int[0];



        return result;
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
        phaser.arriveAndAwaitAdvance();

        result = Arrays.stream(values).sum();
        phaser.arriveAndDeregister();
    }

    public long getResult() {
        return result;
    }
}
