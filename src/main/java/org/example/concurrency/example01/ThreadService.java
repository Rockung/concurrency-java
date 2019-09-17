package org.example.concurrency.example01;

public class ThreadService {
    private Thread parentThread = null;
    private boolean finished = false;

    public void execute(final Runnable task) {
        parentThread = new Thread() {
            @Override
            public void run() {
                Thread child = new Thread(task);
                child.setDaemon(true);

                child.start();
                try {
                    child.join();
                    finished = true;
                    System.out.println("The task is finished");
                } catch(InterruptedException e) {
                    System.out.println("The task is interrupted");
                }
            }
        };

        parentThread.start();
    }

    public void shutdown(long mills) {
        if (parentThread == null) {
            System.out.println("The parent thread isn't created");
            return;
        }

        long currentTime = System.currentTimeMillis();

        while (!finished) {
            if ((System.currentTimeMillis() - currentTime) >= mills) {
                System.out.println("The task is overtime, killing");
                parentThread.interrupt();
                break;
            }

            try {
                parentThread.sleep(1);
            } catch (InterruptedException e) {
                System.out.println("The parent thread is interrupted");
                break;
            }
        }

        parentThread = null;
        finished = false;
    }
}
