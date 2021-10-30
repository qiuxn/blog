# ThreadLocal原理及场景

## *1.场景*

ThreadLocal为每个线程提供独立的变量副本，常用于存储当前登录用户、connection等

## *2.示例*



```java
public class ThreadLocalTest {
    static ThreadLocal<String> threadLocal = new ThreadLocal<String>();

    private static void print(String str){
        //打印当前线程 本次内存中的threadLocal变量的值
        System.out.println(str + ":" + threadLocal.get());
        //清除当前线程 本次内存中的threadLocal变量的值
        //threadLocal.remove();
    }

    public static void main(String[] args) {
        Thread threadOne = new Thread(new Runnable() {
            public void run() {
                threadLocal.set("threadOne threadLocal value");
                print("threadOne");
                System.out.println("threadOne after remove:"+threadLocal.get());
            }
        });


        Thread threadTwo = new Thread(new Runnable() {
            public void run() {
                threadLocal.set("threadTwo threadLocal value");
                print("threadTwo");
                System.out.println("threadTwo after remove:"+threadLocal.get());
            }
        });
        threadOne.start();
        threadTwo.start();

        System.out.println(threadLocal.get());

    }
}
```

在theadOne内部，是可以拿到自己设置的变量（threadOne threadLocal value），在threadTwo内部，是可以拿到自己设置的变量（threadTwo threadLocal value）。在主线程通过threadLocal去获取，发现返回空

## *3.解析*

```java
public void set(T value) {
    Thread t = Thread.currentThread();
    ThreadLocalMap map = getMap(t);
    if (map != null)
        map.set(this, value);
    else
        createMap(t, value);
}
```

```java
void createMap(Thread t, T firstValue) {
    t.threadLocals = new ThreadLocalMap(this, firstValue);
}
```

当调用threadLocal.set方法时，本质是在当前线程threadLocals变量赋值（Thread内部定义了ThreadLocalMap类型的threadLocals），它是一个Map,key为当前threadLocal 的this引用，value是set的值



## *4.扩展*

### 4.1 threadLocal不支持继承

比如：在父线程设置的登录信息，子线程是无法获取到的，因此就有了InheritableThreadLocal

```java
void createMap(Thread t, T firstValue) {
    t.inheritableThreadLocals = new ThreadLocalMap(this, firstValue);
}
```

如上可知其实是在线程内部维护了inheritableThreadLocals变量。在new thread的时候，其实会将父线程的变量副本，copy到子线程

```java
if (parent.inheritableThreadLocals != null)
    this.inheritableThreadLocals =
        ThreadLocal.createInheritedMap(parent.inheritableThreadLocals);
```

### 4.2 threadLocal引起内存泄漏

ThreadLocalMap中key为threadLocal的弱引用，避免内存泄漏，需要在使用完毕后remove



## 5.代码路径

https://github.com/qiuxn/blog/blob/gh-pages/javabase/src/main/java/com/qiuxn/javabase/thread/threadlocal/ThreadLocalTest.java