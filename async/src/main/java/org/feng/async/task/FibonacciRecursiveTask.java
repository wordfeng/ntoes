package org.feng.async.task;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class FibonacciRecursiveTask {

    public static void main(String[] args) {
        new FibonacciRecursiveTask().forkJoinPool();
    }

    void forkJoinPool() {
        ForkJoinPool forkJoinPool = new ForkJoinPool(3);
        FibonacciRecursiveTask.Fib fib = new FibonacciRecursiveTask.Fib(10000);
        Long invoke = forkJoinPool.invoke(fib);
        System.out.println(invoke);
    }

    static class Fib extends RecursiveTask<Long> {
        final long n;

        public Fib(long n) {
            this.n = n;
        }

        @Override
        protected Long compute() {
            if (n <= 1) {
                return n;
            }
            Fib fib1 = new Fib(n - 1);
            //创建子任务
            fib1.fork();
            Fib fib2 = new Fib(n - 2);
            fib2.fork();
            return fib2.compute() + fib1.join();
        }
    }

}
