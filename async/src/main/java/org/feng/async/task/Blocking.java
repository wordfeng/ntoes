package org.feng.async.task;

import java.util.concurrent.*;

public class Blocking {

    private final static ExecutorService executor = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws Exception {

//        System.out.println("2020-01-2".compareTo("2020-01-1"));
//        String date = "2020-01";
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
//        try {
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(sdf.parse(date));
//            cal.add(Calendar.MONTH, -1);
//            System.out.println(sdf.format(cal.getTime()));
//        } catch (Exception e) {
//        }
        System.out.println(Runtime.getRuntime().availableProcessors());

        Blocking.executor.shutdownNow();

        System.out.println(executor.isShutdown());

    }

    /**
     * 把先执行完毕的任务拿出来
     *
     * @throws Exception
     */
    void blocking() throws Exception {
        BlockingDeque<String> bq = new LinkedBlockingDeque<>();

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
        }, executor)
                .exceptionally(e -> "");
        //结果异步进入阻塞队列
        executor.execute(() -> {
            try {
                bq.put(f2.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executor.execute(() -> {
            try {
                bq.put(f1.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        for (int i = 0; i < 2; i++) {
            final String take = bq.take();
            System.out.println("take: " + take);
        }
    }

    /**
     * 简化blocking
     *
     * @throws InterruptedException
     * @throws ExecutionException
     */
    void blockingSuper() throws InterruptedException, ExecutionException {
        ExecutorCompletionService<String> service = new ExecutorCompletionService<>(executor);
        service.submit(() -> {
            sleep(1);
            return "f1";
        });
        service.submit(() -> "f2");
        service.submit(() -> "f3");
        service.submit(() -> "f4");

        for (int i = 0; i < 4; i++) {
//            String s = service.take().get();//take()如果阻塞队列为空 则阻塞
//            String s = service.poll().get();//poll()如果阻塞队列为空 返回null，结束
            final Future<String> poll = service.poll();
            String s = poll != null ? poll.get() : "";
            System.out.println(s);
        }
    }




    void sleep(int t) {
        try {
            TimeUnit.SECONDS.sleep(t);
        } catch (Exception e) {

        }
    }
}
