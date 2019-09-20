package org.example.thread.example02;

import java.util.*;

/**
 * Balance the working threads
 */
public class WorkerBalancer {
    private final static int MAX_WORKER = 5;
    private final static LinkedList<Control> CONTROLS = new LinkedList<>();

    public static void main(String[] args) {
        List<Thread> worker = new ArrayList<>();

        Arrays.asList("M1", "M2", "M3", "M4", "M5", "M6", "M7", "M8", "M9", "M10")
                .stream()
                .map(WorkerBalancer::createWorkerThread)
                .forEach(t -> {
                    t.start();
                    worker.add(t);
                });

        worker.stream().forEach(t -> {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Optional.of("All of work finished").ifPresent(System.out::println);
    }

    private static Thread createWorkerThread(String name) {
        return new Thread(() -> {
            Optional.of("The worker [" + Thread.currentThread().getName() + "] BEGINS work.")
                    .ifPresent(System.out::println);

            synchronized (CONTROLS) {
                while (CONTROLS.size() > MAX_WORKER) {
                    try {
                        CONTROLS.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                CONTROLS.add(new Control());
            }

            Optional.of("The worker [" + Thread.currentThread().getName() + "] is working.")
                    .ifPresent(System.out::println);

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            synchronized (CONTROLS) {
                Optional.of("The worker [" + Thread.currentThread().getName() + "] ENDS work.")
                        .ifPresent(System.out::println);
                CONTROLS.removeFirst();
                CONTROLS.notifyAll();
            }
        }, name);
    }

    private static class Control {}
}
