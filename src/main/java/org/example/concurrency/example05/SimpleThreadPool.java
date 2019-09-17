package org.example.concurrency.example05;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SimpleThreadPool extends Thread {
    private int size;

    private final int min;
    private final int max;
    private final int active;
    private final int queueSize;

    private volatile boolean destroy = false;
    private final DiscardPolicy discardPolicy;

    // the sequence for task naming
    private static volatile int seq = 0;

    private final static int DEFAULT_TASK_QUEUE_SIZE = 2000;
    private final static String THREAD_PREFIX = "SIMPLE_THREAD_POOL-";
    private final static ThreadGroup GROUP = new ThreadGroup("Pool_Group");

    // tasks will be scheculed
    private final static LinkedList<Runnable> TASK_QUEUE = new LinkedList<>();
    // threads running
    private final static List<WorkerTask> THREAD_QUEUE = new ArrayList<>();

    public final static DiscardPolicy DEFAULT_DISCARD_POLICY = () -> {
        throw new DiscardException("Discard This Task.");
    };

    public SimpleThreadPool() {
        this(4, 8, 12, DEFAULT_TASK_QUEUE_SIZE, DEFAULT_DISCARD_POLICY);
    }

    // create and run a thread pool
    public SimpleThreadPool(int min, int active, int max, int queueSize, DiscardPolicy discardPolicy) {
        this.min = min;
        this.active = active;
        this.max = max;
        this.queueSize = queueSize;
        this.discardPolicy = discardPolicy;

        init();
    }

    // submit a task into the pool
    public void submit(Runnable runnable) {
        if (destroy)
            throw new IllegalStateException("The thread pool already destroy and not allow submit task.");

        synchronized (TASK_QUEUE) {
            if (TASK_QUEUE.size() > queueSize)
                discardPolicy.discard();
            TASK_QUEUE.addLast(runnable);
            TASK_QUEUE.notifyAll();
        }
    }

    // shutdown the pool
    public void shutdown() throws InterruptedException {
        while (!TASK_QUEUE.isEmpty()) {
            Thread.sleep(50);
        }

        synchronized (THREAD_QUEUE) {
            int initVal = THREAD_QUEUE.size();
            while (initVal > 0) {
                for (WorkerTask task: THREAD_QUEUE) {
                    if (task.getTaskState() == TaskState.Blocked) {
                        task.interrupt();
                        task.close();
                        initVal--;
                    } else {
                        Thread.sleep(10);
                    }
                }
            }

            System.out.println(GROUP.activeCount());
            this.destroy = true;
            System.out.println("The thread pool disposed");
        }
    }

    @Override
    public void run() {
        while (!destroy) {
            System.out.printf("Pool#Min:%d,Active:%d,Max:%d,Current:%d,QueueSize:%d\n",
                    this.min, this.active, this.max, this.size, TASK_QUEUE.size());
            try {
                Thread.sleep(5_000L);
                if (TASK_QUEUE.size() > active && size < active) {
                    for (int i = size; i < active; i++) {
                        createWorkerTask();
                    }
                    System.out.println("The pool incremented to active.");
                    size = active;
                } else if (TASK_QUEUE.size() > max && size < max) {
                    for (int i = size; i < max; i++) {
                        createWorkerTask();
                    }
                    System.out.println("The pool incremented to max.");
                    size = max;
                }

                synchronized (THREAD_QUEUE) {
                    if (TASK_QUEUE.isEmpty() && size > active) {
                        System.out.println("=========Reduce========");
                        int releaseSize = size - active;
                        for (Iterator<WorkerTask> it = THREAD_QUEUE.iterator(); it.hasNext(); ) {
                            if (releaseSize <= 0)
                                break;

                            WorkerTask task = it.next();
                            task.close();
                            task.interrupt();
                            it.remove();
                            releaseSize--;
                        }
                        size = active;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public int getQueueSize() {
        return this.queueSize;
    }

    public int getSize() {
        return this.size;
    }

    public boolean isDestroy() {
        return this.destroy;
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
    }

    public int getActive() {
        return this.active;
    }

    // ==================================== private methods ====================================

    private void init() {
        for (int i = 0; i < this.min; i++) {
            createWorkerTask();
        }

        this.size = min;
        this.start();
    }

    private void createWorkerTask() {
        WorkerTask task = new WorkerTask(GROUP, THREAD_PREFIX + (seq++));
        task.start();
        THREAD_QUEUE.add(task);
    }

    // =====================================helper classes =====================================

    private enum TaskState {
        Free, Running, Blocked, Dead
    }

    public static class DiscardException extends RuntimeException {
        public DiscardException(String message) {
            super(message);
        }
    }

    public interface DiscardPolicy {
        void discard() throws DiscardException;
    }

    /**
     * Represents a working thread
     *   fetch a task to run from the task queue forever.
     *   if the task queue is empty, it waits until a task is submitted.
     */
    private static class WorkerTask extends Thread {
        private volatile TaskState taskState = TaskState.Free;

        public WorkerTask(ThreadGroup group, String name) {
            super(group, name);
        }

        public TaskState getTaskState() {
            return this.taskState;
        }

        public void run() {
            OUTER:
            while (this.taskState != TaskState.Dead) {
                Runnable runnable = null;
                synchronized (TASK_QUEUE) {
                    while (TASK_QUEUE.isEmpty()) {
                        try {
                            taskState = TaskState.Blocked;
                            TASK_QUEUE.wait();
                        } catch (InterruptedException e) {
                            System.out.println("closed.");
                            break OUTER;
                        }
                    }
                    runnable = TASK_QUEUE.removeFirst();
                }

                if (runnable != null) {
                    taskState = TaskState.Running;
                    runnable.run();
                    taskState = TaskState.Free;
                }
            }
        }

        public void close() {
            this.taskState = TaskState.Dead;
        }
    }
}
