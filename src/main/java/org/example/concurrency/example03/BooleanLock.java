package org.example.concurrency.example03;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class BooleanLock implements Lock {
    private boolean locked;
    private Thread currentThread;
    private Collection<Thread> blockedThreads = new ArrayList<>();

    public BooleanLock() {
        this.locked = false;
    }

    /**
     * lock without timeout
     *   if the lock is occupied by other thread, wait until the other
     *   thread releases the lock
     *
     * @throws InterruptedException
     */
    @Override
    public synchronized void lock() throws InterruptedException {
        while (locked) {
            blockedThreads.add(Thread.currentThread());
            this.wait();
            blockedThreads.remove(Thread.currentThread());
        }

        this.currentThread = Thread.currentThread();
        this.locked = true;
    }

    /**
     * lock with timeout
     *   if mills <= 0, it's same as <b>lock()</b>
     *
     * @param mills the timeout by milliseconds.
     * @throws InterruptedException
     * @throws TimeoutException
     */
    @Override
    public synchronized void lock(long mills) throws InterruptedException, TimeoutException {
        if (mills <= 0) {
            lock();
            return;
        }

        long remaining = mills;
        long endTime = System.currentTimeMillis() + mills;
        while(locked) {
            if (remaining <= 0)
                throw new TimeoutException("Time out when getting the lock");

            blockedThreads.add(Thread.currentThread());
            this.wait(mills);
            blockedThreads.remove(Thread.currentThread());

            remaining = endTime - System.currentTimeMillis();
        }

        this.currentThread = Thread.currentThread();
        this.locked = true;
    }

    @Override
    public synchronized void unlock() {
        if (Thread.currentThread() == currentThread) {
            Optional.of(Thread.currentThread().getName() + " release the lock monitor")
                    .ifPresent(System.out::println);

            this.locked = false;
            this.notifyAll();
        }
    }

    @Override
    public Collection<Thread> getBlockedThreads() {
        return Collections.unmodifiableCollection(blockedThreads);
    }

    @Override
    public int getBlockedSize() {
        return blockedThreads.size();
    }
}
