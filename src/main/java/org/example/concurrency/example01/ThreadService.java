package org.example.concurrency.example01;

public class ThreadService {
    private Thread executeThread = null;
    private boolean finished = false;

    public void execute(final Runnable task) {
        executeThread = new Thread() {
            @Override
            public void run() {
                Thread runner = new Thread(task);
                runner.setDaemon(true);

                runner.start();
                try {
                    runner.join();
                    finished = true;
                    System.out.println("The task is finished");
                } catch(InterruptedException e) {
                    System.out.println("The task is interrupted");
                }
            }
        };

        executeThread.start();
    }

    public void shutdown(long mills) {
        if (executeThread == null) {
            System.out.println("executeThread isn't created");
            return;
        }

        long currentTime = System.currentTimeMillis();

        while (!finished) {
            if ((System.currentTimeMillis() - currentTime) >= mills) {
                System.out.println("The task is overtime, killing");
                executeThread.interrupt();
                break;
            }

            try {
                executeThread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("The executeThread is interrupted");
                break;
            }
        }

        executeThread = null;
        finished = false;
    }
}
