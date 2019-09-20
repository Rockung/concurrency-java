## 单例模式

性能、懒加载、单例

* 恶汉式 不能懒加载

* 懒加载 可能引起多个实例
* synchronized 性能不够
* double check方式 可能引起NullPointerException
* volatile instance double check方式
* holder
* enum

## 线程的休息室

waitset

放到monitor中

## Java内存模型

* 总线锁 效率低
* 高速缓存一致性协议
  Intel, cpu cache, cache line

* Atomic, Ordering, Visibility

* volatile
    ordering, visibility	

* happens-before

* reordering

## 设计模式

### Observer

monitor thread life cycle

### Single-thread execution

### Immutable object
* final
* private final
* no set-methods
* return with clone
* return with constant list
### Guarded Suspension
### Balking
### Producer-consumer
###Read-write lock
* w - w/r conflict
* r - w/r no conflict
### Thread per message
### Worker thread
### Future
### Two-phase termination
### Thread-specific storage
### Active object
### Count-down