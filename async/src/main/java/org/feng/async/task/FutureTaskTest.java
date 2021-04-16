package org.feng.async.task;

import java.util.Optional;
import java.util.concurrent.*;

public class FutureTaskTest {
    private final static ExecutorService executor = Executors.newFixedThreadPool(2);


    String task() throws ExecutionException, InterruptedException, TimeoutException {

        CompletableFuture<String> f1 = CompletableFuture.supplyAsync(() -> {
            System.out.println("running f1...");
            sleep(4);
            System.out.println("f1 completed");
            return "f1";
        }, executor);
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(() -> {
            System.out.println("running f2...");
            sleep(1);
            System.out.println("f2 completed");
            return "f2";
        }, executor);
        CompletableFuture<String> f3 = f1.thenCombine(f2,
                (f1Str, f2Str) -> "f3: " + f1Str + " " + f2Str);
//        final String join = f3.join();
//        System.out.println(join);
        return f3.get(5, TimeUnit.SECONDS);
    }


    void exceptionTask() {
        CompletableFuture<Integer> f = CompletableFuture
                .supplyAsync(() -> 7 / 0)
                .thenApply(r -> r * 10)
                .exceptionally(e -> 999999);
        System.out.println(f.join());
    }

    public String asyncString(ExecutorService executorService) throws ExecutionException, InterruptedException {
        String x = null;
        for (int i = 0; i < 3; i++) {
            Callable<String> callable = () -> "dsa";
            FutureTask<String> task = new FutureTask<>(callable);
            executorService.submit(task);

            Callable<String> callable2 = () -> "2233";
            FutureTask<String> task2 = new FutureTask<>(callable2);
            executorService.submit(task2);

            if (task.isDone() && task2.isDone()) {
                x = task.get() + task2.get();
                task.cancel(true);
                task2.cancel(true);
            }
        }
        return Optional.ofNullable(x).orElse("dsa");
    }

    void sleep(int t) {
        try {
            TimeUnit.SECONDS.sleep(t);
        } catch (Exception e) {

        }
    }
}
