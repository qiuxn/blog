package com.qiuxn.javabase.thread.threadlocal;

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
