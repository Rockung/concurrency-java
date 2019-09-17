package org.example.concurrency.example05;

public class SimpleThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        SimpleThreadPool threadPool = new SimpleThreadPool();

        for (int i = 0; i < 40; i++) {
            threadPool.submit(() -> {
                System.out.println("The runnable  be serviced by " + Thread.currentThread() + " start.");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("The runnable be serviced by " + Thread.currentThread() + " finished.");
            });
        }

        Thread.sleep(10000);
        threadPool.shutdown();

       /* Thread.sleep(10000);
        threadPool.shutdown();
        threadPool.submit(() -> System.out.println("======="));*/
    }
}
