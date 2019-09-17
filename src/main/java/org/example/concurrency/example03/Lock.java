package org.example.concurrency.example03;

import java.util.Collection;

public interface Lock {
    class TimeoutException extends Exception {
        public TimeoutException(String message) {
            super(message);
        }
    }

    // lock/unlock
    void lock() throws InterruptedException;
    void lock(long mills) throws InterruptedException, TimeoutException;
    void unlock();

    Collection<Thread> getBlockedThreads();
    int getBlockedSize();
}
