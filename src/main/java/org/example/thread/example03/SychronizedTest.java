package org.example.thread.example03;

/**
 * Show a problem of <b>synchronized</b> keyword
 *   A synchronized method can block the other synchronized methods
 */
public class SychronizedTest {
    public static void main(String[] args) throws InterruptedException {
        // The first thread runs a long-running task
        new Thread() {
            @Override
            public void run() {
                longRunning();
            }
        }.start();

        Thread.sleep(1000);

        // The second thread runs a short-running task, but blocked
        // by the long-running task
        Thread t2 = new Thread() {
            @Override
            public void run() {
                shortRunning();
            }
        };
        t2.start();

        Thread.sleep(2000);
        t2.interrupt();
        System.out.println(t2.isInterrupted());
    }

    private synchronized static void longRunning() {
        System.out.println(Thread.currentThread());
        while (true) {}
    }

    private synchronized static void shortRunning() {
        System.out.println(Thread.currentThread());
    }
}
