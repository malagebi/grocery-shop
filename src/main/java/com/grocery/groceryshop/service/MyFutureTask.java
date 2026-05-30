package com.grocery.groceryshop.service;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author lishunli
 * @since 2020/1/13 11:34
 */
public class MyFutureTask {

    private static final AtomicInteger IDX = new AtomicInteger(1);
    private static ExecutorService executor =
        new ThreadPoolExecutor(8, 20, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(10),
            r -> { Thread t = new Thread(r, "User_Async_FutureTask-" + IDX.getAndIncrement()); t.setDaemon(true); return t; },
            new ThreadPoolExecutor.CallerRunsPolicy());

    private static Integer test() throws Exception {
        Thread.sleep(1000);
        return 1000 - 1;
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        Future<Integer> n = executor.submit(() -> test());
        Future<Integer> n1 = executor.submit(() -> test());
        System.out.println(n.get());
        System.out.println(n1.get());
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

}
