import java.util.concurrent.Phaser;

/**
 * Created by Alex Korneyko on 16.06.2016 18:03.
 */
public class ThreadsApp {

    public static void main(String[] args) {

        Phaser phaser = new Phaser(1);
        new Thread(new PhaseThread(phaser, "PhaseThread-1")).start();
        new Thread(new PhaseThread(phaser, "PhaseThread-2")).start();

        // ждем завершения фазы 0
        int phase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Фаза " + phase + " завершена");
        // ждем завершения фазы 1
        phase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Фаза " + phase + " завершена");

        // ждем завершения фазы 2
        phase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Фаза " + phase + " завершена");

        // ждем завершения фазы 3
        phase = phaser.getPhase();
        phaser.arriveAndAwaitAdvance();
        System.out.println("Фаза " + phase + " завершена");

        phaser.arriveAndDeregister();
    }
}

class PhaseThread implements Runnable {

    private Phaser phaser;
    private String name;

    PhaseThread(Phaser p, String n) {

        this.phaser = p;
        this.name = n;
        phaser.register();
    }

    public void run() {

        System.out.println(name + " выполняет фазу " + phaser.getPhase());
        try {
            Thread.sleep((long) (Math.random()*2000));
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        phaser.arriveAndAwaitAdvance(); // сообщаем, что первая фаза достигнута

        System.out.println(name + " выполняет фазу " + phaser.getPhase());
        try {
            Thread.sleep((long) (Math.random()*2000));
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        phaser.arriveAndAwaitAdvance(); // сообщаем, что вторая фаза достигнута

        System.out.println(name + " выполняет фазу " + phaser.getPhase());
        try {
            Thread.sleep((long) (Math.random()*2000));
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
        phaser.arriveAndAwaitAdvance(); // сообщаем, что третья фаза достигнута

        System.out.println(name + " выполняет фазу " + phaser.getPhase());
        try {
            Thread.sleep((long) (Math.random()*2000));
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }

        phaser.arriveAndDeregister(); // сообщаем о завершении фаз и удаляем с регистрации объекты
    }
}
