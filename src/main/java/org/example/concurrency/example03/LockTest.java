package org.example.concurrency.example03;

import java.util.Optional;
import java.util.stream.Stream;

public class LockTest {
    public static void main(String[] args) {
        final BooleanLock booleanLock = new BooleanLock();
        Stream.of("T1", "T2", "T3", "T4")
                .forEach(name ->
                        new Thread(() -> {
                            try {
                                booleanLock.lock(1000L);
                                Optional.of(Thread.currentThread().getName() + " get the lock")
                                        .ifPresent(System.out::println);
                                work();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (Lock.TimeoutException e) {
                                Optional.of(Thread.currentThread().getName() + " timeout")
                                        .ifPresent(System.out::println);
                            } finally {
                                booleanLock.unlock();
                            }
                        }, name).start()
                );

        for (int i = 0; i < 6 ; i++) {
            try {
                Thread.sleep(200);
                System.out.println("Blocked threads: " + booleanLock.getBlockedSize());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static void work() throws InterruptedException {
        Optional.of(Thread.currentThread().getName() + " is working ...")
                .ifPresent(System.out::println);
        Thread.sleep(20_000);
    }
}
