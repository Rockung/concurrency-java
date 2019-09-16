# Concurrency Design Patterns

## Signaling

How to implement the situation where a task has to notify an event to another task. The easiest way to implement this pattern is with a semaphore or a mutex, using **ReentrantLock** or **Semaphore** or even the **wait**() and **notify**() in **Object** class.

```java
public void task1() {
    section1();
    commonObject.notify();
}

public void task2() {
    commonObject.wait();
    section2();
}
```

Under these circumstances, the section2() method will always be executed after the section1() method.

## Rendezvous

a generalization of the **Signaling** pattern.  must use two objects instead of one.

```java
public void task1() {
	section1_1();
    commonObject1.notify();
	commonObject2.wait();
	section1_2();
}

public void task2() {
    section2_1();
    commonObject2.notify();
    commonObject1.wait();
    section2_2();
}
```

if you put the call to the wait() method before the call to the notify() method, you will have a deadlock.

## Mutex

implement a critical section ensuring mutual exclusion. That is to say, only one task can execute the portion of code protected by the mutex at one time.

```java
public void task() {
    preCriticalSection();
    lockObject.lock() // The critical section begins
    criticalSection();
    lockObject.unlock(); // The critical section ends
    postCriticalSection();
}
```

## Multiplex

generalization of the mutex.  a determined number of tasks can execute the critical section at once.

```java
public void task() {
    preCriticalSection();
    semaphoreObject.acquire();
    criticalSection();
    semaphoreObject.release();
    postCriticalSection();
}
```

## Barrier

how to implement the situation where you need to synchronize some tasks at a common point. None of the tasks can continue with their execution until all the tasks have arrived at the synchronization point.

```java
public void task() {
    preSyncPoint();
    barrierObject.await();
    postSyncPoint();
}
```



## Double-checked locking

when you acquire a lock and then check for a condition, if the condition is false, you have had the overhead of acquiring the lock ideally.  Eg. 

```java
public class Singleton{
    private static Singleton reference;
    private static final Lock lock=new ReentrantLock();
    
    public static Singleton getReference() {
        lock.lock();
        try {
            if (reference == null) {
            	reference = new Object();
            }
        } finally {
        	lock.unlock();
        }
        return reference;
    }
}
```

If you include the lock inside the condition, and if two tasks check the condition at once, you will create two objects.

```java
public class Singleton{
    private static Singleton reference;
    private static final Lock lock=new ReentrantLock();
    
    public static Singleton getReference() {
        if (reference == null) {
            lock.lock();
            try {
                if (reference == null) {
                    reference = new Object();
                }
            } finally {
                lock.unlock();
            }
        }
        
        return reference;
    }
}
```

The best solution to this problem doesn't use any explicit synchronization mechanism:

```java
public class Singleton {
    private static class LazySingleton {
    	private static final Singleton INSTANCE = new Singleton();
    }
    
    public static Singleton getSingleton() {
    	return LazySingleton.INSTANCE;
    }
}
```

## Read-write lock

when you modify a shared variable a few times but read it many times, if you protect access to the shared variable with a lock,  it provides poor performance because all the read operations can be made concurrently without any problem. 

defines a special kind of lock with two internal locks: one for read, one for write.

- T1, T2 can do a read operation at the same time
- T1 do read, T2 do write, T2 is blocked
- T1 do write, T2 do do read/write, T2 is blocked

## Thread pool

remove the overhead introduced by creating a thread for the task you want to execute.

it's formed by a set of threads and a queue of tasks you want to execute

the set of threads usually has a fixed size

when a thread approaches the execution of a task, it doesn't finish its execution; it looks for another task in the queue, and executes it

if not, the thread waits until a task is inserted in the queue

## Thread local storage

each thread accesses a different instance of the variable

