package org.example.concurrency.example04;

public class ThreadInterruptedTest {
    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            // case 1:
            while (true) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    break;
                }
            }

            // case 2:
            while (!Thread.interrupted()) {
                System.out.println("not interrupted");
            }

            // case 3:
            while (!Thread.interrupted()) {
                System.out.println("long-runnging: cannot be interrupted");
                longRunning();
            }
        });

        t.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(t.isInterrupted());
        t.interrupt();
        System.out.println(t.isInterrupted());
    }

    private static void longRunning() {
        while (true) {}
    }
}
