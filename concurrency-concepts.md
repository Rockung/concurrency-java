# Basic Concurrency Concepts

## Concurrency vs Parallelism

- Concurrency:  one more tasks run simultaneously(a period) 
  - in a single processor with a single core
  - in a multicore processor
- Parallelism: one more tasks run simultaneously(at the same time, a time point)
  - in different computers
  - in a multicore processor

## Thread safety

a piece of code(or a method or an object) is **thread-safe** if you can use the code in a concurrent application without any problem.

- synchronization mechanisms
- nonblocking compare-and-swap(CAS)
- immutable object

## Synchronization

the coordination of two or more tasks to get the desired results. 

### Two kinds of synchronization

- **Control synchronization**: one task depends on the end of another task
- **Data access synchronization**: shared variables

### Granularity

- **coarse-grained granularity**: big tasks with low intercommunication -- overhead low
- **fine-grained granularity**: small tasks with high intercommunication -- overhead high

### Critical section

a concept closely related to synchronization, which is a piece of code that can be only executed  by a task at any given time because of its access to a shared resource.

### Mutual exclusion

the mechanism used to guarantee critical section access and can be implemented by different ways

### Synchronization mechanism

- Semaphore: control the access to one or more units of a resource
  - **a variable**: stores # of the resources
  - **two atomic operations**: manage the value of the variable

- Monitor: get mutual exclusion over a shared resource
  - a mutex: mutual exclusion
  - a condition variable
  - two operations : wait for the condition, signal the condition 

## Immutable object

you can't modify its visible state after its initialization. If you want to modify an immutable object, you have to create a new one. eg, String class in java.

## Atomic operations and variables

- atomic operation: appears to occur instantaneously to the rest of the tasks of the program
- atomic variable: set and get its value with atomic operations

## Shared memory vs message passing

Tasks can use two different methods to communicate with each other.

- shared memory:  the same memory where tasks write and read values
- message passing:  normally is used when the tasks are running in different computers



------------------------------



# Possible problems in concurrent application

## Data race

also named **race condition**. when you have two or more tasks writing a shared variable outside a critical section -- that's to say, without using any synchronization mechanisms.

## Deadlock

when there are two or more tasks waiting for a shared resource that must be free from the other, so none of them will get the resources they need and will be blocked indefinitely. It happens when four conditions happen simultaneously in the system. They are **Coffman's conditions** as follows

- mutual exclusion
- hold and wait condition
- no pre-emption
- circular wait

## Livelock

for example, you have two tasks -- T1 and T2 -- and both need two resources: R1 and R2. Suppose T1 has a lock on R1, T2 has a lock on R2. As they are unable to gain access to the resource they need, they free their resoureces and begin the cycle again.

## Resource starvation

a task never gets a resource that it needs to continue with its execution. good scheduling algorithm is needed.

## Priority inversion

a low-priority task holds a resource that is needed by a high-priority task, so the low-priority task finishes its execution before the high-priority task.



