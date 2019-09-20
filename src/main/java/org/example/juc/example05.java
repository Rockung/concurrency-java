package org.example.juc;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * use atomic variables to coordinate thread execution
 */
public class example05 {
    private final static AtomicBoolean running = new AtomicBoolean(true);

    public static void main(String[] args) throws InterruptedException {
        new Thread() {
            @Override
            public void run() {
                while (running.get()) {
                    try {
                        Thread.sleep(1000);
                        System.out.println("I'm working ...");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("I'm finished!");
            }
        }.start();

        Thread.sleep(5000);
        running.set(false);
    }
}
