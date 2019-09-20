package org.example.juc;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * failfast when locking
 *  use AtomicInteger.compareAndSet atomic operation
 */
public class example04 {

    public static void main(String[] args) {
        CompareAndSetLock lock = new CompareAndSetLock();

        for (int i = 0; i < 5; i++) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        lock.tryLock();
                        doLongRunningTask();
                    } catch (LockException e) {
                        System.out.println(Thread.currentThread().getName() + " cannot get the lock");
                    } finally {
                        lock.unlock();
                    }
                }
            }.start();
        }
    }

    public static void doLongRunningTask() {
        System.out.println(Thread.currentThread().getName() + " is working ...");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static class CompareAndSetLock {
        private Thread thread = null;  // the tread owning the lock
        private AtomicInteger atomic = new AtomicInteger(0);

        public void tryLock() throws LockException {
            boolean success = atomic.compareAndSet(0, 1);
            if (!success)
                throw new LockException("Cannot get lock");

            this.thread = Thread.currentThread();
        }

        public void unlock() {
            if (atomic.get() == 0) // not locked
                return;

            // only the thread owing the lock can do
            if (thread == Thread.currentThread()) {
                atomic.compareAndSet(1, 0);
                thread = null;
            }
        }
    }

    static class LockException extends Exception {
        public LockException(String message) {
            super(message);
        }
    }

}
