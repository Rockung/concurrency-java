package org.example.concurrency.example01;

/**
 * Force a long-running thread to exit
 *   When a parent thread exits, its child daemon threads also exit.
 */
public class ThreadCloseForce {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();

        ThreadService service = new ThreadService();
        service.execute(() -> {
            // while (true) {}
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(1000);
                    System.out.println("execute task: " + i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        service.shutdown(10000);

        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
