package org.example.juc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * **volatile** doesn't guarantee **atomic** of operations
 */
public class example01 {
    private final static int NUM_THREADS = 100;
    private final static int NUM_LOOP = 1000;

    private volatile static int sharedValue = 0;
    private static Set<Integer> set = new HashSet<>();

    public static void main(String[] args) {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < NUM_THREADS; i++) {
            Thread t = new Thread() {
                @Override
                public void run() {
                    loopAdd();
                }
            };
            t.start();
            threads.add(t);
        }

        try {
            for (Thread t : threads) {
                t.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Total: " + (NUM_THREADS * NUM_LOOP) + " set: " + set.size() +" value: " + sharedValue);
    }

    private static void loopAdd() {
        int i = 0;
        while (i < NUM_LOOP) {
            sharedValue++;    // it's not an atomic operation
            synchronized (set) {
                set.add(sharedValue);
            }
            i++;
        }
    }
}
