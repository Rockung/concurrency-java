package org.example.concurrency.example04;

public class ThreadExceptionCatcher {
    private final static int A = 10;
    private final static  int B = 0;

    public static void main(String[] args) {
        Thread t = new Thread(() -> {
            try {
                Thread.sleep((2_000L));
                int result = A / B;
                System.out.println(result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        t.setUncaughtExceptionHandler((thread, e) -> {
            System.out.println(e);
            System.out.println(thread);
        });

        t.start();
    }
}
