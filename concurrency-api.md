# Java Concurrency API

## Basic concurrency classes

- **Thread** class: represents all the threads that execute a concurrent  java application
- **Runnable** interface: another way to create concurrent applications
- **ThreadLocal** class:  stores variables locally to a thread
- **ThreadFactory** interface: create customized threads using Factory design pattern

## Synchronization mechanisms

- **synchronized** keyword: define a critical section in a block of code or in an entire method
- **Lock** interface: more flexible than **synchronized**
  - ReentrantLock: be associated with a condition
  - ReentrantReadWriteLock: separates read and write operations
  - StampedLock: 3 modes for controlling read/write access
- **Semaphore** class: classical semaphore, binary/general
- **CountDownLatch** class: allows task to wait for the finalization of multiple operations
- **CyclicBarrier** class: allows the synchronization of multiple threads in a common point
- **Phaser** class: allows to control the execution of tasks divided into phases. None of the tasks advance to the next phase unitl all of the tasks have finished the current phase

## Executors

the executor framework allows to separate thread creation and management for the implementation of concurrent tasks.

- **Executor**/**ExecutorService** interface: methods common to all executors

- **ThreadPoolExecutor**:  get an executor with a pool of threads and optionally define a maximum number of parallel tasks

- **ScheduledThreadPoolExecutor**:  execute tasks after a delay or periodically

- **Executors**: facilitates the creation of executors

- **Callable** interface: an alternative to the Runnable interface, a separate task that can return a value

- **Future** interface: methods to obtain the value returned by a Callable interface and to control its status

  

## Fork/Join framework

  defines a special kind of executor specialized in the resolution of problems with the divide and conquer technique; optimize the execution of the concurrent tasks that solve these kinds of problems; tailored for fine-grained parallelism at a very low overhead by queuing the task for execution.

- **ForkJoinPool**:  the executor that is going to run the tasks
- **ForkJoinTask**: a task that can be executed in **ForkJoinPool**
- **ForkJoinWorkerThread**: a thread that is going to execute tasks in **ForkJoinPool**

## Concurrent data structures

normal data structures are not ready to work in a concurrent application unless you use an external synchronization mechanism. thread-safe data structures are classified into two groups: **blocking** and **non-blocking** . If the operation can be made on **non-blocking** one, it won't block the calling tasks. Otherwise, it returns the **null** value or throws an **exception**.

- **ConcurrentLinkedDeque**: non-blocking list
- **ConcurrentLinkedQueue**: non-blocking queue
- **LinkedBlockingDeque**: blocking list
- **LinkedBlockingQueue**: blocking queue
- **PriorityBlockingQueue**: blocking queue that orders its elements based on its priority
- **ConcurrentSkipListMap**: non-blocking navigable map
- **CocurrentHashMap**: non-blocking hash map
- **AtomicBoolean**, **AtomicInteger**, **AtomicLong**, **AtomicReference**: atomic implementations of basic types

## Parallel streams

* Stream
* Optional
* Collectors
* Lambda expression



  

   